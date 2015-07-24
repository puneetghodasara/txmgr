package me.puneetghodasara.txmgr.model.db;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * The persistent class for the transaction_detail database table.
 * 
 */
@Entity
@Table(name = "transaction_detail")
@NamedQuery(name = "TransactionDetail.findAll", query = "SELECT t FROM TransactionDetail t")
public class TransactionDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
//	@OneToOne
//	@PrimaryKeyJoinColumn
//	private Transaction transaction;

	private String category;

	private String tag;

	private String merchant;

	@Column(name = "via_card")
	private Boolean viaCard;

	private String way;

	@Transient
	private boolean isTransfer = false;

	@OneToOne
	@JoinColumn(name="target_account", referencedColumnName="id")
	private Account targetAccount;
	
	public Account getTargetAccount() {
		return targetAccount;
	}

	public void setTargetAccount(Account targetAccount) {
		this.targetAccount = targetAccount;
	}

	public TransactionDetail() {
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

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "TransactionDetail [category=" + category + ", tag=" + tag + ", merchant=" + merchant + ", targetAccount=" + targetAccount + "]";
	}



	
	
}