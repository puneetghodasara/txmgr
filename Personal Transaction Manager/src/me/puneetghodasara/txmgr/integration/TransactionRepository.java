package me.puneetghodasara.txmgr.integration;

import java.util.List;

import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.Transaction;

public interface TransactionRepository {

	public void saveTransaction(Transaction transaction);

	public List<Transaction> getAllTransactionsOfAccount(Account account);

	void deleteTransaction(Transaction transaction);

	Integer isTransactionSaved(Transaction transaction);

	public Transaction getTransactionById(Integer txId);
	
}
