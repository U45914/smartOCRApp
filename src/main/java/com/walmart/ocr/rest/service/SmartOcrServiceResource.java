/**
 * 
 */
package com.walmart.ocr.rest.service;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

/**
 * @author Rahul
 * 
 */
@Component
public interface SmartOcrServiceResource {

	@POST
	@Path("/uploadImages")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadProductImages(@Context HttpServletRequest request);

	@GET
	@Path("/images/{smartOcrId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getImagesBySmartId(@PathParam("smartOcrId") String smartOcrId);

	@GET
	@Path("/crowdsource/get/task")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSingleOCRTask();

	@GET
	@Path("/tasks/history")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOCRTasks(@QueryParam("status") @DefaultValue("*") String status, @QueryParam("start") @DefaultValue("0") String start,
			@QueryParam("max") @DefaultValue("10") String end, @QueryParam("sort") @DefaultValue("asc") String orderBy);

	@GET
	@Path("/attributes/{smartOcrId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAbzoobaResponseForId(@PathParam("smartOcrId") String smartOcrId);

	@GET
	@Path("/ocrText/google/{smartOcrId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGoogleVisionResponse(@PathParam("smartOcrId") String smartOcrId);

	@PUT
	@Path("attributes/{smartOcrId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateAbzoobaResponse(@PathParam("smartOcrId") String smartOcrId, List<Map<String, Object>> request);

	@GET
	@Path("/upc/{smartOcrId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUpcForSmartId(@PathParam("smartOcrId") String smartOcrId);

}
