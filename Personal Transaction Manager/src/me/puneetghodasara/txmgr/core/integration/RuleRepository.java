package me.puneetghodasara.txmgr.core.integration;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import me.puneetghodasara.txmgr.core.model.db.Rule;

public interface RuleRepository extends JpaRepository<Rule, Integer> {

	public Stream<Rule> getRuleByRule(String rule);

}
