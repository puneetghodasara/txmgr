package me.puneetghodasara.txmgr.core.model.db;

public enum TransactionCategory {
	/* Default */
	UNKNOWN,
	
	/* Expense Types */
	FOOD,
	GROCERY,
	TRAVELLING,
	RECHARGE,
	CHARGES,
	MISC,

	/* Transfer Internal */
	DEPOSIT,
	INTEREST_CHARGES,
	
	/* Transfer */
	WITHDRAW,
	ELECTRONIC,
	BILL_PAY,
	SALARY,

}
