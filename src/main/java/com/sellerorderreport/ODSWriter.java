package com.sellerorderreport;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ODSService {

	private static final String DOCU_PATH = "XX";

	public static void main(String[] args) throws IOException {

		String sellerId = "";
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();

		Row row1 = sheet.createRow(0);

		Cell cell1 = row1.createCell(0);

		LocalDateTime time = LocalDateTime.now();
		cell1.setCellValue(time.toString());

		FileOutputStream fileoutputstream = new FileOutputStream(DOCU_PATH + "_" + sellerId + ".xlsx");
		workbook.write(fileoutputstream);

		fileoutputstream.close();

		long begin = System.currentTimeMillis();

		FileInputStream in = new FileInputStream("");
		HSSFWorkbook wb1 = new HSSFWorkbook(in);
		Sheet s1 = wb1.getSheetAt(0);
		String cell = new DataFormatter().formatCellValue(cell1);

		FormulaEvaluator ff = new HSSFFormulaEvaluator(wb1);
		CellType celltype = cell1.getCellType();
		switch (cell1.getCellType()) {
		case ERROR:

		case FORMULA:

		}

	}

}