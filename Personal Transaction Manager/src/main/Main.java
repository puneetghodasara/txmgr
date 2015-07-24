package main;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import me.puneetghodasara.txmgr.exception.CustomException;
import me.puneetghodasara.txmgr.exception.CustomException.ExceptionType;
import me.puneetghodasara.txmgr.integration.AccountRepository;
import me.puneetghodasara.txmgr.integration.RuleRepository;
import me.puneetghodasara.txmgr.integration.TransactionRepository;
import me.puneetghodasara.txmgr.manager.AccountManager;
import me.puneetghodasara.txmgr.manager.StatementManager;
import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.Rule;
import me.puneetghodasara.txmgr.util.CSVRuleReader;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	private static final Logger logger = Logger.getLogger(Main.class);

	public static ApplicationContext appContext = new ClassPathXmlApplicationContext("spring.xml");

	static{
		try {
			SetupUtil.setup(appContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		AccountRepository accountRepository = (AccountRepository) appContext.getBean("accountRepository");
		TransactionRepository transactionRepository = (TransactionRepository) appContext.getBean("transactionRepository");
		AccountManager accountManager = (AccountManager) appContext.getBean("accountManager");
		StatementManager statementManager = (StatementManager) appContext.getBean("statementManager");
		RuleRepository ruleRepository = (RuleRepository) appContext.getBean("ruleRepository");

		// Load rules
		Collection<Rule> allRules = null;
		try {
			allRules = CSVRuleReader.loadAllRules("rule.csv");
		} catch (CustomException e1) {
			e1.printStackTrace();
		}

		// Save new rules
		allRules.forEach(rule -> {
			if (!ruleRepository.isRuleExist(rule.getRule())) {
				ruleRepository.saveRule(rule);
			}
		});

		// Get Statements

		try {
			getAllStatements().entrySet().stream().forEach(entry -> {
				System.out.println("Account :" + entry.getKey() + " - " + entry.getValue());
				entry.getValue().parallelStream().forEach(stmt -> {
					try {
						statementManager.processStatement(entry.getKey(), stmt);
					} catch (Exception e) {
						logger.error("Exception in processing statement entries : " + e.getMessage());
						logger.error(e);
					}
				});
			});
		} catch (CustomException e) {
			logger.error("Exception in getting statement entries : " + e.getMessage());
		}

		logger.info("End");
	}

	private static final String ROOT_DIR = "statement";

	public static HashMap<Account, Set<String>> getAllStatements() throws CustomException {
		File rootFolder = new File(ROOT_DIR);
		if (!rootFolder.exists()) {
			logger.error("ROOT Folder is not available.");
			throw new CustomException(ExceptionType.NO_FOLDER_AVBL);
		}

		// Create a HashMap for Bank,Statement
		HashMap<Account, Set<String>> allStatements = new HashMap<Account, Set<String>>();
		Arrays.asList(rootFolder.listFiles((file) -> {
			return file.getName().toUpperCase().contains(".XLS");
		})).stream().forEach(file -> {
			Account acc = getAccountFromFileName(file.getName());
			if (acc != null && allStatements.get(acc) == null) {
				allStatements.put(acc, new HashSet<String>());
			}

			if (acc != null) {
				allStatements.get(acc).add(file.getAbsolutePath());
			} else {
				logger.warn("File is not matching with any bank code : " + file.getAbsolutePath());
			}
		});

		logger.info("Got " + allStatements.size() + " statements to parse.");
		return allStatements;
	}

	private static Account getAccountFromFileName(String name) {
		AccountManager accountManager = (AccountManager) appContext.getBean("accountManager");
		Account citiCardAcc = accountManager.getAccountByName("CITI-CC");
		Account citiAcc = accountManager.getAccountByName("CITI");
		Account iciciAcc = accountManager.getAccountByName("ICICI");
		if (name.toUpperCase().contains("CITI-CC")) {
			return citiCardAcc;
		} else if (name.toUpperCase().contains("ICICI")) {
			return iciciAcc;
		} else if (name.toUpperCase().contains("CITI")) {
			return citiAcc;
		} else {
			return null;
		}

	}

}
