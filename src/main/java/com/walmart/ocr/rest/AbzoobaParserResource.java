package com.walmart.ocr.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.walmart.ocr.dao.OcrInfoDao;
import com.walmart.ocr.model.ParseRequest;
import com.walmart.ocr.model.SmartOCRDataModel;
import com.walmart.ocr.util.JsonstringToMap;
import com.walmart.ocr.util.MessageConverter;

@Path("/abzoobaParse")
@Component
public class AbzoobaParserResource {
private final static int _TIMEOUT = 2 * 60 * 1000; 
	private static final Logger logger = Logger.getLogger(AbzoobaParserResource.class);
	private static Client client = Client.create();
	
	public AbzoobaParserResource() {
		client.setConnectTimeout(_TIMEOUT);
		client.setReadTimeout(_TIMEOUT);
	}
	@Autowired
	private OcrInfoDao ocrInfoDao;
	
	public void setOcrInfoDao(OcrInfoDao ocrInfoDao) {
		this.ocrInfoDao = ocrInfoDao;
	}

	@POST
	@Path("/parseText")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile1(ParseRequest parseInput, @HeaderParam("userId") @DefaultValue("smartOcr") String userId, @HeaderParam("isCrowd") @DefaultValue("False") String isCrowd) {
		String output = null;
		// Only Fake Response.
		boolean fake = false;
		SmartOCRDataModel ocrData = null;
		
		if (ocrInfoDao != null) {
			
			ocrData = ocrInfoDao.findOcrDataById(MessageConverter.getIdForTask(parseInput.getSmartOcrId()));
			
			if (ocrData != null && isCrowd.equalsIgnoreCase("True")) {
				System.out.println("For Crowd Request");
				ocrData.setCrowdSourceUserId(userId);
				ocrData.setCrowdSourceResponse(MessageConverter.getStringForObject(parseInput));
				ocrInfoDao.updateOcrData(ocrData);
			}
		}
		
		
		Map<String, Object> myMap = null;
		try {
			logger("Parsing With  Abzooba ....");
			if (!fake) {
				

				// WebResource webResource =
				// client.resource("http://52.23.170.75:5000/model2");
				WebResource webResource = client.resource("http://52.23.170.75:5000/beauty");
				ObjectMapper mapper = new ObjectMapper();
				String jsonInString = mapper.writeValueAsString(parseInput);
				jsonInString = jsonInString.replace("frontText", "FrontText");
				jsonInString = jsonInString.replace("backText", "BackText");
				ocrData.setAbsoobaRequestInfo(jsonInString);
				ClientResponse serviceResponse = webResource.type("application/json").post(ClientResponse.class, jsonInString);

				if (serviceResponse.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + serviceResponse.getStatus());
				}

				output = serviceResponse.getEntity(String.class);
				//output = "[{\"Brand\":\"Fresh\",\"Product_Name\":\"Fresh  New Look Same Great Smart Mouth \",\"Units_Per_Consumer_Unit\":\"Nil\",\"Product_Short_Description\":\"fresh   herb .\",\"Product_Long_Description\":\"Nil\",\"Ingredients\":\"Nil\",\"Gender\":\"Both\",\"Alcohol_Content\":\"True\",\"Is_Aerosol\":\"False\",\"Manufacturing_Country\":\"NIL\",\"Direction_Of_Use\":\"Nil\",\"Warning\":\"Nil\",\"Back_Side_Color\":\"Nil\",\"Front_Side_Color\":\" Seagreen\",\"Manufacturer\":\"NIL\",\"Age\":\"Nil\",\"Skin_Type\":\"Nil\",\"Hair_Type\":\"Nil\"},{\"Confidence_Score_Brand\":0.88,\"Confidence_Score_Product_Name\":0.88,\"Confidence_Score_Units_Per_Consumer_Unit\":0.95,\"Confidence_Score_Product_Short_Description\":0.47,\"Confidence_Score_Product_Long_Description\":0.0,\"Confidence_Score_Gender\":0.65,\"Confidence_Score_Manufacturing_Country\":1.0,\"Confidence_Score_Direction_Of_Use\":0.52,\"Confidence_Score_Warning\":0.0,\"Confidence_Score_Back_Side_Color\":1.0,\"Confidence_Score_Front_Side_Color\":1.0,\"Confidence_Score_Manufacturer\":1.0,\"Confidence_Score_Age\":0.84,\"Confidence_Score_Ingredients\":0.5,\"Confidence_Score_Skin_Type\":0.65,\"Confidence_Score_Hair_Type\":0.65}] ";
				logger("***********************************");
				logger(output);
				System.out.println(output);
				logger("***********************************");
				myMap = new LinkedHashMap<String, Object>();
				List<Map<String, Object>> response = JsonstringToMap.jsonToJsonList(output);
				
				response.remove("id");
				response.remove("Raw_Data");
				Map<String, Object> upc = new HashMap();
				upc.put("Attribute", "UPC");
				upc.put("Value", parseInput.getId());
				upc.put("CLevel", 100);
				
								
				response.add(upc);
				
				String finalResponse = MessageConverter.getStringForObject(response);
				
				if (isCrowd.equalsIgnoreCase("True")) {
					ocrData.setAbzoobaResponse2(finalResponse);
				} else {
					ocrData.setAbsoobaResponse(finalResponse);					
				}
				
				ocrInfoDao.updateOcrData(ocrData);
				return Response.ok().entity(MessageConverter.getStringForObject(response)).build();

			} else {
				myMap = new LinkedHashMap<String, Object>();
				myMap.put("Brand", "Lego");
				myMap.put("Age", "7-14");
				myMap.put("Warning", "choking hazard");
				myMap.put("Pieces", "248");
			}

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Final save to Database
		//ocrData.setAbsoobaResponse(MessageConverter.getStringForObject(myMap));
		ocrInfoDao.updateOcrData(ocrData);
		return  Response.ok().entity(myMap).build();
	}

	void logger(String log) {
		logger.debug(log);
	}
	
	ObjectMapper mapper = new ObjectMapper();
	
	public List<Map<String,Object>> processParseRequestWithAbzooba(ParseRequest pRequest) {
		Map<String, Object> myMap = null;
		String output = null;
		try {
			logger("Parsing With  Abzooba ....");
			
				Client client = Client.create();

				// WebResource webResource =
				// client.resource("http://52.23.170.75:5000/model2");
				WebResource webResource = client.resource("http://52.23.170.75:5000/beauty");
				String jsonInString = mapper.writeValueAsString(pRequest);
				jsonInString = jsonInString.replace("frontText", "FrontText");
				jsonInString = jsonInString.replace("backText", "BackText");
				ClientResponse serviceResponse = webResource.type("application/json").post(ClientResponse.class, jsonInString);

				if (serviceResponse.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + serviceResponse.getStatus());
				}

				output = serviceResponse.getEntity(String.class);
				//output = "[{\"Brand\":\"Fresh\",\"Product_Name\":\"Fresh  New Look Same Great Smart Mouth \",\"Units_Per_Consumer_Unit\":\"Nil\",\"Product_Short_Description\":\"fresh   herb .\",\"Product_Long_Description\":\"Nil\",\"Ingredients\":\"Nil\",\"Gender\":\"Both\",\"Alcohol_Content\":\"True\",\"Is_Aerosol\":\"False\",\"Manufacturing_Country\":\"NIL\",\"Direction_Of_Use\":\"Nil\",\"Warning\":\"Nil\",\"Back_Side_Color\":\"Nil\",\"Front_Side_Color\":\" Seagreen\",\"Manufacturer\":\"NIL\",\"Age\":\"Nil\",\"Skin_Type\":\"Nil\",\"Hair_Type\":\"Nil\"},{\"Confidence_Score_Brand\":0.88,\"Confidence_Score_Product_Name\":0.88,\"Confidence_Score_Units_Per_Consumer_Unit\":0.95,\"Confidence_Score_Product_Short_Description\":0.47,\"Confidence_Score_Product_Long_Description\":0.0,\"Confidence_Score_Gender\":0.65,\"Confidence_Score_Manufacturing_Country\":1.0,\"Confidence_Score_Direction_Of_Use\":0.52,\"Confidence_Score_Warning\":0.0,\"Confidence_Score_Back_Side_Color\":1.0,\"Confidence_Score_Front_Side_Color\":1.0,\"Confidence_Score_Manufacturer\":1.0,\"Confidence_Score_Age\":0.84,\"Confidence_Score_Ingredients\":0.5,\"Confidence_Score_Skin_Type\":0.65,\"Confidence_Score_Hair_Type\":0.65}] ";
				logger("***********************************");
				logger(output);
				System.out.println(output);
				logger("***********************************");
				myMap = new LinkedHashMap<String, Object>();
				List<Map<String, Object>> response = JsonstringToMap.jsonToJsonList(output);
				
				response.remove("id");
				response.remove("Raw_Data");
				Map<String, Object> upc = new HashMap();
				upc.put("Attribute", "UPC");
				upc.put("Value", pRequest.getId());
				upc.put("CLevel", 100);
				response.add(upc);

				Map<String, Object> frontText = new HashMap();
				frontText.put("Attribute", "FrontText");
				frontText.put("Value", pRequest.getFrontText());
				frontText.put("CLevel", 100);
				response.add(frontText);

				Map<String, Object> backText = new HashMap();
				backText.put("Attribute", "BackText");
				backText.put("Value", String.valueOf(pRequest.getFrontText()));
				backText.put("CLevel", 100);
				response.add(backText);
				
				String finalResponse = MessageConverter.getStringForObject(response);
				
				
				return response;

			

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
