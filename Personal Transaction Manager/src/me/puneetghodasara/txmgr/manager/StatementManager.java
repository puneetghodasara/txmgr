package me.puneetghodasara.txmgr.manager;

import me.puneetghodasara.txmgr.model.db.Account;

public interface StatementManager {

	boolean processStatement(Account account, String statementFileName) throws Exception;

}
