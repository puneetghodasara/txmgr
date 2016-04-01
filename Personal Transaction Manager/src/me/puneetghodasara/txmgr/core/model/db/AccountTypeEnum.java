package me.puneetghodasara.txmgr.core.model.db;

public enum AccountTypeEnum {

	MAIN_ACCOUNT(1, "Main Account", true), 
	DEBIT_CARD(2, "Debit Card", true), 
	CREDIT_CARD(3, "Credit Card", true), 
	FD_ACCOUNT(4, "FD Account", false),
	MF_ACCOUNT(7, "MF Account", false),
	SELF(5, "Self", false), 
	SOURCE_ACCOUNT(6, "Source Account", false), 
	SINK_ACCOUNT(8, "Sink Account", false);

	private Integer id;
	private String type;
	private Boolean isManaged;

	private AccountTypeEnum(Integer id, String type, Boolean isManaged) {
		this.id = id;
		this.type = type;
		this.isManaged = isManaged;
	}

	public Integer getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public Boolean getIsManaged() {
		return isManaged;
	}

}
