package com.sellerorderreport;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

import com.orderdetails.model.OrderDetailsVO;
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

//		System.out.println("File path: " + classPathResource.getPath());

		String startTimeStr = startTime.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
		String endTimeStr = endTime.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));

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

//		Path staticPath2 = Paths.get(resourceLoader.getResource("classpath:static").getURI());
//        String targetFileName2 = "OrderDetail_" + sellerId + "_" + startTimeStr + "_" + endTimeStr + ".xlsx";
//        Path filePath2 = staticPath.resolve(targetFileName);
//        System.out.println(filePath2.toAbsolutePath());

		try (FileInputStream in = new FileInputStream(filePath.toFile());
				Workbook wb = WorkbookFactory.create(in);
				FileOutputStream fos = new FileOutputStream(filePath.toFile())) {

			Sheet sheet = wb.getSheetAt(0);
			List<ProductOrderVO> productOrderVOs = productOrdSvc.findAllBySellerIdOrderByTime(sellerId);

//			System.out.println(productOrderVOs.get(0).getOrderId());
			Integer a1 = productOrderVOs.get(0).getOrderId();

			Row currentRow;
//			currentRow = sheet.getRow(0);
//			for (int col = 0; col < currentRow.getLastCellNum(); col++) {
//				Cell cell = currentRow.getCell(col);
//				if (cell != null) {
//					System.out.print(cell.getStringCellValue() + "\t");
//				}
//			}

			List<OrderReportDTO> combinedData = new ArrayList<>();
			for (ProductOrderVO productOrderVO : productOrderVOs) {
				Set<OrderDetailsVO> orderDetailss = productOrderVO.getOrderDetailss();
				for (OrderDetailsVO orderDetail : orderDetailss) {
					OrderReportDTO o = new OrderReportDTO();
					o.setOrderId(orderDetail.getOrderDetailsId());
					o.setStatus(productOrderVO.getOrderStatus());
					o.setQuantity(orderDetail.getPurchaseQuantity());
					o.setUnitPrice(orderDetail.getProductVO().getPrice());
					o.setOrderDate(productOrderVO.getOrderTime().toLocalDateTime());
					o.setProductName(orderDetail.getProductVO().getProductName());
					combinedData.add(o);
				}
			}

			for (int row = 1; row <= combinedData.size(); row++) {
				currentRow = sheet.getRow(row);
				if (currentRow != null) {
					for (int col = 0; col < currentRow.getLastCellNum(); col++) {
						Cell cell = currentRow.getCell(col);

						int currentIndex = row - 1;
						if (cell != null) {
							if (cell.getCellType() == CellType.STRING) {
								String stringValue = cell.getStringCellValue();
								cell.setCellValue(stringValue + "XXX");
							} else if (cell.getCellType() == CellType.NUMERIC) {
								Double numericValue = cell.getNumericCellValue();
								cell.setCellValue(String.valueOf(numericValue) + "XXX");
							} else if (cell.getCellType() == CellType.BLANK) {
								switch (col) {
								case 1:
								    cell.setCellValue(combinedData.get(currentIndex).getOrderId());
								    break;
								case 2:
									
								    cell.setCellValue(combinedData.get(currentIndex).getOrderDate().toString());
								    break;
								case 3:
								    cell.setCellValue(combinedData.get(currentIndex).getProductName());
								    break;
								case 4:
								    cell.setCellValue(combinedData.get(currentIndex).getQuantity());
								    break;
								case 5:
								    cell.setCellValue(combinedData.get(currentIndex).getUnitPrice().doubleValue());
								    break;
								case 6:
								    cell.setCellValue(combinedData.get(currentIndex).getStatus());
								    break;
								case 7: // 金額列，动态计算
									String rowInExcel = String.valueOf(row+1);
									cell.setCellFormula("IFERROR(E$"+rowInExcel+" * F$"+rowInExcel+", \"\")");
								    break;
								case 8: // 狀態列，使用 VLOOKUP 函数
								    String lookupValue = "G" + ( String.valueOf(row+1)); // 要查找的值所在单元格
								    String tableArray = "代碼!$B:$C"; // 查找范围
								    int colIndex = 2; // 返回值所在列的索引
								    cell.setCellFormula("IFERROR(VLOOKUP(" + lookupValue + "," + tableArray + "," + colIndex + ",0), \"\")");
								    break;
								default:
								    // 处理默认情况
								    break;
								}
							}
						}
					}

				}

			}
			wb.write(fos);
		}

		return "";
	}

}
