package me.puneetghodasara.txmgr.core.util;

import java.io.FileWriter;
import java.io.IOException;

public class RuleProblemWriter {

	public static void write(String description){
		try {
			FileWriter fileWriter = new FileWriter("RuleProblem.txt",true);
			fileWriter.write(description);
		} catch (IOException e) {
		}
	}
}
