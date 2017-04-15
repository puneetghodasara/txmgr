package me.puneetghodasara;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransferRegExTest {

	// private static String pattern = "ATM WITHDRAWAL .* Card no.:([X0-9]*)
	// Ref: [0-9]* ([A-Za-z0-9]*).*$";
	// private static String txDesc = "ATM WITHDRAWAL SUBJECT: NFSATM 14AUG1816
	// Card no.: 5497XXX0XXXX2905 Ref: 498 INDIRANAGARBRANCH 496SABANGALORE
	// KAIN";

	// private static String pattern = "ATM WITHDRAWAL .* Card no.: ([A-Z0-9]*)
	// Ref: ([0-9]*) (\\S*) .*$";
	// private static String txDesc = "ATM WITHDRAWAL SUBJECT: NFSATM 12NOV1843
	// Card no.: 5497XXX0XXXX2905 Ref: 863 +THIPPASANDRA,INDIRA NABANGALORE
	// KAIN";

	// private static String pattern = "PURCHASE SUBJECT: .* Card no.: ([X0-9]*)
	// \\S* (.*) Ref: ([0-9]*)$";
	// private static String txDesc = "PURCHASE SUBJECT: MCUPOS 12JUN1720 Card
	// no.: 5497XXX0XXXX2905 12JUN RELIANCE MF -BILLDESK Ref: 516319255068";

	private static String pattern = "UTILITY PAYMENT (.*) -([0-9]*)$";
	private static String txDesc = "UTILITY PAYMENT BIRLA SUN MUTUAL FUND ALL INDIA PAYMENTS BSDIRECT -033505";

	// private static String pattern = "EFT TO UTR#(\\S*) .* -([X0-9]*) -.*$";
	// private static String txDesc = "EFT TO UTR#CITIN15610382960- -PADMA R -SA
	// -XXXXXXXX5083 -ICICI BANK LT";

	// private static String pattern = "FUND TRANSFER UTR ([0-9]*) .* FROM
	// (\\S+\\s\\S+\\s\\S+) .*$";
	// private static String txDesc = "FUND TRANSFER UTR 1522728890000060 TRF
	// FROM RMF DIVIDEND POOL AC RMF DIV IH 36118673 TSAD R ";

	// private static String pattern = "NEFT INWARD NEFT [A-Za-z\\s]*
	// ([A-Za-z0-9]*) FROM ([A-Za-z\\s]*|[X0-9]*) ([A-Za-z0-9]*) .*$";
	// private static String txDesc = "NEFT INWARD NEFT IN UTR CITIN15540765804
	// FROM Mr SUMIT JAYANTILAL GHOD SBIN215126144490T Mr SUMIT JAYANTILAL G";
	// private static String txDesc = "NEFT INWARD NEFT IN UTR CITIN14489341566
	// FROM 0367001131301814 N326140042915842TXN REF NO 0367001131301814INFORM";

	// private static String pattern = "IMPS DR TRN INTER CITY TRANSFERRED FROM
	// A/C OF [A-Za-z0-9\\s]* TO A/C OF ([A-Za-z0-9\\s]*) .*$";
	// private static String txDesc = "IMPS DR TRN INTER CITY TRANSFERRED FROM
	// A/C OF PUNIT J GHODASARA TO A/C OF US VISA FEE COLLECTION A/C";

	private static Integer accNoGroupNo = 1, refNoGroupNo = null, tagNoGroupNo = null;

	public static void main(String[] args) {
		Pattern customPattern = Pattern.compile(pattern);
		Matcher customMatcher = customPattern.matcher(txDesc);
		if (customMatcher.matches()) {
			String accNo = customMatcher.group(accNoGroupNo);
			String refNo = refNoGroupNo == null ? "" : customMatcher.group(refNoGroupNo);
			String tagDesc = tagNoGroupNo == null ? "" : customMatcher.group(tagNoGroupNo);

			System.out.println("Acc No :" + accNo);
			System.out.println("Ref No :" + refNo);
			System.out.println("Tag Desc :" + tagDesc);
		} else {
			System.out.println("No Match");
		}

	}
}
