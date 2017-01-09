package com.smartocr.ocr.rest.automation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.smartocr.ocr.model.ParseRequest;
import com.smartocr.ocr.rest.AbzoobaParserResource;
import com.smartocr.ocr.rest.GoogleVisionResource;
import com.smartocr.ocr.util.GenerateExcel;

@Path("/batch")
@Component
public class SmartOCRRun {
	final static String SOURCE_LOCATION  = "D:\\New\\Images";
	//final static String SOURCE_LOCATION  = "D:\\OCR_Images\\Images";
	final static String SORTED_FILE_LOCATION = "D:\\OCR_Images\\Sorted";
	
	private GoogleVisionResource gVisionResource = new GoogleVisionResource();
	private AbzoobaParserResource abResource = new AbzoobaParserResource();
	
	@GET
	@Path("/process")
	@Produces("application/json")
	public Response startSmartOCR() {
		
		File file = new File(SOURCE_LOCATION);
		List<String> failedProducts = new ArrayList<String>();
		
		List<File> listFiles = (List<File>) FileUtils.listFiles(file, null, false);
		Collections.sort(listFiles);
		Map<String, List<File>> fileGroups = new HashMap<String, List<File>>();
		Map<String, List<Map<String, Object>>> gtinsAttribMap = new HashMap<String, List<Map<String,Object>>>();
		try {
			for(File imgFile : listFiles) {
				String fName = imgFile.getName();
				
				String upc = "";
				if (!fName.contains("-")) {
					upc = fName.substring(0, fName.indexOf("."));
					if (!fileGroups.containsKey(upc)) {
						fileGroups.put(upc, new ArrayList<File>());
					}			
					String newFileName = imgFile.getAbsolutePath().replaceFirst("\\.", "-");
					File newFile = new File(newFileName);
					imgFile.renameTo(newFile);
					fileGroups.get(upc).add(newFile);			
				} else {
					upc = fName.substring(0, fName.indexOf("-"));
					if (!fileGroups.containsKey(upc)) {
						fileGroups.put(upc, new ArrayList<File>());
					}
					fileGroups.get(upc).add(imgFile);
				}
				
			}
			
			
			for(Entry<String, List<File>> fileEntry : fileGroups.entrySet()) {
				List<File> files = fileEntry.getValue();
				String upc = fileEntry.getKey();
				List<File> filesToProcess = new ArrayList<File>();
				boolean isMoreThanTwoFiles = files.size() > 2;
				
				if (isMoreThanTwoFiles) {
					filesToProcess.add(files.get(0));
					filesToProcess.add(files.get(2));
				} else {
					filesToProcess.add(files.get(0));
					if (files.size() > 1){
						filesToProcess.add(files.get(1));
					}
				}
				
				try {
					List<Map<String, Object>> attribForGTIN = processFiles(filesToProcess);
					gtinsAttribMap.put(upc, attribForGTIN);
					
				} catch (Exception e) {
					failedProducts.add(upc);
					System.out.println("Parsing UPC Images caused some issue : "+ upc);
					e.printStackTrace();
				}
				
				System.out.println(">>>>>>>>>>>>>>>>>>>> " + upc);
			}
			GenerateExcel.generateReport(gtinsAttribMap);
		} catch (Exception e) {
			System.out.println("*****************************************");
			System.out.println(failedProducts.toString());
			System.out.println("*****************************************");
			GenerateExcel.generateReport(gtinsAttribMap);
			e.printStackTrace();
			
		}
		
		
		
		return Response.ok(gtinsAttribMap).type(MediaType.APPLICATION_JSON).build();
		
	}
	
	
	private List<Map<String, Object>> processFiles(List<File> imageFiles) {
		ParseRequest request = gVisionResource.parseFileRequest(imageFiles);
		request.setFrontText(request.getFrontText().replaceFirst("Optimized by www.Imageoptimizer.net", ""));
		request.setBackText(request.getBackText().replaceFirst("Optimized by www.Imageoptimizer.net", ""));
		//abResource.uploadFile1(request, "guest", false);
		List<Map<String, Object>> processParseRequestWithAbzooba = abResource.processParseRequestWithAbzooba(request);
		
		return processParseRequestWithAbzooba;
	}

}
