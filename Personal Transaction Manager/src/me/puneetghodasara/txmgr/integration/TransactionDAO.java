package me.puneetghodasara.txmgr.integration;

import java.util.List;

import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.Transaction;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component(value = "transactionRepository")
public class TransactionDAO extends HibernateDaoSupport implements TransactionRepository {

	@Autowired
	public void init(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	@Override
	public void saveTransaction(Transaction transaction) {
		this.getHibernateTemplate().saveOrUpdate(transaction);
	}

	@Override
	public void deleteTransaction(Transaction transaction) {
		this.getHibernateTemplate().delete(transaction);
	}

	@Override
	public Integer isTransactionSaved(Transaction transaction) {
		@SuppressWarnings("rawtypes")
		List txList = this.getHibernateTemplate().findByNamedParam(
				" FROM Transaction t WHERE t.account.id = :id" + " AND t.amount = :amount " + " AND t.date = :date " + " AND t.description = :description ",
				new String[] { "id", "amount", "date", "description" },
				new Object[] { transaction.getAccount().getId(), transaction.getAmount(), transaction.getDate(), transaction.getDescription() });
		if (!CollectionUtils.isEmpty(txList))
			return ((Transaction)txList.get(0)).getId();
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> getAllTransactionsOfAccount(Account account) {
		return (List<Transaction>) this.getHibernateTemplate().findByNamedParam(" FROM Transaction t WHERE t.account.id = :id", "id", account.getId());
	}

	@Override
	public Transaction getTransactionById(Integer txId) {
		List<?> txList = this.getHibernateTemplate().findByNamedParam(" FROM Transaction t WHERE t.id = :id", "id", txId);
		if (!CollectionUtils.isEmpty(txList))
			return ((Transaction)txList.get(0));
		return null;

	}

}
