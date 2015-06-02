package me.puneetghodasara.txmgr.integration;

import java.util.List;

import me.puneetghodasara.txmgr.model.db.Transaction;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

@Component(value="transactionRepository")
public class TransactionDAO extends HibernateDaoSupport implements TransactionRepository {

	@Autowired
	public void init(SessionFactory sessionFactory){
		this.setSessionFactory(sessionFactory);
	}
	
	@Override
	public void saveTransaction(Transaction transaction) {
		this.getHibernateTemplate().save(transaction);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> getAllTransactions() {
		return (List<Transaction>) this.getHibernateTemplate().find(" FROM Transaction t ");
	}
	

}
