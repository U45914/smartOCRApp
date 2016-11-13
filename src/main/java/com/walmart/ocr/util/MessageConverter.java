package com.walmart.ocr.util;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	
	public static String getSmartOCRId(Integer id) {
		String smartId = "SMART-" + id;	
		return smartId;
	}
	
	public static Integer getIdForTask(String smartOcrId) {
		String id = smartOcrId.replace("SMART-", "");
		return Integer.valueOf(id);
	}
	
	public static String getStringForObject(Object input) {
		try {
			return mapper.writeValueAsString(input);
		} catch (JsonProcessingException e) {
			return null;
		}
	}
}
