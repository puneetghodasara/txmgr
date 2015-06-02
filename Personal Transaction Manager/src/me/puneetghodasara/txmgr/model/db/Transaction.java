package me.puneetghodasara.txmgr.model.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the transaction database table.
 * 
 */
@Entity
@NamedQuery(name = "Transaction.findAll", query = "SELECT t FROM Transaction t")
public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "credit_debit")
	@Enumerated(EnumType.STRING)
	private CreditDebitEnum creditDebit;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name = "description")
	private String description;

	// bi-directional many-to-one association to Account
	@ManyToOne
	private Account account;

	// uni-directional one-to-one association to TransactionDetail
	@OneToOne
	@JoinColumn(name = "id")
	private TransactionDetail transactionDetail;

	public Transaction() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public CreditDebitEnum getCreditDebit() {
		return this.creditDebit;
	}

	public void setCreditDebit(CreditDebitEnum creditDebit) {
		this.creditDebit = creditDebit;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public TransactionDetail getTransactionDetail() {
		return this.transactionDetail;
	}

	public void setTransactionDetail(TransactionDetail transactionDetail) {
		this.transactionDetail = transactionDetail;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", amount=" + amount + ", creditDebit=" + creditDebit + ", date=" + date + ", description=" + description + "]";
	}

}