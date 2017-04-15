package me.puneetghodasara.txmgr.core.manager.impl;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.core.integration.StatementRepository;
import me.puneetghodasara.txmgr.core.manager.StatementManager;
import me.puneetghodasara.txmgr.core.model.db.Statement;

@Component("statementManager")
public class StatementManagerImpl implements StatementManager {

	@Resource
	private StatementRepository statementRepository;
	
	@Override
	public boolean isFileUploaded(String fileName) {
		Optional<Statement> stmt = statementRepository.getStatementByFilename(fileName).findAny();
		return stmt.isPresent();
	}

	@Override
	public void saveStatement(Statement stmt) {
		statementRepository.save(stmt);
		
	}

	@Override
	public void markFileProcessed(Statement stmt) {
		stmt.setProcessDate();
		statementRepository.save(stmt);
	}

}
