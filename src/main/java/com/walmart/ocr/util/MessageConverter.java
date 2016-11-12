package com.walmart.ocr.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.ocr.model.SmartOCRDataModel;

public class MessageConverter {

	private final static ObjectMapper mapper = new ObjectMapper();
	
	public static String convertSmartOcrDataModelToJsonString(SmartOCRDataModel dataModel) {
		String response = "{}";
		try {
			response = mapper.writeValueAsString(dataModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
}
