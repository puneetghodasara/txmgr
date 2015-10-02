package me.puneetghodasara.txmgr.core.manager;

import me.puneetghodasara.txmgr.core.exception.DuplicateException;
import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.core.model.db.BankEnum;

public interface AccountManager {

	public Account getAccountByName(String name);

	public Account getAccountByNumber(String number);

	public Account getAccountById(String id);

	public Account createAccount(String name, String number, BankEnum bank, AccountTypeEnum accType, String tag) throws DuplicateException;

	public void deleteAccount(Account citiCardAcc);

}
