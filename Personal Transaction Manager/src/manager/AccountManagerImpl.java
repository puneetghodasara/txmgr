package manager;

import integration.AccountRepository;

import java.util.Date;

import exception.DuplicateException;
import model.db.Account;
import model.db.AccountTypeEnum;
import model.db.BankEnum;

public class AccountManagerImpl implements AccountManager {

	private AccountRepository accountRepository; 
	
	@Override
	public Account createAccount(String name, BankEnum bank,
			AccountTypeEnum accType, String tag) throws DuplicateException{

		// Check
		if(getAccountByName(name)!=null)
			throw new DuplicateException(name);
		
		// Make an Account
		Account account = new Account();
		account.setName(name);
		account.setBank(bank.getBank());
		account.setAccountType(accType.getAccountType());
		account.setTag(tag);
		account.setOpenDate(new Date());
		
		// TODO Persist the account
		accountRepository.saveAccount(account);
		
		return account;
	}

	@Override
	public Account getAccountByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account getAccountById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
