package me.puneetghodasara.txmgr.core.util;

import me.puneetghodasara.txmgr.core.exception.DuplicateException;
import me.puneetghodasara.txmgr.core.manager.AccountManager;
import me.puneetghodasara.txmgr.core.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.core.model.db.BankEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dbGen")
public class DefaultEntries {

	@Autowired
	private AccountManager accountManager;
	
	public void enter(){
		try {
			accountManager.createAccount("CITI-4386", "0", BankEnum.CITI_CC_VISA_REWARDS, AccountTypeEnum.CREDIT_CARD, "Dell Visa Rewards Credit Card");
		} catch (DuplicateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
