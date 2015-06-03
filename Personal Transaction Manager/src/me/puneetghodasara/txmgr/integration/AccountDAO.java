package me.puneetghodasara.txmgr.integration;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.model.db.Account;

@Component(value="accountRepository")
public class AccountDAO extends HibernateDaoSupport implements AccountRepository {

	@Autowired
	public void init(SessionFactory sessionFactory){
		this.setSessionFactory(sessionFactory);
	}
	
	@Override
	public void saveAccount(Account account) {
		this.getHibernateTemplate().save(account);
	}

}
