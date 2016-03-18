package me.puneetghodasara.txmgr.core.integration;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import me.puneetghodasara.txmgr.core.model.db.Statement;

public interface StatementRepository extends JpaRepository<Statement, Integer> {

	public Stream<Statement> getStatementByFilename(String name);

}
