package model.input;

import model.db.Account;
import model.db.AccountTypeEnum;
import model.db.BankEnum;
import model.parser.ICICIBankStatementParser;


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
