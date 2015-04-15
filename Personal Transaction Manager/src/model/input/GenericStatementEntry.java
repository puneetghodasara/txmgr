package model.input;

public class GenericStatementEntry {

	public enum FieldMap{
		SERIAL_NUMBER("srNo"),
		DATE("date"),
		DESCRIPTION("description"),
		CREDIT_AMOUNT("credit"),
		DEBIT_AMOUNT("debit"),
		ABS_AMOUNT("absAmount");
		
		public String value;
		private FieldMap(String val) {
			this.value = val;
		}
		
	}
	
	private String srNo;
	private String date;
	private String description;

	/**
	 * Either statement can have Credit/Debit as separate field
	 */
	private String credit;
	private String debit;
	/**
	 * Or statement can have in common filed
	 */
	private String absAmount;
	public String getSrNo() {
		return srNo;
	}
	public void setSrNo(String srNo) {
		this.srNo = srNo;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getDebit() {
		return debit;
	}
	public void setDebit(String debit) {
		this.debit = debit;
	}
	public String getAbsAmount() {
		return absAmount;
	}
	public void setAbsAmount(String absAmount) {
		this.absAmount = absAmount;
	}
	
	
	
}
