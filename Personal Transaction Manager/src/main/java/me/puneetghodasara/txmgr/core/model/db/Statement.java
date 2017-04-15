package me.puneetghodasara.txmgr.core.model.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

@Entity
@NamedQuery(name = "Statement.findAll", query = "SELECT s FROM Statement s")
public class Statement implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@OneToOne
	@JoinColumn
	private Account account;

	@Column
	private String filename;

	@Column
	private Date uploadDate;

	@Column
	private Date processDate;

	@Column(length = 1024 * 1024)
	private byte[] content;

	@PrePersist
	private void setReadDate() {
		uploadDate = new Date();
	}

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Date getReadDate() {
		return uploadDate;
	}

	public void setReadDate(Date readDate) {
		this.uploadDate = readDate;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate() {
		this.processDate = new Date();
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Statement [id=" + id + ", account=" + account.getName() + ", filename=" + filename + ", readDate="
				+ uploadDate + "]";
	}

}
