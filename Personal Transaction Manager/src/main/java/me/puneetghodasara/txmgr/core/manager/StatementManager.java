package me.puneetghodasara.txmgr.core.manager;

import me.puneetghodasara.txmgr.core.model.db.Statement;

public interface StatementManager {

	boolean isFileUploaded(String fileName);

	void markFileProcessed(Statement stmt);

	void saveStatement(Statement stmt);

}
