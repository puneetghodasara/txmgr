package me.puneetghodasara.txmgr.core.model.db;

public enum TransactionCategory {
	/* Default */
	UNKNOWN,
	
	/* Expense Types */
	FOOD,
	GROCERY,
	MEDICINE,
	TRAVELLING,
	RECHARGE,
	SHOPPING,
	CHARGES,
	MISC,
	BROADBAND,
	FUEL_SERVICE,
	RENT_BROKERAGE,
	GROOMING,

	/* Transfer Internal */
	DEPOSIT,
	INTEREST_CHARGES,
	
	/* Transfer */
	WITHDRAW,
	ELECTRONIC,
	BILL_PAY,
	SALARY,
	INVEST

}
