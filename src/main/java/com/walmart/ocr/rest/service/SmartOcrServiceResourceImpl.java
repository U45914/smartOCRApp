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

import com.walmart.ocr.job.SmartOcrTaskCreator;

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

	

	public void setOcrTaskCreator(SmartOcrTaskCreator ocrTaskCreator) {
		this.ocrTaskCreator = ocrTaskCreator;
	}

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
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	@GET
	@Path("/crowdsource/get/task")
	@Produces("application/json")
	public Response getSingleOCRTask() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	@GET
	@Path("/tasks/history")
	@Produces("application/json")
	public Response getOCRTasks(@QueryParam("status") @DefaultValue("*") String status, @QueryParam("start") @DefaultValue("0") String start,
			@QueryParam("max") @DefaultValue("10") String end, @QueryParam("sort") @DefaultValue("asc") String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	@GET
	@Path("/attributes/{smartOcrId}")
	@Produces("application/json")
	public Response getAbzoobaResponseForId(@PathParam("smartOcrId") String smartOcrId) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	@GET
	@Path("/ocrText/google/{smartOcrId}")
	@Produces("application/json")
	public Response getGoogleVisionResponse(@PathParam("smartOcrId") String smartOcrId) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	@PUT
	@Path("attributes/{smartOcrId}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response updateAbzoobaResponse(@PathParam("smartOcrId") String smartOcrId, List<Map<String, Object>> request) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	@GET
	@Path("/upc/{smartOcrId}")
	@Produces("application/json")
	public Response getUpcForSmartId(@PathParam("smartOcrId") String smartOcrId) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
