/**
 * 
 */
package com.walmart.ocr.rest;

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
			SmartOCRDataModel taskData = ocrInfoDao.findOcrDataById(MessageConverter.getIdForTask(userTask));
			
			response = new JSONObject();
			response.put("data", new JSONObject(taskData.getGivisionResponse()));
			response.put("message", "1 Record returned");
		} else {
			response = new JSONObject();
			response.put("message", "No more pending task");
		}
		
		return Response.ok().type(MediaType.APPLICATION_JSON).entity(response).build();
		
	}

	public void setMyTaskProvider(MQTaskProvider myTaskProvider) {
		this.myTaskProvider = myTaskProvider;
	}

	public void setOcrInfoDao(OcrInfoDao ocrInfoDao) {
		this.ocrInfoDao = ocrInfoDao;
	}
	
	
}
