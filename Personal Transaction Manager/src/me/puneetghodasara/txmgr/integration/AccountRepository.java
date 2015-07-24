package me.puneetghodasara.txmgr.integration;

import java.util.Collection;

import me.puneetghodasara.txmgr.model.db.Account;

public interface AccountRepository {

	
	public void saveAccount(Account account);
	
	public Collection<Account> getAllAccounts();
	
	public Account getAccountByName(String name);
	
	public Account getAccountByNumber(String number);

	public void deleteAccount(Account account);
	
	public void deleteAllAccounts();
}
