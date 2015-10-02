package me.puneetghodasara.txmgr.core.parser;

import java.util.Date;

/**
 * This interface provides a way on how to parse date object from
 * GenericStatementEntry to TransactionEntry
 * 
 * This MUST be implemented by provider, as different provider have different
 * way to give dates.
 * 
 * @author Punit_Ghodasara
 *
 */
public interface DateParser {

	/**
	 * How @param dateString represents the @return date
	 */
	public Date getDate(String dateString);

}
