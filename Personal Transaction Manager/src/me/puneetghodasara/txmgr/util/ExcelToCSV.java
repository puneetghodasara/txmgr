package me.puneetghodasara.txmgr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

@Component(value="excel2csv")
public class ExcelToCSV {


	public static void convertToXls(File inputFile, File outputFile) {
		// For storing data into CSV files
		StringBuffer cellDData = new StringBuffer();
		try {
			FileOutputStream fos = new FileOutputStream(outputFile);

			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
					inputFile));
			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);
			Cell cell;
			Row row;

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();

				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					cell = cellIterator.next();

					cellDData.append("\"");
					
					switch (cell.getCellType()) {

					case Cell.CELL_TYPE_BOOLEAN:
						cellDData.append(cell.getBooleanCellValue());
						break;

					case Cell.CELL_TYPE_NUMERIC:
						if(((HSSFCell)cell).getCellStyle().getFont(workbook).getColor() == Font.COLOR_RED){
							cellDData.append("-");
						}
						cellDData.append(cell.getNumericCellValue());
						break;

					case Cell.CELL_TYPE_STRING:
						cellDData.append(cell.getStringCellValue());
						break;

					case Cell.CELL_TYPE_BLANK:
						cellDData.append("");
						break;

					default:
						cellDData.append(cell);

					}
					cellDData.append("\",");
				}
				cellDData.append("\n");
			}

			fos.write(cellDData.toString().getBytes());
			fos.close();

		} catch (FileNotFoundException e) {
			System.err.println("Exception" + e.getMessage());
		} catch (IOException e) {
			System.err.println("Exception" + e.getMessage());
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		File inputFile = new File("statement\\ICICI.xls");
		File outputFile = new File("statement\\ICICI.csv");
//		File inputFile2 = new File("C:\\input.xlsx");
//		File outputFile2 = new File("C:\\output2.csv");
		convertToXls(inputFile, outputFile);
//		convertToXlsx(inputFile2, outputFile2);
	}
}