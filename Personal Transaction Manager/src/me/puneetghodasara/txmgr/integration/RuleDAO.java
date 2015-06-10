package me.puneetghodasara.txmgr.integration;

import java.util.Collection;
import java.util.List;

import me.puneetghodasara.txmgr.model.db.Rule;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component(value="ruleRepository")
public class RuleDAO extends HibernateDaoSupport implements RuleRepository {

	@Autowired
	public void init(SessionFactory sessionFactory){
		this.setSessionFactory(sessionFactory);
	}

	@Override
	public Collection<Rule> getAllRules() {
		return this.getHibernateTemplate().loadAll(Rule.class);
	}

	@Override
	public boolean isRuleExist(String rule) {
		List<?> ruleList = this.getHibernateTemplate().findByNamedParam("FROM Rule r WHERE r.rule = :rule", "rule", rule);
		if(!CollectionUtils.isEmpty(ruleList))
			return true;
		return false;
	}

	@Override
	public void saveRule(Rule rule) {
		this.getHibernateTemplate().saveOrUpdate(rule);
	}
	
	
}
