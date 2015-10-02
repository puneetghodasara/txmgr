package me.puneetghodasara.txmgr.core.engine;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import me.puneetghodasara.txmgr.core.model.db.Statement;
import me.puneetghodasara.txmgr.core.util.ExcelToCSV;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component("statementProcessor")
public class CoreEngine {

	private static Logger logger = Logger.getLogger(CoreEngine.class);

	@Value("${stmt.process.thread.count:3}")
	private static int PROCESS_THREAD_CNT;

	@Value("${stmt.convert.thread.count:3}")
	private static int CONVERT_THREAD_CNT;

	private static ExecutorService service = Executors.newFixedThreadPool(PROCESS_THREAD_CNT);
	private static ExecutorService convertService = Executors.newFixedThreadPool(CONVERT_THREAD_CNT);

	@Autowired
	private InputEngine inputEngine;

	public synchronized void processStatement(Statement stmt) {
		service.submit(() -> {
			File ifile = new File(stmt.getFilename());
			File pfile = new File(stmt.getFilename()).getParentFile();
			File ofile = new File(pfile.getAbsolutePath() + ifile.getName() + ".csv");
			try {
				ExcelToCSV.convertToXls(ifile, ofile);
			} catch (Exception e) {
				e.printStackTrace();
				return ;
			}
			inputEngine.processStatementFile(stmt);
		});
	}

	/*
	private HashMap<String, Future<Boolean>> convertResult = new HashMap<String, Future<Boolean>>();

	public synchronized void convertStatement(Statement stmt) {
		Future<Boolean> result = convertService.submit(() -> {
			File ifile = new File(stmt.getFilename());
			File pfile = new File(stmt.getFilename()).getParentFile();
			File ofile = new File(pfile.getAbsolutePath() + ifile.getName() + ".csv");
			try {
				ExcelToCSV.convertToXls(ifile, ofile);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		});
		convertResult.put(stmt.getFilename(), result);
	}

	public Boolean isConverted(Statement stmt) {
		if (convertResult.get(stmt).isDone()) {
			try {
				return convertResult.get(stmt).get();
			} catch (InterruptedException | ExecutionException e) {
				return false;
			}
		} else {
			return null;
		}
	}
	*/
	
	public void waitToShutdown() {
		convertService.shutdown();
		try {
			convertService.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			logger.error("Convert Service had to shutdown forcefully.");
		}

		service.shutdown();
		try {
			convertService.awaitTermination(12, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			logger.error("Process Service had to shutdown forcefully.");
		}

		logger.info("Exiting waitToShutdown.");
	}

	public InputEngine getInputEngine() {
		return inputEngine;
	}

	public void setInputEngine(InputEngine inputEngine) {
		this.inputEngine = inputEngine;
	}


	
}
