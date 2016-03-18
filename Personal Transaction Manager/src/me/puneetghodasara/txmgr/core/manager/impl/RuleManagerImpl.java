package me.puneetghodasara.txmgr.core.manager.impl;

import java.util.Optional;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.core.exception.CustomException;
import me.puneetghodasara.txmgr.core.integration.RuleRepository;
import me.puneetghodasara.txmgr.core.manager.RuleManager;
import me.puneetghodasara.txmgr.core.model.db.Rule;

@Component("ruleManager")
public class RuleManagerImpl implements RuleManager {

	private static final Logger logger = Logger.getLogger(RuleManagerImpl.class);

	@Resource
	private RuleRepository ruleRepository;

	@Override
	public boolean isRuleExist(Rule rule) {
		Optional<Rule> ruleBiz = ruleRepository.getRuleByRule(rule.getRule()).findAny();
		return ruleBiz.isPresent();
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
