package me.puneetghodasara.txmgr.core.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Extending a generic {@link WebApplicationException} exception class used for
 * JAX-RS
 * 
 * @author I324801
 *
 */
public class CustomException extends WebApplicationException {

	private static final long serialVersionUID = 341058583571650888L;

	@Expose
	@SerializedName("errorkey")
	private String errorContract;
	@Expose
	@SerializedName("params")
	private String[] errorParams;

	private CustomException(Status status, String errorContract, String... errorParams) {
		super(status);
		this.errorContract = errorContract;
		this.errorParams = errorParams;
	}

	public static CustomException getCMSException(ExceptionKey eKey, String... params) {
		Status status = Status.NOT_ACCEPTABLE;
		String errorContract = eKey.name();
		String[] errorParams = params;

		return new CustomException(status, errorContract, errorParams);
	}

	public static CustomException getCMSException(Exception dae) {
		Status status = Status.INTERNAL_SERVER_ERROR;

		return new CustomException(status, ExceptionKey.DB_EXCEPTION.name(), dae.getMessage());
	}

	public String getErrorContract() {
		return errorContract;
	}

	public String[] getErrorParams() {
		return errorParams;
	}

}
