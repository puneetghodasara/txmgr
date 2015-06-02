package me.puneetghodasara.txmgr.model.db;

public enum AccountTypeEnum {

	
	MAIN_ACCOUNT(1,"Main Account",true),
	DEBIT_CARD(2,"Debit Card",true),
	CREDIT_CARD(3,"Credit Card",true),
	FD_ACCOUNT(4,"FD Account",false),
	SELF(5,"Self",false),
	SOURCE_ACCOUNT(6,"Source Account",false),
	BANK_ACCOUNT(7,"Bank Account",false),
	SINK_ACCOUNT(8,"Sink Account",false);
	
	public Integer id;
	private String type;
	private Boolean isManaged;
	
	private AccountTypeEnum(Integer id, String type, Boolean isManaged) {
		this.id = id;
		this.type = type;
		this.isManaged = isManaged;
	}
	
	
	public AccountType getAccountType(){
		AccountType accountType = new AccountType();
		accountType.setId(id);
		accountType.setType(type);
		accountType.setIsManaged(isManaged);
		return accountType;
	}
	
	
}
