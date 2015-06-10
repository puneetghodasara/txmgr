package me.puneetghodasara.txmgr.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import me.puneetghodasara.txmgr.model.db.Transaction;
import me.puneetghodasara.txmgr.model.parser.StatementParser;

import com.opencsv.bean.BeanToCsv;

public class UnmatchedTransactionWriter {

	public static void write(Transaction transaction){
		try {
			FileWriter fileWriter = new FileWriter("unmatchedtx.txt",true);
			
			StringBuilder entry = new StringBuilder();
			entry.append(transaction.getDescription());
			entry.append("\n");
			
			fileWriter.append(entry.toString());
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
