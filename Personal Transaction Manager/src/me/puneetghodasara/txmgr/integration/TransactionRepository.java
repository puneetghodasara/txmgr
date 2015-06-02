package me.puneetghodasara.txmgr.integration;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import me.puneetghodasara.txmgr.model.db.Transaction;

public interface TransactionRepository {

	public void saveTransaction(Transaction transaction);

	public List<Transaction> getAllTransactions();
	
}
