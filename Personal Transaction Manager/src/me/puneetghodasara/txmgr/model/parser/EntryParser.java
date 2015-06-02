package me.puneetghodasara.txmgr.model.parser;

import java.util.List;

import me.puneetghodasara.txmgr.exception.MessageException;
import me.puneetghodasara.txmgr.model.db.Account;
import me.puneetghodasara.txmgr.model.db.Transaction;
import me.puneetghodasara.txmgr.model.input.GenericStatementEntry;

public interface EntryParser {

	List<Transaction> convertEntries(Account account, List<GenericStatementEntry> statementEntryList) throws MessageException;

}
