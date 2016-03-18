package main;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import me.puneetghodasara.txmgr.core.engine.InputEngine;
import me.puneetghodasara.txmgr.core.engine.StatementEngine;
import me.puneetghodasara.txmgr.core.exception.CustomException;
import me.puneetghodasara.txmgr.core.integration.RuleRepository;
import me.puneetghodasara.txmgr.core.model.db.Rule;
import me.puneetghodasara.txmgr.core.model.db.Statement;
import me.puneetghodasara.txmgr.core.parser.RuleParser;

public class Main {

	private static final Logger logger = Logger.getLogger(Main.class);

	public static ApplicationContext appContext = new ClassPathXmlApplicationContext("spring.xml");

	public static void main(String[] args) {

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
			if (!ruleRepository.getRuleByRule(rule.getRule()).findAny().isPresent()) {
				logger.info("Saving New Rule :" + rule);
				ruleRepository.save(rule);
			}
		});

//		DefaultEntries de = (DefaultEntries) appContext.getBean("dbGen");
//		de.enter();

		// Get Statements
		StatementEngine se = (StatementEngine) appContext.getBean("statementEngine");
		List<Statement> allStatements = null;
		try {
			allStatements = se.getAllStatements();
		} catch (CustomException e) {
			e.printStackTrace();
		}
		logger.info("Got Statements");

		// CoreEngine ce = (CoreEngine)
		// appContext.getBean("statementProcessor");
		InputEngine ce = (InputEngine) appContext.getBean("inputEngine");

		for (Statement aStmt : allStatements) {
			ce.processStatementFile(aStmt);
		}

		// ce.waitToShutdown();

		/*
		 * try { getAllStatements().entrySet().stream().forEach(entry -> {
		 * System.out.println("Account :" + entry.getKey() + " - " +
		 * entry.getValue()); entry.getValue().parallelStream().forEach(stmt ->
		 * { try { statementManager.processStatement(entry.getKey(), stmt); }
		 * catch (Exception e) { logger.error(
		 * "Exception in processing statement entries : " + e.getMessage());
		 * logger.error(e); } }); }); } catch (CustomException e) {
		 * logger.error("Exception in getting statement entries : " +
		 * e.getMessage()); }
		 */
		logger.info("End");
	}

}
