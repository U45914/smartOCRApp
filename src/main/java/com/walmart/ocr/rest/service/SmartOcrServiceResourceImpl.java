/**
 * 
 */
package com.walmart.ocr.rest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walmart.ocr.image.ImageServiceProcessor;
import com.walmart.ocr.job.AbzoobaAttributeRequestProcessor;
import com.walmart.ocr.job.OcrImageDto;
import com.walmart.ocr.job.SmartOcrTaskCreator;
import com.walmart.ocr.job.SmartOcrTaskProvider;
import com.walmart.ocr.job.TaskDto;

/**
 * @author Rahul
 *
 *
 */
@Path("/services")
@Component
public class SmartOcrServiceResourceImpl implements SmartOcrServiceResource {

	private static final Logger _LOGGER = Logger.getLogger(SmartOcrServiceResourceImpl.class);
	@Autowired
	private SmartOcrTaskCreator ocrTaskCreator;
	@Autowired
	private ImageServiceProcessor imageServiceProcessor;
	@Autowired
	private AbzoobaAttributeRequestProcessor attributeRequestProcessor;
	@Autowired
	private SmartOcrTaskProvider taskProvider;

	@Override
	@POST
	@Path("/uploadImages")
	@Consumes("multipart/form-data")
	@Produces("application/json")
	public Response uploadProductImages(@Context HttpServletRequest request) {
		_LOGGER.info("Received request for upload images");
		String smartOcrId = ocrTaskCreator.submitJob(request);
		Map<String, String> response = new HashMap<String, String>(1);
		response.put("smartId", smartOcrId);
		_LOGGER.info("Completed request for upload images");
		return Response.ok().entity(response).build();
	}

	@Override
	@GET
	@Path("/images/{smartOcrId}")
	@Produces("application/json")
	public Response getImagesBySmartId(@PathParam("smartOcrId") String smartOcrId) {
		if (smartOcrId != null && !smartOcrId.isEmpty()) {
			List<OcrImageDto> images = imageServiceProcessor.getImagesById(smartOcrId, false);
			return Response.ok().entity(images).build();
		} else {
			Map<String, String> response = new HashMap<String, String>(1);
			response.put("message", "Invalid Request");
			return Response.status(400).entity(response).build();
		}
	}

	@Override
	@GET
	@Path("/crowdsource/get/task")
	@Produces("application/json")
	public Response getSingleOCRTask() {
		_LOGGER.info("Request received for get Task from Queue");
		TaskDto taskDto = taskProvider.getTaskForCrowd();

		if (taskDto != null) {
			_LOGGER.info("Completed get my task");
			return Response.ok().entity(taskDto).build();
		} else {
			Map<String, String> response = new HashMap<String, String>(1);
			response.put("message", "No more pending task");
			return Response.ok().entity(response).build();
		}
	}

	@Override
	@GET
	@Path("/tasks/history")
	@Produces("application/json")
	public Response getOCRTasks(@QueryParam("status") @DefaultValue("*") String status,
			@QueryParam("start") @DefaultValue("0") String start, @QueryParam("max") @DefaultValue("10") String max,
			@QueryParam("sort") @DefaultValue("asc") String orderBy) {
		_LOGGER.info("Request received for get all Tasks");
		List<TaskDto> taskDtos = taskProvider.getTasks(status, start, max, orderBy);

		if (taskDtos != null) {
			_LOGGER.info("Completed get all tasks");
			return Response.ok().entity(taskDtos).build();
		} else {
			Map<String, String> response = new HashMap<String, String>(1);
			response.put("message", "No tasks found");
			return Response.ok().entity(response).build();
		}
	}

	@Override
	@GET
	@Path("/attributes/{smartOcrId}")
	@Produces("application/json")
	public Response getAbzoobaResponseForId(@PathParam("smartOcrId") String smartOcrId) {
		_LOGGER.info("Request received for getting attributes");

		List<Map<String, Object>> attributes = attributeRequestProcessor.getAttributes(smartOcrId);

		return Response.ok().entity(attributes).build();
	}

	@Override
	@GET
	@Path("/ocrText/google/{smartOcrId}")
	@Produces("application/json")
	public Response getGoogleVisionResponse(@PathParam("smartOcrId") String smartOcrId) {
		if (smartOcrId != null && !smartOcrId.isEmpty()) {
			List<OcrImageDto> images = imageServiceProcessor.getImagesById(smartOcrId, true);
			return Response.ok().entity(images).build();
		} else {
			Map<String, String> response = new HashMap<String, String>(1);
			response.put("message", "Invalid Request");
			return Response.status(400).entity(response).build();
		}
	}

	@Override
	@PUT
	@Path("attributes/{smartOcrId}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response updateAbzoobaResponse(@PathParam("smartOcrId") String smartOcrId, List<Map<String, Object>> attributes) {
		_LOGGER.info("Request received for updating attributes");

		Map<String, String> updatedResponse = attributeRequestProcessor.updateAttributeByCrowd(smartOcrId, attributes);

		return Response.ok().entity(updatedResponse).build();
	}

	@Override
	@GET
	@Path("/upc/{smartOcrId}")
	@Produces("application/json")
	public Response getUpcForSmartId(@PathParam("smartOcrId") String smartOcrId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOcrTaskCreator(SmartOcrTaskCreator ocrTaskCreator) {
		this.ocrTaskCreator = ocrTaskCreator;
	}

	public void setImageServiceProcessor(ImageServiceProcessor imageServiceProcessor) {
		this.imageServiceProcessor = imageServiceProcessor;
	}

	public void setAttributeRequestProcessor(AbzoobaAttributeRequestProcessor attributeRequestProcessor) {
		this.attributeRequestProcessor = attributeRequestProcessor;
	}

	public void setTaskProvider(SmartOcrTaskProvider taskProvider) {
		this.taskProvider = taskProvider;
	}

}
