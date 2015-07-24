package me.puneetghodasara.txmgr.integration;

import java.util.Collection;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import me.puneetghodasara.txmgr.model.db.Account;

@Component(value="accountRepository")
public class AccountDAO extends HibernateDaoSupport implements AccountRepository {

	@Autowired
	public void init(SessionFactory sessionFactory){
		this.setSessionFactory(sessionFactory);
	}
	
	@Override
	public void saveAccount(Account account) {
		this.getHibernateTemplate().saveOrUpdate(account);
	}

	@Override
	public void deleteAccount(Account account) {
		this.getHibernateTemplate().delete(account);
	}

	@Override
	public Collection<Account> getAllAccounts() {
		return this.getHibernateTemplate().loadAll(Account.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Account getAccountByName(String name) {
		List<Account> accountList = (List<Account>) this.getHibernateTemplate().findByNamedParam(" FROM Account a WHERE a.name =:name", "name", name);
		return CollectionUtils.isEmpty(accountList)?null:accountList.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Account getAccountByNumber(String number) {
		List<Account> accountList = (List<Account>) this.getHibernateTemplate().findByNamedParam(" FROM Account a WHERE a.accountNumber =:number", "number", number);
		return CollectionUtils.isEmpty(accountList)?null:accountList.get(0);
	}

	@Override
	public void deleteAllAccounts() {
		this.getHibernateTemplate().deleteAll(getAllAccounts());
	}

}
