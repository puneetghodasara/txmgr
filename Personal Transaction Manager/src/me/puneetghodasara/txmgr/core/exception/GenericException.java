package me.puneetghodasara.txmgr.core.exception;

import me.puneetghodasara.txmgr.core.exception.CustomException.ExceptionType;

public class GenericException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected ExceptionType eType;
	
	public GenericException(ExceptionType errorType) {
		this.eType = errorType;
	}

	@Override
	public String getMessage() {
		return eType.msg;
	}

}
