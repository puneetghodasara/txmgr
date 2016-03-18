package me.puneetghodasara.txmgr.ui.web;

import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.core.exception.CustomException;
import me.puneetghodasara.txmgr.core.exception.ExceptionKey;
import me.puneetghodasara.txmgr.core.manager.AccountManager;
import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.core.model.db.BankEnum;
import me.puneetghodasara.txmgr.ui.modal.UIAccount;

@Component
@Path("/account")
public class AccountEndPoint {

	@Resource
	private AccountManager accountManager;

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Account> getAllAccounts() {
		return accountManager.getAllAccounts();
	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Integer addAccount(UIAccount accountUI) {

		String name = accountUI.getName();
		String number = accountUI.getNumber();

		String bankStr = accountUI.getBank();
		BankEnum bank;
		try {
			bank = BankEnum.valueOf(bankStr);
		} catch (IllegalArgumentException e) {
			throw CustomException.getCMSException(ExceptionKey.INVALID_PARAM, "bank");

		}

		String accountTypeStr = accountUI.getAccountType();
		AccountTypeEnum accountType;
		try {
			accountType = AccountTypeEnum.valueOf(accountTypeStr);
		} catch (IllegalArgumentException e) {
			throw CustomException.getCMSException(ExceptionKey.INVALID_PARAM, "account_type");

		}

		String tag = accountUI.getTag();

		// Create
		Account accountBiz = accountManager.createAccount(name, number, bank, accountType, tag);

		return accountBiz.getId();
	}

}
