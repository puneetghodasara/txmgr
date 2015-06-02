package me.puneetghodasara.txmgr.model.input;

import java.util.List;

import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.Transaction;

public interface StatementManager {

	List<Transaction> getStatementEntries(Account account, String statementFileName) throws Exception;

}
