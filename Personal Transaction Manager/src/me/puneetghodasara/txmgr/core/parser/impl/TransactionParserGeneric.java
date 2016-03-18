package me.puneetghodasara.txmgr.core.parser.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.config.SetupUtil;
import me.puneetghodasara.txmgr.core.exception.CustomException;
import me.puneetghodasara.txmgr.core.integration.RuleRepository;
import me.puneetghodasara.txmgr.core.manager.AccountManager;
import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.core.model.db.BankEnum;
import me.puneetghodasara.txmgr.core.model.db.Rule;
import me.puneetghodasara.txmgr.core.model.db.Transaction;
import me.puneetghodasara.txmgr.core.model.db.TransactionCategory;
import me.puneetghodasara.txmgr.core.model.db.TransactionDetail;
import me.puneetghodasara.txmgr.core.parser.TransactionParser;
import me.puneetghodasara.txmgr.core.util.UnmatchedTransactionWriter;

@Component(value="TransactionParser")
public class TransactionParserGeneric implements TransactionParser {

	// Logger 
	private static final Logger logger = Logger.getLogger(TransactionParserGeneric.class);
	
	@Autowired
	private RuleRepository ruleRepository;

	@Autowired
	private AccountManager accountManager;
	
	@Override
	public TransactionDetail parseTransaction(Transaction transaction) {
		logger.debug("Parsing Transaction " + transaction);

		final String txDesc = transaction.getDescription();

		Rule matchedRule = null;

		List<Rule> matchedRuleList = ruleRepository.findAll().stream().filter(rule -> (StringUtils.containsIgnoreCase(txDesc, rule.getRule())))
				.collect(Collectors.toList());

		if (matchedRuleList.size() == 1) {
			matchedRule = matchedRuleList.get(0);
		} else if (matchedRuleList.size() > 1) {
			// Write logic for best match
			matchedRule = getBestMatchingRule(transaction, matchedRuleList);
		}

		TransactionDetail txDetail = null;
		if (matchedRule != null && matchedRule.getPropRef() != null) {
			// It is a reference to read property file
			txDetail = getTransferDetail(transaction, matchedRule, txDesc);
			transaction.setTransactionDetail(txDetail);
			return txDetail;
		} else if (matchedRule != null) {
			txDetail = getTransactionDetail(matchedRule);
			transaction.setTransactionDetail(txDetail);
			return txDetail;
		}

		/*
		 * txDetail = getAccountSpecificTransactionDetail(account, txDesc); if
		 * (txDetail != null) { transaction.setTransactionDetail(txDetail);
		 * return; }
		 */
		// ask for a rule
		logger.info("No match for " + transaction);
		UnmatchedTransactionWriter.write(transaction);

		logger.debug("Transaction Parsed " + transaction);
		return txDetail;
	
	}

	private TransactionDetail getTransferDetail(Transaction transaction, Rule matchedRule, String txDesc) {

		String pattern = SetupUtil.getProp().getProperty(matchedRule.getPropRef());
		Integer accNoGroupNo = null, refNoGroupNo = null, tagNoGroupNo = null;
		try {
			accNoGroupNo = Integer.parseInt(SetupUtil.getProp().getProperty(matchedRule.getPropRef() + "_ACC_NO"));
			refNoGroupNo = Integer.parseInt(SetupUtil.getProp().getProperty(matchedRule.getPropRef() + "_REF_NO"));
			tagNoGroupNo = Integer.parseInt(SetupUtil.getProp().getProperty(matchedRule.getPropRef() + "_TAG_NO"));
		} catch (NumberFormatException nfe) {
			// Ignore
		}
		if (StringUtils.isBlank(pattern) || accNoGroupNo == null) {
			// TODO log this
			return null;
		}

		Pattern customPattern = Pattern.compile(pattern);
		Matcher customMatcher = customPattern.matcher(txDesc);
		if (customMatcher.matches()) {
			String accNo = customMatcher.group(accNoGroupNo);
			String refNo = refNoGroupNo == null ? "" : customMatcher.group(refNoGroupNo);
			String tagDesc = tagNoGroupNo == null ? "" : customMatcher.group(tagNoGroupNo);

			TransactionDetail txDtl = new TransactionDetail();
			txDtl.setCategory(TransactionCategory.TRANSFER);
			txDtl.setMerchant(transaction.getAccount().getName());
			Account targetAcc = accountManager.getAccountByNumber(accNo);
			if (targetAcc == null) {
				try {
					AccountTypeEnum accType = AccountTypeEnum.valueOf(matchedRule.getMerchant());
					// Override if null
					if (accType == null)
						accType = AccountTypeEnum.MAIN_ACCOUNT;
					targetAcc = accountManager.createAccount("Auto Generated", accNo, BankEnum.NO_BANK, accType, "");
				} catch (CustomException e) {
				}
			}
			txDtl.setTargetAccount(targetAcc);
			txDtl.setWay("Electronic");
			return txDtl;
		}
		return null;
	}


	private Rule getBestMatchingRule(Transaction transaction, List<Rule> matchedRuleList) {
		// TODO Auto-generated method stub
		return matchedRuleList.get(matchedRuleList.size() - 1);
	}

	private TransactionDetail getTransactionDetail(Rule rule) {
		TransactionDetail txDetail = new TransactionDetail();
		TransactionCategory category;
		try {
			category = TransactionCategory.valueOf(rule.getCategory());
		} catch (IllegalArgumentException e) {
			category = TransactionCategory.UNKNOWN;
		}
		
		txDetail.setCategory(category);
		txDetail.setMerchant(rule.getMerchant());
		String targetAccount = rule.getTargetAccount();
		if (StringUtils.isNotBlank(targetAccount)) {
			Account account = accountManager.getAccountByName(targetAccount);
			if (account != null) {
				txDetail.setTargetAccount(account);
				txDetail.setWay("Direct");
			}
		}
		return txDetail;
	}

}
