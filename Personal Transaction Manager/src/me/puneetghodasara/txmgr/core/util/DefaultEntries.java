package me.puneetghodasara.txmgr.core.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.core.exception.CustomException;
import me.puneetghodasara.txmgr.core.manager.AccountManager;
import me.puneetghodasara.txmgr.core.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.core.model.db.BankEnum;

@Component("dbGen")
public class DefaultEntries implements InitializingBean {

	@Autowired
	private AccountManager accountManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			accountManager.createAccount("CITI-4386", "xxxx-4386", BankEnum.CITI_BANK, AccountTypeEnum.MAIN_ACCOUNT,
					"Dell Visa Rewards Credit Card");
		} catch (CustomException e) {
			e.printStackTrace();
		}
	}

}
