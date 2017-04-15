package me.puneetghodasara.txmgr.core.parser;

import me.puneetghodasara.txmgr.core.model.db.Transaction;
import me.puneetghodasara.txmgr.core.model.db.TransactionDetail;

public interface TransactionParser {

//	public void parseTransaction(Account account, Transaction tx);
	
	public TransactionDetail parseTransaction(Transaction tx);
}
