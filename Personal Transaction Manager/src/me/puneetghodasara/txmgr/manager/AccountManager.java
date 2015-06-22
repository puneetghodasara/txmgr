package me.puneetghodasara.txmgr.manager;

import me.puneetghodasara.txmgr.exception.DuplicateException;
import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.model.db.BankEnum;

public interface AccountManager {

	public Account getAccountByName(String name);
	
	public Account getAccountById(String id);

	public Account createAccount(String name, BankEnum bank, AccountTypeEnum accType, String tag) throws DuplicateException;

	public void deleteAccount(Account citiCardAcc);
	
}
