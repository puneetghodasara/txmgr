package me.puneetghodasara.txmgr.model.parser;

import me.puneetghodasara.txmgr.model.db.Transaction;

public interface TransactionParser {

	public void parseTransaction(Transaction transaction);
	
}
