/**
 * 
 */
package com.walmart.ocr.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walmart.ocr.dao.OcrInfoDao;
import com.walmart.ocr.model.AbzoobaCompareModel;
import com.walmart.ocr.model.MyTaskModel;
import com.walmart.ocr.model.ParseRequest;
import com.walmart.ocr.model.SmartOCRDataModel;
import com.walmart.ocr.rabbit.listner.MQTaskProvider;
import com.walmart.ocr.util.MessageConverter;

/**
 * @author Rahul
 * 
 */
@Path("/mytask")
@Component
public class MyTaskResource {

	@Autowired
	private MQTaskProvider myTaskProvider;
	@Autowired
	private OcrInfoDao ocrInfoDao;

	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response getSingleTask() throws JSONException {

		String userTask = myTaskProvider.getUserTask();
		JSONObject response = null;
		if (userTask != null) {
			SmartOCRDataModel taskData = ocrInfoDao
					.findOcrDataById(MessageConverter.getIdForTask(userTask));

			GVisionData myData = new GVisionData();
			myData.setImage(taskData.getImage());
			myData.setRequest(MessageConverter
					.getParseRequestObjectFromJson(taskData
							.getGivisionResponse()));
			myData.getRequest().setFrontTextFormatted(myData.getRequest().getFrontTextFormatted().replaceAll("&nbsp;", " "));
			myData.setSmartId(userTask);

			return Response.ok().type(MediaType.APPLICATION_JSON)
					.entity(myData).build();
		} else {
			response = new JSONObject();
			response.put("message", "No more pending task");
		}

		return Response.ok().type(MediaType.APPLICATION_JSON).entity(response)
				.build();

	}

	@Path("/get/all")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response getAll() throws JSONException {
		Integer limit = getLimit();
		JSONObject response = null;
		if (limit != null) {
			List<SmartOCRDataModel> taskDatas = ocrInfoDao.findAllOcrData();
			System.out.println("*****************************************************************");
			System.out.println(taskDatas);
			System.out.println("*****************************************************************");
			return Response.ok().entity(getUserTasks(taskDatas)).build();
		}

		response = new JSONObject();
		response.put("message", "No more pending task");

		return Response.ok().type(MediaType.APPLICATION_JSON).entity(response)
				.build();

	}

	private Integer getLimit() {
		// TODO Auto-generated method stub
		return 10;
	}

	private List<MyTaskModel> getUserTasks(List<SmartOCRDataModel> taskDatas) {
		List<MyTaskModel> tasks = new ArrayList<MyTaskModel>();
		for (SmartOCRDataModel data : taskDatas) {
			MyTaskModel task = new MyTaskModel();
			task.setImage(data.getImage());
			task.setImageName(data.getImageUrls());
			task.setSmartId(data.getOcrRequestId().toString());
			task.setAbzoobaResponse(getAbzoobaModel(data));
		}

		return tasks;
	}

	private List<AbzoobaCompareModel> getAbzoobaModel(SmartOCRDataModel data) {
		List<Map<String, Object>> abzoobaResponse = MessageConverter
				.getListOfMapFromJson(data.getAbsoobaResponse());
		String abre2 =data.getAbzoobaResponse2();
		List<Map<String, Object>> abzoobaResponse2=null;
		if(abre2!=null){
			abzoobaResponse2 = MessageConverter
					.getListOfMapFromJson(abre2);
		}
		List<AbzoobaCompareModel> listOfAttributes = new ArrayList<AbzoobaCompareModel>();

		for (Map<String, Object> attribute : abzoobaResponse) {
			AbzoobaCompareModel abzoobaModel = new AbzoobaCompareModel();
			String parentAttributeKey = String.valueOf(attribute.get("Attribute"));
			abzoobaModel.setAttributeName(parentAttributeKey);
			abzoobaModel.setValueFromGivision(String.valueOf(attribute
					.get("Value")));
			abzoobaModel.setConfidenceLevel(String.valueOf(attribute
					.get("CLevel")));
			// add more info to list
			if(abre2!=null){
				addAbzooba2ResponseToCompareModel(abzoobaModel, parentAttributeKey, abzoobaResponse2);
			}

			listOfAttributes.add(abzoobaModel);
		}

		return listOfAttributes;
	}

	private void addAbzooba2ResponseToCompareModel(
			AbzoobaCompareModel compareModel, String parentAttributeKey,
			List<Map<String, Object>> abzoobaResponse) {

		String childAttributeKey=null;
		for(Map<String,Object> attribute : abzoobaResponse){
			childAttributeKey=String.valueOf(attribute.get("Attribute"));
			
			if(parentAttributeKey.equals(childAttributeKey)){
				compareModel.setValueFromCrowdSource(String.valueOf(attribute.get("Value")));
				compareModel.setConfidenceLevelAfterCrowd(String.valueOf(attribute.get("CLevel")));
				break;
			}
		}
	}

	public void setMyTaskProvider(MQTaskProvider myTaskProvider) {
		this.myTaskProvider = myTaskProvider;
	}

	public void setOcrInfoDao(OcrInfoDao ocrInfoDao) {
		this.ocrInfoDao = ocrInfoDao;
	}

	class GVisionData {
		byte[] image;
		ParseRequest request;
		String smartId;

		/**
		 * @return the image
		 */
		public byte[] getImage() {
			return image;
		}

		/**
		 * @param image
		 *            the image to set
		 */
		public void setImage(byte[] image) {
			this.image = image;
		}

		/**
		 * @return the request
		 */
		public ParseRequest getRequest() {
			return request;
		}

		/**
		 * @param request
		 *            the request to set
		 */
		public void setRequest(ParseRequest request) {
			this.request = request;
		}

		/**
		 * @return the smartId
		 */
		public String getSmartId() {
			return smartId;
		}

		/**
		 * @param smartId
		 *            the smartId to set
		 */
		public void setSmartId(String smartId) {
			this.smartId = smartId;
		}

	}

}