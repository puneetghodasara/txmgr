package me.puneetghodasara.txmgr.model.parser;

import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.model.db.BankEnum;


public class StatementParserFactory {

	public StatementParser getStatementParser(Account account){
		// ICICI Bank
		if(account.getBank().getEnumValue() == BankEnum.ICICI
			&& account.getAccountType().getEnumValue() == AccountTypeEnum.BANK_ACCOUNT){
			return new ICICIBankStatementParser();
		}
		return null;
	}
}
