package me.puneetghodasara.txmgr.core.manager.impl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.core.exception.DuplicateException;
import me.puneetghodasara.txmgr.core.integration.AccountRepository;
import me.puneetghodasara.txmgr.core.manager.AccountManager;
import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.core.model.db.BankEnum;

@Component(value="accountManager")
public class AccountManagerImpl implements AccountManager {

	private static final Logger logger = Logger.getLogger(AccountManagerImpl.class);
	
	@Autowired
	private AccountRepository accountRepository; 
	
	@Override
	public Account createAccount(String name, String number, BankEnum bank,
			AccountTypeEnum accType, String tag) throws DuplicateException{

		// Check
		if(getAccountByName(name)!=null)
			throw new DuplicateException(name);
		
		// Make an Account
		Account account = new Account();
		account.setName(name);
		account.setAccountNumber(number);
		account.setBank(bank.getBank());
		account.setAccountType(accType.getAccountType());
		account.setTag(tag);
		account.setOpenDate(new Date());

		logger.debug(account);
		
		// Persist
		accountRepository.saveAccount(account);
		
		return account;
	}

	@Override
	public void deleteAccount(Account citiCardAcc) {
		accountRepository.deleteAccount(citiCardAcc);
	}

	
	@Override
	public Account getAccountByName(String name) {
		return accountRepository.getAccountByName(name);
	}

	@Override
	public Account getAccountByNumber(String number) {
		return accountRepository.getAccountByNumber(number);
	}

	
	@Override
	public Account getAccountById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public AccountRepository getAccountRepository() {
		return accountRepository;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	/* GETTER - SETTER */
	
}
