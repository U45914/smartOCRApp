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

			if(taskData!=null){
				GVisionData myData = new GVisionData();
				myData.setFrontImage(taskData.getImage());
				myData.setBackImage(taskData.getBackImage());
				myData.setLeftImage(taskData.getLeftImage());
				myData.setRightImage(taskData.getRightImage());
				myData.setTopImage(taskData.getTopImage());
				myData.setBottomImage(taskData.getBottomImage());
				myData.setRequest(MessageConverter.getParseRequestObjectFromJson(taskData.getGivisionResponse()));
				myData.setAttributeBag(MessageConverter.getListOfMapFromJson(taskData.getAbsoobaResponse()));
				myData.setSmartId(userTask);

				return Response.ok().type(MediaType.APPLICATION_JSON)
						.entity(myData).build();
			}
			else{
				response = new JSONObject();
				response.put("message", "No such task in DB");
			}
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
			if(data.getAbsoobaResponse()!=null){
				MyTaskModel task = new MyTaskModel();
				task.setImage(data.getImage());
				task.setImageName(data.getImageUrls());
				task.setBackImage(data.getBackImage());
				task.setLeftImage(data.getLeftImage());
				task.setRightImage(data.getRightImage());
				task.setTopImage(data.getTopImage());
				task.setBottomImage(data.getBottomImage());
				task.setSmartId(MessageConverter.getSmartOCRId(data.getOcrRequestId()));
				task.setAbzoobaResponse(getAbzoobaModel(task, data));
				tasks.add(task);
			}
		}

		return tasks;
	}

	private List<AbzoobaCompareModel> getAbzoobaModel(MyTaskModel task, SmartOCRDataModel data) {
		List<AbzoobaCompareModel> listOfAttributes = new ArrayList<AbzoobaCompareModel>();
		if(data.getAbsoobaResponse()!=null){
		List<Map<String, Object>> abzoobaResponse = MessageConverter
				.getListOfMapFromJson(data.getAbsoobaResponse());
		String abre2 =data.getAbzoobaResponse2();
		List<Map<String, Object>> abzoobaResponse2=null;
		if(abre2!=null){
			abzoobaResponse2 = MessageConverter.getListOfMapFromJson(abre2);
		}
		
		for (Map<String, Object> attribute : abzoobaResponse) {
			AbzoobaCompareModel abzoobaModel = new AbzoobaCompareModel();
			String parentAttributeKey = String.valueOf(attribute.get("Attribute"));
			abzoobaModel.setAttributeName(parentAttributeKey);
			abzoobaModel.setValueFromGivision(String.valueOf(attribute
					.get("Value")));
			abzoobaModel.setConfidenceLevel(String.valueOf(attribute
					.get("CLevel")));
			if (parentAttributeKey.equalsIgnoreCase("UPC")) {
				task.setUpc(String.valueOf(attribute.get("Value")));
			}
			// add more info to list
			if(abre2!=null){
				addAbzooba2ResponseToCompareModel(abzoobaModel, parentAttributeKey, abzoobaResponse2);
			}

			listOfAttributes.add(abzoobaModel);
		}
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
		byte[] frontImage;
		ParseRequest request;
		String smartId;
		byte [] backImage;
		byte[] leftImage;
		byte [] rightImage;
		byte[] topImage;
		byte[] bottomImage;
		List<Map<String, Object>> attributeBag;

		

		public byte[] getFrontImage() {
			return frontImage;
		}

		public void setFrontImage(byte[] frontImage) {
			this.frontImage = frontImage;
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

		/**
		 * @return the backImage
		 */
		public byte[] getBackImage() {
			return backImage;
		}

		/**
		 * @param backImage the backImage to set
		 */
		public void setBackImage(byte[] backImage) {
			this.backImage = backImage;
		}

		public byte[] getLeftImage() {
			return leftImage;
		}

		public void setLeftImage(byte[] leftImage) {
			this.leftImage = leftImage;
		}

		public byte[] getRightImage() {
			return rightImage;
		}

		public void setRightImage(byte[] rightImage) {
			this.rightImage = rightImage;
		}

		public byte[] getTopImage() {
			return topImage;
		}

		public void setTopImage(byte[] topImage) {
			this.topImage = topImage;
		}

		public byte[] getBottomImage() {
			return bottomImage;
		}

		public void setBottomImage(byte[] bottomImage) {
			this.bottomImage = bottomImage;
		}

		public List<Map<String, Object>> getAttributeBag() {
			return attributeBag;
		}

		public void setAttributeBag(List<Map<String, Object>> attributeBag) {
			this.attributeBag = attributeBag;
		}

	}

}
