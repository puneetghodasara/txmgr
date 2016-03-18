package me.puneetghodasara.txmgr.core.parser;

import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.model.db.Transaction;
import me.puneetghodasara.txmgr.core.model.input.GenericStatementEntry;

/**
 * This parses generic statements to transactions.
 * 
 * @author Punit_Ghodasara
 *
 */
public interface StatementParser {

	public void setDateParser(DateParser dateParser);

	public Transaction getTransactionEntry(GenericStatementEntry statementEntry, Account account);
}
