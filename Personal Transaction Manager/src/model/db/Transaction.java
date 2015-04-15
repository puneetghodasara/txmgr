package model.db;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the transaction database table.
 * 
 */
@Entity
@NamedQuery(name = "Transaction.findAll", query = "SELECT t FROM Transaction t")
public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "TRANSACTION_ID_GENERATOR", sequenceName = "SEQ_TRANSACTION_MANAGER")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSACTION_ID_GENERATOR")
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