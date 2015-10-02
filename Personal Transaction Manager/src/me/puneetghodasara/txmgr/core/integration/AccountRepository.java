package me.puneetghodasara.txmgr.core.integration;

import java.util.Collection;

import me.puneetghodasara.txmgr.core.model.db.Account;

public interface AccountRepository {

	
	public void saveAccount(Account account);
	
	public Collection<Account> getAllAccounts();
	
	public Account getAccountByName(String name);
	
	public Account getAccountByNumber(String number);

	public void deleteAccount(Account account);
	
	public void deleteAllAccounts();
}
