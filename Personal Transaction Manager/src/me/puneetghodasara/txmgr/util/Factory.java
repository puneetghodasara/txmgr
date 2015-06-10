package me.puneetghodasara.txmgr.util;

import org.apache.log4j.Logger;

import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.model.db.BankEnum;
import me.puneetghodasara.txmgr.model.parser.DateParser;
import me.puneetghodasara.txmgr.model.parser.StatementParser;
import me.puneetghodasara.txmgr.provider.citi.CITIBankHelper;
import me.puneetghodasara.txmgr.provider.icici.ICICIBankHelper;


public class Factory {

	private static final Logger logger = Logger.getLogger(Factory.class);
	
	public static StatementParser getStatementParser(Account account){
		
		logger.warn("In statemetn parser factory.");
		// ICICI Bank
		if(account.getBank().getEnumValue() == BankEnum.ICICI
			&& account.getAccountType().getEnumValue() == AccountTypeEnum.BANK_ACCOUNT){
			return new ICICIBankHelper();
		}
		
		logger.warn("Returning NULL statement parser.");
		return null;
	}
	
	public static DateParser getDateParser(Account account){
		return new CITIBankHelper();
	}
}
