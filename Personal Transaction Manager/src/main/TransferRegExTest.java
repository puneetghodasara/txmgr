package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransferRegExTest {

//	private static String pattern = "ATM WITHDRAWAL .* Card no.:([X0-9]*) Ref: [0-9]* ([A-Za-z0-9]*).*$"; 
//	private static String txDesc = "ATM WITHDRAWAL SUBJECT: NFSATM 14AUG1816 Card no.: 5497XXX0XXXX2905 Ref: 498 INDIRANAGARBRANCH 496SABANGALORE KAIN";
	
	private static String pattern = "ATM WITHDRAWAL .* Card no.: ([A-Z0-9]*) Ref: ([0-9]*) ([A-Za-z0-9]*) .*$"; 
	private static String txDesc = "ATM WITHDRAWAL SUBJECT: NFSATM 14AUG1816 Card no.: 5497XXX0XXXX2905 Ref: 498 INDIRANAGARBRANCH 496SABANGALORE KAIN";

	
	private static Integer accNoGroupNo = 1, refNoGroupNo = 2, tagNoGroupNo = 3;

	
	public static void main(String[] args) {
		Pattern customPattern = Pattern.compile(pattern);
		Matcher customMatcher = customPattern.matcher(txDesc);
		if (customMatcher.matches()) {
			String accNo = customMatcher.group(accNoGroupNo);
			String refNo = refNoGroupNo == null ? "" : customMatcher.group(refNoGroupNo);
			String tagDesc = tagNoGroupNo == null ? "" : customMatcher.group(tagNoGroupNo);

			System.out.println("Acc No :"+accNo);
			System.out.println("Ref No :"+refNo);
			System.out.println("Tag Desc :"+tagDesc);
		}else{
			System.out.println("No Match");
		}

	}
}
