package com.smartocr.ocr.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class GenerateExcel {
	
	public static void generateReport(Map<String, List<Map<String, Object>>> maps) {
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Report");
		
		int rownum = 0;
		for(Map.Entry<String, List<Map<String, Object>>> entry : maps.entrySet()){
			String key = entry.getKey();
			List<Map<String, Object>> attrs = entry.getValue();
			
			HSSFCellStyle style = workbook.createCellStyle();
			style.setBorderTop((short) 6); // double lines border
			style.setBorderBottom((short) 1); // single line border
			
			HSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style.setFont(font);
			
			int columnIndex = 0;
			int headerIndex = 0;
			if(rownum==0){
				Row header = sheet.createRow(rownum++);
				header.setRowStyle(style);
				Cell col1 = header.createCell(headerIndex);
				col1.setCellValue("GTIN");
			}
			
			Row row = sheet.createRow(rownum++);
			Cell gtinCell = row.createCell(columnIndex);
			gtinCell.setCellValue(key);
			
			//list of attrs
			for(Map<String, Object> attr : attrs){
				if(rownum==2){
					Row header = sheet.getRow(0);
					Cell col2 = header.createCell(++headerIndex);
					col2.setCellValue(String.valueOf(attr.get("Attribute"))+" - Value");
					Cell col4 = header.createCell(++headerIndex);
					col4.setCellValue(String.valueOf(attr.get("Attribute"))+" - Confidence");
				}
				
				Cell cell2 = row.createCell(++columnIndex);
				cell2.setCellValue(String.valueOf(attr.get("Value")));
				
				Cell cell3 = row.createCell(++columnIndex);
				cell3.setCellValue(String.valueOf(attr.get("CLevel")));
				
			}
		}
		
		try {
			FileOutputStream out = new FileOutputStream(new File("D:\\Report3.xls"));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}

}
