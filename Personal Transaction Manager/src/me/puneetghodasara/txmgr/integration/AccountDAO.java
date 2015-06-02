package me.puneetghodasara.txmgr.integration;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import me.puneetghodasara.txmgr.model.db.Account;

public class AccountDAO extends HibernateDaoSupport implements AccountRepository {

	@Override
	public void saveAccount(Account account) {
		this.getHibernateTemplate().save(account);
	}

}
