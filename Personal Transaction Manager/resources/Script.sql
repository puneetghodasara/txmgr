--<ScriptOptions statementTerminator=";"/>

CREATE TABLE account_type (
	id INT NOT NULL,
	type VARCHAR(64),
	is_managed BIT,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE transaction (
	id INT NOT NULL,
	account_id INT,
	date DATETIME,
	description VARCHAR(1024),
	amount DECIMAL(10 , 0),
	credit_debit ENUM,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE account (
	id INT NOT NULL,
	name VARCHAR(128),
	bank_name VARCHAR(128),
	type INT,
	open_date DATETIME DEFAULT 'CURRENT_TIMESTAMP',
	tag VARCHAR(128),
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE transaction_detail (
	id INT NOT NULL,
	category VARCHAR(128),
	tag VARCHAR(128),
	target_account INT,
	way VARCHAR(128),
	via_card BIT,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE bank (
	id INT NOT NULL,
	bank_name VARCHAR(45),
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX esc_index ON transaction (description ASC);

CREATE INDEX fk_transaction_detail_account_idx ON transaction_detail (target_account ASC);

CREATE INDEX fk_transaction_accoun_idx ON transaction (account_id ASC);

