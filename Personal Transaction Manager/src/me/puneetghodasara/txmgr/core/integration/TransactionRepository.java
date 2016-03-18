package me.puneetghodasara.txmgr.core.integration;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import me.puneetghodasara.txmgr.core.model.db.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	@Query(name = "Transaction.isSaved")
	Integer isTransactionSaved(@Param("id") Integer accountid, @Param("description") String description,
			@Param("amount") Double amount, @Param("date") Date date);
}
