package me.puneetghodasara.txmgr.core.parser.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.config.SetupUtil;
import me.puneetghodasara.txmgr.core.integration.RuleRepository;
import me.puneetghodasara.txmgr.core.manager.AccountManager;
import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.core.model.db.BankEnum;
import me.puneetghodasara.txmgr.core.model.db.Rule;
import me.puneetghodasara.txmgr.core.model.db.Transaction;
import me.puneetghodasara.txmgr.core.model.db.TransactionCategory;
import me.puneetghodasara.txmgr.core.model.db.TransactionDetail;
import me.puneetghodasara.txmgr.core.parser.TransactionProcessor;
import me.puneetghodasara.txmgr.core.util.RuleProblemWriter;

@Component("transactionProcessor")
public class TransactionProcessorImpl implements TransactionProcessor {

	@Resource
	private AccountManager accountManager;

	@Resource
	private RuleRepository ruleRepository;

	@Override
	public TransactionDetail getTransferDetail(Rule matchedRule, String txDesc) {

		// Get RegEx pattern
		String pattern = SetupUtil.getProp().getProperty(matchedRule.getPropRef());

		// Initialize all three parameters as null first
		Integer accNoGroupNo = null, refNoGroupNo = null, tagNoGroupNo = null;
		try {
			accNoGroupNo = Integer.parseInt(SetupUtil.getProp().getProperty(matchedRule.getPropRef() + "_ACC_NO"));
			refNoGroupNo = Integer.parseInt(SetupUtil.getProp().getProperty(matchedRule.getPropRef() + "_REF_NO"));
			tagNoGroupNo = Integer.parseInt(SetupUtil.getProp().getProperty(matchedRule.getPropRef() + "_TAG_NO"));
		} catch (NumberFormatException nfe) {
			// Ignore, as we have null
		}
		if (StringUtils.isBlank(pattern) || accNoGroupNo == null) {
			RuleProblemWriter.write("Rule " + matchedRule.getRule() + " has error in property reference.");
			return null;
		}

		// Match
		Pattern customPattern = Pattern.compile(pattern);
		Matcher customMatcher = customPattern.matcher(txDesc);
		if (customMatcher.matches()) {
			String accNo = customMatcher.group(accNoGroupNo);
			String refNo = refNoGroupNo == null ? "" : customMatcher.group(refNoGroupNo);
			String tagDesc = tagNoGroupNo == null ? "" : customMatcher.group(tagNoGroupNo);

			TransactionDetail txDtl = new TransactionDetail();
			TransactionCategory category;
			try {
				category = TransactionCategory.valueOf(matchedRule.getCategory());
			} catch (IllegalArgumentException e) {
				RuleProblemWriter.write(
						"Rule " + matchedRule.getRule() + " has undefined category " + matchedRule.getCategory());
				return null;
			}
			txDtl.setCategory(category);
			txDtl.setMerchant("");
			AccountTypeEnum accType = null;
			try {
				accType = AccountTypeEnum.valueOf(matchedRule.getTargetAccType());
			} catch (IllegalArgumentException e) {
				RuleProblemWriter.write("Rule " + matchedRule.getRule() + " has undefined account type "
						+ matchedRule.getTargetAccType());
				return null;
			}

			Account targetAcc = accountManager.getAccountByNumber(accNo);
			if (targetAcc == null) {
				targetAcc = accountManager.getAccountByNumber(accNo);
				if (targetAcc == null) {
					// First time create
					targetAcc = accountManager.createAccount("Auto Generated", accNo, BankEnum.NO_BANK, accType, "");
				}
			}
			txDtl.setTargetAccount(targetAcc);

			txDtl.setWay("Electronic");
			txDtl.setTag("" + refNo + tagDesc);
			return txDtl;
		}
		return null;
	}

	@Override
	public TransactionDetail getExpenseDetail(Rule matchedRule) {

		TransactionDetail txDetail = new TransactionDetail();
		TransactionCategory category;
		try {
			category = TransactionCategory.valueOf(matchedRule.getCategory());
		} catch (IllegalArgumentException e) {
			RuleProblemWriter
					.write("Rule " + matchedRule.getRule() + " has undefined category " + matchedRule.getCategory());
			return null;
		}
		txDetail.setCategory(category);

		txDetail.setMerchant(matchedRule.getMerchant());
		txDetail.setWay("Direct");
		return txDetail;

	}

	@Override
	public Rule getMatchingRule(Transaction transaction) {
		// TODO Auto-generated method stub

		List<Rule> matchedRuleList = ruleRepository.findAll().stream()
				.filter(rule -> (StringUtils.containsIgnoreCase(transaction.getDescription(), rule.getRule())))
				.collect(Collectors.toList());

		if(matchedRuleList == null || matchedRuleList.isEmpty())
			return null;
		
		if (matchedRuleList.size() == 1) {
			return matchedRuleList.get(0);
		}

		return selectOneRule(transaction, matchedRuleList);
	}

	private Rule selectOneRule(Transaction transaction, List<Rule> matchedRuleList) {
		return matchedRuleList.get(matchedRuleList.size() - 1);
	}

}
