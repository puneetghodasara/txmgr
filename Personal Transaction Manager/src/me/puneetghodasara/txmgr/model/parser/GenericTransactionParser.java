package me.puneetghodasara.txmgr.model.parser;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import main.Main;
import main.SetupUtil;
import me.puneetghodasara.txmgr.exception.DuplicateException;
import me.puneetghodasara.txmgr.integration.RuleRepository;
import me.puneetghodasara.txmgr.manager.AccountManager;
import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.model.db.BankEnum;
import me.puneetghodasara.txmgr.model.db.Rule;
import me.puneetghodasara.txmgr.model.db.Transaction;
import me.puneetghodasara.txmgr.model.db.TransactionDetail;
import me.puneetghodasara.txmgr.util.UnmatchedTransactionWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "transactionParser")
public class GenericTransactionParser implements TransactionParser {

	private static final Logger logger = Logger.getLogger(GenericTransactionParser.class);

	@Autowired
	private RuleRepository ruleRepository;

	@Autowired
	private AccountManager accountManager;

	@Override
	public void parseTransaction(Account account, Transaction transaction) {
		logger.debug("Parsing Transaction " + transaction);

		final String txDesc = transaction.getDescription();

		Rule matchedRule = null;

		List<Rule> matchedRuleList = ruleRepository.getAllRules().stream().filter(rule -> (StringUtils.containsIgnoreCase(txDesc, rule.getRule())))
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
			txDetail = getTransferDetail(account, matchedRule, txDesc);
			transaction.setTransactionDetail(txDetail);
			return;
		} else if (matchedRule != null) {
			txDetail = getTransactionDetail(matchedRule);
			transaction.setTransactionDetail(txDetail);
			return;
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
	}

	private TransactionDetail getTransferDetail(Account account, Rule matchedRule, String txDesc) {

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
			txDtl.setCategory("Transfer");
			txDtl.setMerchant(account.getName());
			Account targetAcc = accountManager.getAccountByNumber(accNo);
			if (targetAcc == null) {
				try {
					AccountTypeEnum accType = AccountTypeEnum.valueOf(matchedRule.getMerchant());
					// Override if null
					if (accType == null)
						accType = AccountTypeEnum.BANK_ACCOUNT;
					targetAcc = accountManager.createAccount("Auto Generated", accNo, BankEnum.UNKNOWN_BANK, accType, "");
				} catch (DuplicateException e) {
				}
			}
			txDtl.setTargetAccount(targetAcc);
			txDtl.setViaCard(false);
			txDtl.setWay("Electronic");
			return txDtl;
		}
		return null;
	}

	/*
	 * private TransactionDetail getAccountSpecificTransactionDetail(Account
	 * account, String txDesc) { Pattern recurringDepositPattern =
	 * Pattern.compile("To RD Ac no ([0-9]+)");
	 * 
	 * Matcher recurringDepositMatcher =
	 * recurringDepositPattern.matcher(txDesc); if
	 * (recurringDepositMatcher.find()) { String rdAccNo =
	 * recurringDepositMatcher.group(1); TransactionDetail txDtl = new
	 * TransactionDetail(); txDtl.setCategory("Saving");
	 * txDtl.setMerchant("ICICI"); Account rdAcc =
	 * accountManager.getAccountByName("ICICI-RD-" + rdAccNo); if (rdAcc ==
	 * null) { try { rdAcc = accountManager.createAccount("ICICI-RD-" + rdAccNo,
	 * BankEnum.ICICI_BANK, AccountTypeEnum.FD_ACCOUNT, "RD"); } catch
	 * (DuplicateException e) { } } txDtl.setTargetAccount(rdAcc);
	 * txDtl.setViaCard(false); txDtl.setWay("Direct"); return txDtl; }
	 * 
	 * return null; }
	 */
	private Rule getBestMatchingRule(Transaction transaction, List<Rule> matchedRuleList) {
		// TODO Auto-generated method stub
		return matchedRuleList.get(matchedRuleList.size() - 1);
	}

	private TransactionDetail getTransactionDetail(Rule rule) {
		TransactionDetail txDetail = new TransactionDetail();
		txDetail.setCategory(rule.getCategory());
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

	public RuleRepository getRuleRepository() {
		return ruleRepository;
	}

	public void setRuleRepository(RuleRepository ruleRepository) {
		this.ruleRepository = ruleRepository;
	}

	public AccountManager getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

}
