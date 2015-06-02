package me.puneetghodasara.txmgr.model.db;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the transaction_detail database table.
 * 
 */
@Entity
@Table(name="transaction_detail")
@NamedQuery(name="TransactionDetail.findAll", query="SELECT t FROM TransactionDetail t")
public class TransactionDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;

	private String category;

	private String tag;

	@Column(name="via_card")
	private Boolean viaCard;

	private String way;

	//uni-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="target_account")
	private Account account;

	public TransactionDetail() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Boolean getViaCard() {
		return this.viaCard;
	}

	public void setViaCard(Boolean viaCard) {
		this.viaCard = viaCard;
	}

	public String getWay() {
		return this.way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}