package me.puneetghodasara.txmgr.core.parser;

import java.util.List;

import me.puneetghodasara.txmgr.core.model.input.GenericStatementEntry;

/**
 * This interface provides a way to parse different statements records to list
 * of GenericStatementEntries
 * 
 * 
 * @author Punit_Ghodasara
 *
 */
public interface RecordParser {

	public List<GenericStatementEntry> parseStatementFile(String stmtFile) throws Exception;
}
