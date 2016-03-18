package me.puneetghodasara.txmgr.core.manager.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.core.exception.CustomException;
import me.puneetghodasara.txmgr.core.exception.ExceptionKey;
import me.puneetghodasara.txmgr.core.integration.AccountRepository;
import me.puneetghodasara.txmgr.core.manager.AccountManager;
import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.core.model.db.BankEnum;

@Component(value = "accountManager")
public class AccountManagerImpl implements AccountManager {

	private static final Logger logger = Logger.getLogger(AccountManagerImpl.class);

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Account createAccount(String name, String number, BankEnum bank, AccountTypeEnum accType, String tag)
			throws CustomException {

		// Check
		if (getAccountByNumber(number) != null)
			throw CustomException.getCMSException(ExceptionKey.DUPLICATE_ACCOUNT_NAME, name);

		// Make an Account
		Account account = new Account();
		account.setName(name);
		account.setNumber(number);
		account.setBank(bank);
		account.setAccountType(accType);
		account.setTag(tag);
		account.setOpenDate(new Date());

		logger.debug(account);

		// Persist
		try {
			account = accountRepository.save(account);
		} catch (DataAccessException e) {
			throw CustomException.getCMSException(e);
		}

		return account;
	}

	@Override
	public List<Account> getAllAccounts() {
		return accountRepository.findAll();
	}

	@Override
	public void deleteAccount(Account citiCardAcc) {
		accountRepository.delete(citiCardAcc);
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
	public Account getAccountById(Integer id) {
		return accountRepository.findOne(id);
	}

	public AccountRepository getAccountRepository() {
		return accountRepository;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	/* GETTER - SETTER */

}
