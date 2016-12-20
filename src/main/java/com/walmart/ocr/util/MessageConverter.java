package com.walmart.ocr.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;



import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.ocr.model.ParseRequest;
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
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<Map<String, Object>> getListOfMapFromJson(String json) {
		try {
			System.out.println("************************** : " + json);
			return mapper.readValue(json, List.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public static ParseRequest getParseRequestObjectFromJson(
			String givisionResponse) {
		try {
			ParseRequest pRequest = mapper.readValue(givisionResponse, ParseRequest.class);
			return pRequest;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ParseRequest();
	}
	
	public static String convertUpcToGTIN(String upc) {
		String gtin = null;
		if(upc.length() < 14) {
			int length = 14-upc.length();
			gtin = StringUtils.leftPad(upc, 14, '0');
		}
		return gtin;
	}
	
	
}
