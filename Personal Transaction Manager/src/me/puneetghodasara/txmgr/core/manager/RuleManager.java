package me.puneetghodasara.txmgr.core.manager;

import me.puneetghodasara.txmgr.core.model.db.Rule;

public interface RuleManager {

	
	public boolean isRuleExist(Rule rule);

	public Integer addRule(Rule rule);

	public Long getRuleCount();

	void initializeRules();
}
