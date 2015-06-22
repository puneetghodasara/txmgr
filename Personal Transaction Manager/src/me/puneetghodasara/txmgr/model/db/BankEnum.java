package me.puneetghodasara.txmgr.model.db;

public enum BankEnum {

	ICICI_BANK(1,"ICICI Bank Saving Account"),
	CITI_CC_VISA_REWARDS(2,"Citi Visa Rewards"),
	CITI_CC_MC_FUEL(3,"Citi Master Card Indian Oil"),
	CITI_BANK(4,"Citi Bank Saving Account");
	
	public Integer id;
	private String name;
	
	private BankEnum(Integer id, String type) {
		this.id = id;
		this.name = type;
	}
	
	
	public Bank getBank(){
		Bank bank = new Bank();
		bank.setId(this.id);
		bank.setBankName(this.name);
		return bank;
	}
	
}
