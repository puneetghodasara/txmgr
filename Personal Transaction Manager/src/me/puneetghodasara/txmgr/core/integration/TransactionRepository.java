package me.puneetghodasara.txmgr.core.integration;

import java.util.List;

import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.model.db.Transaction;

public interface TransactionRepository {

	public void saveTransaction(Transaction transaction);

	public List<Transaction> getAllTransactionsOfAccount(Account account);

	void deleteTransaction(Transaction transaction);

	Integer isTransactionSaved(Transaction transaction);

	public Transaction getTransactionById(Integer txId);
	
}
