package main;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.puneetghodasara.txmgr.core.engine.InputEngine;
import me.puneetghodasara.txmgr.core.engine.StatementEngine;
import me.puneetghodasara.txmgr.core.exception.CustomException;
import me.puneetghodasara.txmgr.core.exception.CustomException.ExceptionType;
import me.puneetghodasara.txmgr.core.integration.RuleRepository;
import me.puneetghodasara.txmgr.core.manager.AccountManager;
import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.model.db.Rule;
import me.puneetghodasara.txmgr.core.model.db.Statement;
import me.puneetghodasara.txmgr.core.parser.RuleParser;
import me.puneetghodasara.txmgr.core.util.DefaultEntries;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	private static final Logger logger = Logger.getLogger(Main.class);

	public static ApplicationContext appContext = new ClassPathXmlApplicationContext("spring.xml");

	
	public static void main(String[] args) {

		SetupUtil su = new SetupUtil();
		su.setApplicationContext(appContext);
		try {
			su.afterPropertiesSet();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
//		AccountRepository accountRepository = (AccountRepository) appContext.getBean("accountRepository");
//		TransactionRepository transactionRepository = (TransactionRepository) appContext.getBean("transactionRepository");
//		AccountManager accountManager = (AccountManager) appContext.getBean("accountManager");
		RuleRepository ruleRepository = (RuleRepository) appContext.getBean("ruleRepository");

		RuleParser csvRuleParser = (RuleParser) appContext.getBean("ruleParser");
		
		// Load rules
		Collection<Rule> allRules = null;
		try {
			allRules = csvRuleParser.getRules();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Save new rules
		allRules.forEach(rule -> {
			if (!ruleRepository.isRuleExist(rule.getRule())) {
				logger.info("Saving New Rule :"+rule);
				ruleRepository.saveRule(rule);
			}
		});
		
		DefaultEntries de = (DefaultEntries) appContext.getBean("dbGen");
		de.enter();

		// Get Statements
		StatementEngine se = (StatementEngine) appContext.getBean("statementEngine");
		List<Statement> allStatements = null;
		try {
			allStatements = se.getAllStatements();
		} catch (CustomException e) {
			e.printStackTrace();
		}
		logger.info("Got Statements");

//		CoreEngine ce = (CoreEngine) appContext.getBean("statementProcessor");
		InputEngine ce = (InputEngine) appContext.getBean("inputEngine");
		
		for(Statement aStmt:allStatements){
			ce.processStatementFile(aStmt);
		}
		
		
		
//		ce.waitToShutdown();
		
		/*try {
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
*/
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
			return StringUtils.endsWith(file.getName().toUpperCase(), ".XLS");
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
