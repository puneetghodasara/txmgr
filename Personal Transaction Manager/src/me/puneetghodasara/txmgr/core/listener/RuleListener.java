package me.puneetghodasara.txmgr.core.listener;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.core.manager.RuleManager;
import me.puneetghodasara.txmgr.core.model.db.Rule;
import me.puneetghodasara.txmgr.core.parser.RuleParser;

@Component("ruleListener")
//@WebListener
public class RuleListener implements InitializingBean, ApplicationListener<ContextStartedEvent> {

	private static final Logger logger = Logger.getLogger(RuleListener.class);

	@Resource
	private RuleParser ruleParser;

	@Resource
	private RuleManager ruleManager;

	@Override
	public void onApplicationEvent(ContextStartedEvent event) {
		List<Rule> ruleList;
		try {
			ruleList = ruleParser.getRules();
		} catch (Exception e) {
			logger.error(e);
			return;
		}

		// Add all rules
		int newRuleCount = 0;
		for (Rule rule : ruleList) {
			if (!ruleManager.isRuleExist(rule)) {
				newRuleCount++;
				ruleManager.addRule(rule);
			}
		}

		logger.info(newRuleCount + " new Rules are added.");
		Long totalRules = ruleManager.getRuleCount();
		logger.info("Total rules are :" + totalRules);

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		onApplicationEvent(null);
	}

}
