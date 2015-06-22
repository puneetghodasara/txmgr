package me.puneetghodasara.txmgr.provider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.puneetghodasara.txmgr.model.db.AccountTypeEnum;
import me.puneetghodasara.txmgr.model.db.BankEnum;

@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionHelper {
	public abstract BankEnum bank();
	public abstract AccountTypeEnum accountType();
}
