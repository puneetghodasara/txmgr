package me.puneetghodasara.txmgr.core.exception;

import me.puneetghodasara.txmgr.core.exception.CustomException.ExceptionType;

public class StatementParseException extends GenericException {

	
	public StatementParseException(ExceptionType errorType) {
		super(errorType);
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
