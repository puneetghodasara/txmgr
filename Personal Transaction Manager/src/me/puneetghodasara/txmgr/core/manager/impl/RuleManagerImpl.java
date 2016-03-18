package me.puneetghodasara.txmgr.core.manager.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.core.exception.CustomException;
import me.puneetghodasara.txmgr.core.integration.RuleRepository;
import me.puneetghodasara.txmgr.core.manager.RuleManager;
import me.puneetghodasara.txmgr.core.model.db.Rule;
import me.puneetghodasara.txmgr.core.parser.RuleParser;

@Component("ruleManager")
public class RuleManagerImpl implements RuleManager {

	private static final Logger logger = Logger.getLogger(RuleManagerImpl.class);

	@Resource
	private RuleRepository ruleRepository;
	
	@Resource
	private RuleParser ruleParser;

	@Override
	public boolean isRuleExist(Rule rule) {
		Optional<Rule> ruleBiz = ruleRepository.getRuleByRule(rule.getRule()).findAny();
		return  ruleBiz.isPresent();
	}

	@Override
	@Transactional
	public void initializeRules(){

		List<Rule> ruleList;
		try {
			ruleList = ruleParser.getRules();
		} catch (Exception e) {
			logger.error(e);
			return;
		}

		// Add all rules
		int newRuleCount = 0;
		try {
			for (Rule rule : ruleList) {
				System.out.println(rule);
				if (!this.isRuleExist(rule)) {
					newRuleCount++;
					this.addRule(rule);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info(newRuleCount + " new Rules are added.");
		Long totalRules = this.getRuleCount();
		logger.info("Total rules are :" + totalRules);
	

	}
	
	@Override
	public Integer addRule(Rule rule) {

		Rule ruleBiz;
		try {
			ruleBiz = ruleRepository.save(rule);
		} catch (Exception e) {
			logger.error("Saving Rule :" + e);
			throw CustomException.getCMSException(e);
		}
		return ruleBiz.getId();

	}

	@Override
	public Long getRuleCount(){
		return ruleRepository.count();
	}
	
}
