package me.puneetghodasara.txmgr.core.model.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the account database table.
 * 
 */
@Entity
@NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "account_no")
	private String number;

	@Column(name = "bank_name")
	@Enumerated(EnumType.STRING)
	private BankEnum bank;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private AccountTypeEnum accountType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "open_date")
	private Date openDate;

	@Column(name = "tag")
	private String tag;

//	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
//	private List<Transaction> transactions = new ArrayList<>();


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BankEnum getBank() {
		return this.bank;
	}

	public void setBank(BankEnum bank) {
		this.bank = bank;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getOpenDate() {
		return this.openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public AccountTypeEnum getAccountType() {
		return this.accountType;
	}

	public void setAccountType(AccountTypeEnum accountType) {
		this.accountType = accountType;
	}


	@Override
	public String toString() {
		return "Account [id=" + id + ", name=" + name + ", bank=" + bank + ", accountType=" + accountType + ", tag="
				+ tag + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}