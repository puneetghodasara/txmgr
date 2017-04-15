package me.puneetghodasara.txmgr.core.parser;

import java.util.List;

import me.puneetghodasara.txmgr.core.model.db.Rule;

/**
 * This is to get rules.
 * 
 * @author Punit_Ghodasara
 *
 */
public interface RuleParser {

	public List<Rule> getRules() throws Exception;

}
