package me.puneetghodasara.txmgr.ui.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import me.puneetghodasara.txmgr.core.exception.CustomException;
import me.puneetghodasara.txmgr.core.exception.ExceptionKey;
import me.puneetghodasara.txmgr.core.manager.AccountManager;
import me.puneetghodasara.txmgr.core.manager.StatementManager;
import me.puneetghodasara.txmgr.core.model.db.Account;
import me.puneetghodasara.txmgr.core.model.db.Statement;
import me.puneetghodasara.txmgr.core.processor.StatementProcessor;
import me.puneetghodasara.txmgr.core.util.ExcelToCSV;

@Component
@Transactional
@Path("/statement")
public class StatementEndPoint {

	@Resource
	private StatementProcessor statementProcessor;

	@Resource
	private AccountManager accountManager;

	@Resource
	private StatementManager statementManager;
	
	@Resource
	private TaskExecutor executor;

	@POST
	@Path("/upload")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Long uploadFile(@FormDataParam("file") InputStream fileStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("accountid") Integer id) {

		String fileName = fileDetail.getFileName();

		// Get Account ID
		Account account = accountManager.getAccountById(id);
		if (account == null)
			throw CustomException.getCMSException(ExceptionKey.INVALID_PARAM, "accountid");

//		if (statementManager.isFileUploaded(fileName)) {
//			throw CustomException.getCMSException(ExceptionKey.FILE_ALREADY_UPLOADED, fileName);
//		}

		byte[] content;
		try {
			// TODO encrypt and then store ;-)
			content = convertFile(fileStream, fileName);
//			content = IOUtils.toByteArray(fileStream);
		} catch (IOException e) {
			throw CustomException.getCMSException(e);
		}

		Statement stmt = new Statement();
		stmt.setFilename(fileName);
		stmt.setAccount(account);
		stmt.setContent(content);

		statementManager.saveStatement(stmt);

		// Call for a Process
		statementProcessor.setStatement(stmt);
		
		// Submit
		executor.execute(statementProcessor);

		return -1L;
	}

	
	private byte[] convertFile(InputStream fileStream, String name) throws IOException {

		String fileName = name;

		byte[] content;
			// TODO encrypt and then store ;-)
			
			File tempFile = File.createTempFile("PTM-I", fileName.substring(0, 1));
			Files.copy(fileStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			File tempOutFile = File.createTempFile("PTM-O", fileName.substring(0, 1));
			
			ExcelToCSV.convertToXls(tempFile, tempOutFile);
			
			content = FileUtils.readFileToByteArray(tempOutFile);
//			content = IOUtils.toByteArray(fileStream);

		return content;
	}

}
