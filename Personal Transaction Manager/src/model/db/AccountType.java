package model.db;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the account_type database table.
 * 
 */
@Entity
@Table(name = "account_type")
@NamedQuery(name = "AccountType.findAll", query = "SELECT a FROM AccountType a")
public class AccountType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ACCOUNT_TYPE_ID_GENERATOR", sequenceName = "SEQ_TRANSACTION_MANAGER")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_TYPE_ID_GENERATOR")
	private Integer id;

	@Column(name = "type")
	private String type;

	@Column(name = "is_managed")
	private Boolean isManaged;

	public AccountType() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsManaged() {
		return this.isManaged;
	}

	public void setIsManaged(Boolean isManaged) {
		this.isManaged = isManaged;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AccountTypeEnum getEnumValue(){
		for (AccountTypeEnum typeEnum : AccountTypeEnum.values()){
			if(typeEnum.id == this.id)
				return typeEnum;
		}
		return null;
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
		AccountType other = (AccountType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}