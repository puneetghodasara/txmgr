package me.puneetghodasara.txmgr.core.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the rule database table.
 * 
 */
@Entity
@Table(name = "rule")
// @NamedQuery(name = "Rule.findAll", query = "SELECT r FROM Rule r")
public class Rule implements Serializable {

	private static final long serialVersionUID = -7093062700582172674L;

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

	@Column
	private String isTransfer;
	
	@Column
	private String targetAccType;
	
	@Column(name = "prop_ref")
	private String propRef;


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

	public String getPropRef() {
		return propRef;
	}

	public void setPropRef(String propRef) {
		this.propRef = propRef;
	}

	public boolean isTransfer() {
		return isTransfer.equalsIgnoreCase("TRUE");
	}

	public String getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(String isTransfer) {
		this.isTransfer = isTransfer;//?("TRUE"):("FALSE");
	}

	public String getTargetAccType() {
		return targetAccType;
	}

	public void setTargetAccType(String targetAccType) {
		this.targetAccType = targetAccType;
	}

	@Override
	public String toString() {
		return "Rule [id=" + id + ", category=" + category + ", rule=" + rule + ", subcategory=" + subcategory
				+ ", merchant=" + merchant + ", isTransfer=" + isTransfer + ", propRef=" + propRef + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
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
		Rule other = (Rule) obj;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		return true;
	}

}