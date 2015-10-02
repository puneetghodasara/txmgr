package me.puneetghodasara.txmgr.core.engine;

import java.util.List;

import me.puneetghodasara.txmgr.core.integration.RuleRepository;
import me.puneetghodasara.txmgr.core.model.db.Rule;
import me.puneetghodasara.txmgr.core.parser.RuleParser;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ruleEngine")
public class RuleEngine {

	@Autowired
	private RuleParser ruleParser;

	@Autowired
	private RuleRepository ruleRepository;

	// Logger
	private static final Logger logger = Logger.getLogger(RuleEngine.class);

	public void loadAllRules() {
		List<Rule> rules = null;
		try {
			rules = ruleParser.getRules();
		} catch (Exception e) {
			logger.error("Error occurred while loading new rules." + e);
			return;
		}

		// SAVE
		rules.forEach(rule -> {
			if (!ruleRepository.isRuleExist(rule.getRule())) {
				logger.info("Saving new rule :" + rule);
				ruleRepository.saveRule(rule);
			}
		});
		
		logger.debug("exiting loadNewRules.");
		return;
	}

	public RuleParser getRuleParser() {
		return ruleParser;
	}

	public void setRuleParser(RuleParser ruleParser) {
		this.ruleParser = ruleParser;
	}

	public RuleRepository getRuleRepository() {
		return ruleRepository;
	}

	public void setRuleRepository(RuleRepository ruleRepository) {
		this.ruleRepository = ruleRepository;
	}
	
	
}
