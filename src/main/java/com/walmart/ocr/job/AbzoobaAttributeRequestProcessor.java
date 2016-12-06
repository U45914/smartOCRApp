package com.walmart.ocr.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.walmart.ocr.dao.OcrInfoDao;
import com.walmart.ocr.model.AbzoobaCompareModel;
import com.walmart.ocr.model.SmartOCRDataModel;
import com.walmart.ocr.util.GvisionResponseToOCRResponseConverter;
import com.walmart.ocr.util.JsonstringToMap;
import com.walmart.ocr.util.MessageConverter;

public class AbzoobaAttributeRequestProcessor {

	private static final String ABZOOBA_ENDPOINT_URL = "http://52.23.170.75:5000/beauty";

	private static final Logger _LOGGER = Logger.getLogger(AbzoobaAttributeRequestProcessor.class);

	private final static int _TIMEOUT = 2 * 60 * 1000;

	Client client = Client.create();

	public AbzoobaAttributeRequestProcessor() {
		client.setConnectTimeout(_TIMEOUT);
		client.setReadTimeout(_TIMEOUT);
	}

	@Autowired
	private OcrInfoDao ocrInfoDao;

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAttributes(SmartOCRDataModel ocrDataModel, String smartOcrId, String smartRequest)
			throws JSONException {

		String ocrInputStr = processStringForAbzooba(smartRequest);

		ocrDataModel.setAbsoobaRequestInfo(ocrInputStr);

		String response = sendRequestToAbzooba(ocrInputStr);

		List<Map<String, Object>> attributesList = JsonstringToMap.jsonToJsonList(response);

		return attributesList;
	}

	public Map<String, String> updateAttributeByCrowd(String ocrId, List<Map<String, Object>> abzoobaModel) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			SmartOCRDataModel smartOcrModel = ocrInfoDao.findOcrDataById(MessageConverter.getIdForTask(ocrId));

			if (smartOcrModel != null) {
				smartOcrModel.setAbzoobaResponse2(MessageConverter.getStringForObject(abzoobaModel));
			}
			ocrInfoDao.updateOcrData(smartOcrModel);

			message.put("message", "Values updated sucessfully");

		} catch (Exception e) {
			message.put("Error", "Failed to update data");
		}

		return message;
	}

	public List<Map<String, Object>> getAttributes(String ocrId) {
		List<Map<String, Object>> attributes = new ArrayList<Map<String, Object>>();
		try {
			SmartOCRDataModel smartOcrModel = ocrInfoDao.findOcrDataById(MessageConverter.getIdForTask(ocrId));

			if (smartOcrModel != null && smartOcrModel.getAbsoobaResponse() != null) {
				attributes = readAttributesFromJson(smartOcrModel.getAbsoobaResponse());
			}
		} catch (Exception e) {
			_LOGGER.error("Exception while fetching attributes", e);
		}

		return attributes;
	}

	public List<Map<String, Object>> readAttributesFromJson(String attributesJson) {

		return MessageConverter.getListOfMapFromJson(attributesJson);
	}

	private String sendRequestToAbzooba(String entity) {
		_LOGGER.info("Sending request to abzooba");
		WebResource webResource = client.resource(ABZOOBA_ENDPOINT_URL);

		ClientResponse serviceResponse = webResource.type("application/json").post(ClientResponse.class, entity);

		if (serviceResponse.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + serviceResponse.getStatus());
		}

		_LOGGER.info("Completed Abzooba request");
		return serviceResponse.getEntity(String.class);

	}

	public String processStringForAbzooba(String source) {
		source = source.replace("frontText", "FrontText");
		source = source.replace("backText", "BackText");
		source = source.replace("leftSideText", "LeftSideText");
		source = source.replace("rightSideText", "RightSideText");
		source = source.replace("topSideText", "TopSideText");
		source = source.replace("bottomSideText", "BottomSideText");
		source = source.replaceAll(GvisionResponseToOCRResponseConverter.BR, " ");

		return source;
	}

	protected static List<Map<String, Object>> addRequiredAttributesToList(List<Map<String, Object>> attributesList, String attributeName,
			String value) {
		Map<String, Object> attribute = new HashMap<String, Object>();
		attribute.put("Attribute", attributeName);
		attribute.put("Value", value);
		attribute.put("CLevel", 100);

		attributesList.add(attribute);

		return attributesList;
	}

	public List<AbzoobaCompareModel> getAbzoobaModel(TaskDto taskDto) {
		List<AbzoobaCompareModel> listOfAttributes = new ArrayList<AbzoobaCompareModel>();
		if (taskDto.getAttributes() != null) {
			List<Map<String, Object>> abzoobaResponse = taskDto.getAttributes();

			List<Map<String, Object>> abzoobaResponse2 = null;
			if (taskDto.getEditedAttributes() != null) {
				abzoobaResponse2 = taskDto.getEditedAttributes();
			}

			for (Map<String, Object> attribute : abzoobaResponse) {
				AbzoobaCompareModel abzoobaModel = new AbzoobaCompareModel();
				String parentAttributeKey = String.valueOf(attribute.get("Attribute"));
				abzoobaModel.setAttributeName(parentAttributeKey);
				abzoobaModel.setValueFromGivision(String.valueOf(attribute.get("Value")));
				abzoobaModel.setConfidenceLevel(String.valueOf(attribute.get("CLevel")));

				// add more info to list
				if (taskDto.getEditedAttributes() != null) {
					addAbzooba2ResponseToCompareModel(abzoobaModel, parentAttributeKey, abzoobaResponse2);
				}

				listOfAttributes.add(abzoobaModel);
			}
		}

		return listOfAttributes;
	}

	private void addAbzooba2ResponseToCompareModel(AbzoobaCompareModel compareModel, String parentAttributeKey,
			List<Map<String, Object>> abzoobaResponse) {

		String childAttributeKey = null;
		for (Map<String, Object> attribute : abzoobaResponse) {
			childAttributeKey = String.valueOf(attribute.get("Attribute"));

			if (parentAttributeKey.equals(childAttributeKey)) {
				compareModel.setValueFromCrowdSource(String.valueOf(attribute.get("Value")));
				break;
			}
		}
	}

	public void setOcrInfoDao(OcrInfoDao ocrInfoDao) {
		this.ocrInfoDao = ocrInfoDao;
	}

}
