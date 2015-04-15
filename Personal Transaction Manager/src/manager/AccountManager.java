package manager;

import exception.DuplicateException;
import model.db.Account;
import model.db.AccountTypeEnum;
import model.db.BankEnum;

public interface AccountManager {

	public Account getAccountByName(String name);
	
	public Account getAccountById(String id);

	public Account createAccount(String name, BankEnum bank, AccountTypeEnum accType, String tag) throws DuplicateException;
	
}
