package me.puneetghodasara.txmgr.exception;

public class CustomException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum ExceptionType {
		INVALID_STATEMENT_PARSER("No valid statement parser found."), CSV_PARSE_EXCEPTION("CSV parser not found."), INVALID_ENTRY_PARSER(
				"Entry parser not found."), CREDIT_DEBIT_PARSE_ERROR("Neither Credit nor Debit is a number."), DATE_PARSE_ERROR("Date is not in a valid format."), INVALID_DATE_PARSER("No valid date parser found."), NO_FOLDER_AVBL("NO_FOLDER_AVBL"), RULE_FILE_NOT_FOUND("RULE_FILE_NOT_FOUND");

		String msg;

		private ExceptionType(String msg) {
			this.msg = msg;
		}

	}

	public CustomException(ExceptionType type) {
		super(type.msg);
	}
}
