package me.puneetghodasara.txmgr.core.listener;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.core.manager.RuleManager;
import me.puneetghodasara.txmgr.core.parser.RuleParser;

@Component("ruleListener")
public class RuleListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = Logger.getLogger(RuleListener.class);

	@Resource
	private RuleParser ruleParser;

	@Resource
	private RuleManager ruleManager;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ruleManager.initializeRules();
	}

}
