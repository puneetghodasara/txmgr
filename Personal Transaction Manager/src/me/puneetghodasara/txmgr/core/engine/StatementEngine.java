package me.puneetghodasara.txmgr.core.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.puneetghodasara.txmgr.core.exception.CustomException;
import me.puneetghodasara.txmgr.core.exception.CustomException.ExceptionType;
import me.puneetghodasara.txmgr.core.model.db.Statement;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(value = "statementEngine")
public class StatementEngine {

	// Logger
	private static final Logger logger = Logger.getLogger(StatementEngine.class);

	@Value("${file.root}")
	private String rootDirectory;

	public List<Statement> getAllStatements() throws CustomException {
		logger.debug("ROOT Folder is " + rootDirectory);
		File rootFolder = new File(rootDirectory);
		if (!rootFolder.exists()) {
			logger.error("ROOT Folder is not available.");
			throw new CustomException(ExceptionType.NO_FOLDER_AVBL);
		}

		List<Statement> stmtList = new ArrayList<Statement>();

		File[] bankName = rootFolder.listFiles();
		// Arrays.asList(bankName).parallelStream().filter(b ->
		// b.isDirectory()).forEach(aBank -> {
		// File[] bankTypeName = aBank.listFiles();
		// Arrays.asList(bankTypeName).parallelStream().filter(b ->
		// b.isDirectory()).forEach(aType -> {
		// File[] stmtFiles = aType.listFiles((file) -> {
		// return file.isFile() &&
		// StringUtils.endsWith(file.getName().toUpperCase(), ".XLS");
		// });
		//
		// Arrays.asList(stmtFiles).parallelStream().forEach(stmtFile -> {
		// Statement stmt = new Statement();
		// stmt.setFilename(stmtFile.getAbsoluteFile().toPath().toString());
		// stmt.setAccountname(aBank.getName());
		// stmt.setAccounttype(aType.getName());
		//
		// stmtList.add(stmt);
		//
		// });
		// });
		// });

		for (File aBank : bankName) {
			if (!aBank.isDirectory())
				continue;
			for (File aType : aBank.listFiles()) {
				if (!aType.isDirectory())
					continue;
				for (File accName : aType.listFiles()) {
					if (!accName.isDirectory())
						continue;

					for (File stmtFile : accName.listFiles()) {
						if (stmtFile.isFile() && StringUtils.endsWith(stmtFile.getAbsolutePath().toUpperCase().toString(), "XLS")) {

							Statement stmt = new Statement();
							stmt.setFilename(stmtFile.getAbsoluteFile().toPath().toString());
							stmt.setAccountname(accName.getName());
							stmt.setAccounttype(aType.getName());
							stmt.setBankname(aBank.getName());
							stmtList.add(stmt);
						}
					}
				}
			}
		}

		logger.info(stmtList);

		logger.debug("Got " + stmtList.size() + " statements to parse.");
		return stmtList;

	}

}
