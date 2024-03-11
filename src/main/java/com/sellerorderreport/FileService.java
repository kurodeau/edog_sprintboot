package com.sellerorderreport;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.productorder.model.ProductOrderService;
import com.productorder.model.ProductOrderVO;

@Component
public class FileService {

	private final String workpath = "AAAExportOrderDetailsSeller_YYYY_MM_DD.xlsx";

	private Integer sellerId;

	@Autowired
	private ProductOrderService productOrdSvc;

	@Autowired
	private ResourceLoader resourceLoader;

	public String generateFiles(LocalDate startTime, LocalDate endTime, Integer sellerId) throws IOException {

		String sourcePath = "ExportOrderDetailsSeller_YYYY_MM_DD.xlsx";
		Resource resource = resourceLoader.getResource("classpath:spreadsheet/" + sourcePath);

		ClassPathResource classPathResource = new ClassPathResource(
				"spreadsheet/ExportOrderDetailsSeller_YYYY_MM_DD.xlsx");

//		ClassPathResource classPathResource = new ClassPathResource("spreadsheet/Test.xlsx");

		System.out.println("File path: " + classPathResource.getPath());

		String startTimeStr = startTime.format(DateTimeFormatter.ofPattern("YYYY_MM_DD"));
		String endTimeStr = endTime.format(DateTimeFormatter.ofPattern("YYYY_MM_DD"));
		
		
        Path staticPath = Paths.get(resourceLoader.getResource("classpath:static").getURI());
        String targetFileName = "OrderDetail_" + sellerId + "_" + startTimeStr + "_" + endTimeStr + ".xlsx";
        Path filePath = staticPath.resolve(targetFileName);
		
		String destinationPath = targetFileName;

		try (InputStream fis = classPathResource.getInputStream();
				FileOutputStream fos = new FileOutputStream(filePath.toFile());) {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Resource resourceDest = resourceLoader.getResource("classpath:spreadsheet/");
		
		
		Path staticPath2 = Paths.get(resourceLoader.getResource("classpath:static").getURI());
        String targetFileName2 = "OrderDetail_" + sellerId + "_" + startTimeStr + "_" + endTimeStr + ".xlsx";
        Path filePath2 = staticPath.resolve(targetFileName);
        System.out.println(filePath2.toAbsolutePath());
        
        
        try (FileInputStream in = new FileInputStream(filePath.toFile());
        	     Workbook wb = WorkbookFactory.create(in);
        	     FileOutputStream fos = new FileOutputStream(filePath.toFile())) {

        	    Sheet sheet = wb.getSheetAt(0);
    			List<ProductOrderVO> productOrderVOs = productOrdSvc.getBySellerId(sellerId);

        	    for (int row = 1; row <= productOrderVOs.size(); row++) {
        	        Row currentRow = sheet.getRow(row);
        	        if (currentRow != null) {
        	            for (int col = 0; col < currentRow.getLastCellNum(); col++) {
        	                Cell cell = currentRow.getCell(col);
        	                if (cell != null) {
        	                    if (cell.getCellType() == CellType.STRING) {
        	                        String stringValue = cell.getStringCellValue();
        	                        cell.setCellValue(stringValue + "XXX");
        	                    } else if (cell.getCellType() == CellType.NUMERIC) {
        	                        double numericValue = cell.getNumericCellValue();
        	                        cell.setCellValue(String.valueOf(numericValue) + "XXX");
        	                    }
        	                }
        	            }
        	        }
        	    }

        	    wb.write(fos);
        	    
        	} catch (IOException e) {
        	    e.printStackTrace();
        	}

        
        
     

		return "";
	}

}
