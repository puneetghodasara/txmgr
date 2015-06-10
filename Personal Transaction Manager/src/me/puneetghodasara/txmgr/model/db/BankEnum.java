package me.puneetghodasara.txmgr.model.db;

public enum BankEnum {

	ICICI(1,"ICICI");
	
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
