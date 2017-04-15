package me.puneetghodasara.txmgr.core.model.db;

public enum BankEnum {

	ICICI_BANK(1,"ICICI Bank"),
	CITI_BANK(2,"Citi Bank"),
	NO_BANK(5,"No Bank");
	
	private Integer id;
	private String name;
	
	private BankEnum(Integer id, String type) {
		this.id = id;
		this.name = type;
	}
	
	
}
