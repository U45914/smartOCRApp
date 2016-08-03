package com.walmart.ocr.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.walmart.ocr.model.ParseRequest;

@Path("/abzoobaParse")
public class AbzoobaParserResource {

	private static final Logger logger = Logger.getLogger(AbzoobaParserResource.class);

	@POST
	@Path("/parseText")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> uploadFile1(ParseRequest parseInput) {
		String output = null;
		Map<String, Object> myMap=null;
		try {
			logger("Parsing With  Abzooba ....");
			Client client = Client.create();

			WebResource webResource = client.resource("http://52.23.170.75:5000/model1");
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(parseInput);
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonInString);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			output = response.getEntity(String.class);
			myMap= new HashMap<String, Object>();
			myMap=JsonstringToMap.jsonString2Map(output);
			
			logger("Output from Server .... ");
			logger(output);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return myMap;
	}

	void logger(String log) {
		logger.debug(log);
	}
}