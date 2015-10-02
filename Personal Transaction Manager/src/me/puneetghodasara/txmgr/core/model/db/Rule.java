package me.puneetghodasara.txmgr.core.model.db;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the rule database table.
 * 
 */
@Entity
@Table(name="rule")
//@NamedQuery(name = "Rule.findAll", query = "SELECT r FROM Rule r")
public class Rule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private String category;
	@Column
	private String rule;
	@Column
	private String subcategory;
	@Column
	private String merchant;

	@Column(name = "account")
	private String targetAccount;

	@Column(name = "prop_ref")
	private String propRef;

	public Rule() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRule() {
		return this.rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getSubcategory() {
		return this.subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getTargetAccount() {
		return targetAccount;
	}

	public void setTargetAccount(String targetAccount) {
		this.targetAccount = targetAccount;
	}

	public String getPropRef() {
		return propRef;
	}

	public void setPropRef(String propRef) {
		this.propRef = propRef;
	}

	@Override
	public String toString() {
		return "Rule [id=" + id + ", category=" + category + ", rule=" + rule + ", merchant=" + merchant + ", propRef=" + propRef + "]";
	}

	
}