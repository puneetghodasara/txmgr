package me.puneetghodasara.txmgr.core.integration;

import org.springframework.data.jpa.repository.JpaRepository;

import me.puneetghodasara.txmgr.core.model.db.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

	public Account getAccountByName(String name);

	public Account getAccountByNumber(String number);

}
