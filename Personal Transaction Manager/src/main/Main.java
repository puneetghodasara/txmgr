package main;

import java.util.Collection;
import java.util.List;

import me.puneetghodasara.txmgr.exception.DuplicateException;
import me.puneetghodasara.txmgr.integration.AccountRepository;
import me.puneetghodasara.txmgr.integration.RuleRepository;
import me.puneetghodasara.txmgr.integration.TransactionRepository;
import me.puneetghodasara.txmgr.manager.AccountManager;
import me.puneetghodasara.txmgr.manager.StatementManager;
import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.model.db.BankEnum;
import me.puneetghodasara.txmgr.model.db.Rule;
import me.puneetghodasara.txmgr.model.db.Transaction;
import me.puneetghodasara.txmgr.util.CSVRuleReader;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {

		Logger logger = Logger.getLogger(Main.class);

		ApplicationContext appContext = new ClassPathXmlApplicationContext("spring.xml");

		AccountRepository accountRepository = (AccountRepository) appContext.getBean("accountRepository");
		TransactionRepository transactionRepository = (TransactionRepository) appContext.getBean("transactionRepository");
		AccountManager accountManager = (AccountManager) appContext.getBean("accountManager");
		StatementManager statementManager = (StatementManager) appContext.getBean("statementManager");
		RuleRepository ruleRepository = (RuleRepository) appContext.getBean("ruleRepository");
		
		CSVRuleReader ruleLoader = new CSVRuleReader();
		Collection<Rule> allRules = ruleLoader.loadAllRules("rule.csv");
		
		allRules.forEach(rule->{
			if(!ruleRepository.isRuleExist(rule.getRule())){
				ruleRepository.saveRule(rule);
			}
		});

		logger.info("Rules found "+allRules.size());
		
		Account iciciAccount = accountRepository.getAccountByName("ICICI-1");
		if (iciciAccount == null) {
			logger.debug("Creating new ICICI account");
			try {
				iciciAccount = accountManager.createAccount("ICICI-1", BankEnum.ICICI, AccountTypeEnum.BANK_ACCOUNT, "ICICI-TAG");
			} catch (DuplicateException e) {
			}
		}
		logger.debug("Account is "+iciciAccount);
		
		
		try {
			statementManager.processStatement(iciciAccount, "ICICI.xls");
		} catch (Exception e) {
			logger.error("Exception in getting statement entries : "+e.getMessage());
			logger.error(e);
			return;
		}

		logger.info("End");
	}
}
