package me.puneetghodasara.txmgr.integration;

import java.util.Collection;

import me.puneetghodasara.txmgr.model.db.Rule;

public interface RuleRepository {

	public Collection<Rule> getAllRules();

	public boolean isRuleExist(String rule);

	public void saveRule(Rule rule);
	
}
