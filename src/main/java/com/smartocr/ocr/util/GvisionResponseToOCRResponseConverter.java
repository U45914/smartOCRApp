package com.smartocr.ocr.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.ColorInfo;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.ImageProperties;
import com.smartocr.ocr.model.GVisionResponse;
import com.smartocr.ocr.model.ParseRequest;

public class GvisionResponseToOCRResponseConverter {
	public static final String BR = "<br/>";
	public static GVisionResponse convert(BatchAnnotateImagesResponse bAIR) {
		GVisionResponse gVR = new GVisionResponse();
		List<String> logos = new ArrayList<String>();
		gVR.setLogoDetails(logos);
		List<String> labels = new ArrayList<String>();
		gVR.setLabelDetails(labels);
		List<String> texts = new ArrayList<String>();
		gVR.setTextDeatils(texts);
		List<String> formatedTexts = new ArrayList<String>();
		gVR.setTextDeatilsFormatted(formatedTexts);
		List<String> colors = new ArrayList<String>();
		gVR.setColorDeatils(colors);

		for (AnnotateImageResponse annotateImageResponse : bAIR.getResponses()) {
			getLogoDeatils(annotateImageResponse, gVR);
			getLabelDeatils(annotateImageResponse, gVR);
			getTextDeatils(annotateImageResponse, gVR);
			getColorDetails(annotateImageResponse, gVR);
		}
		return gVR;
	}

	public static GVisionResponse convertToParseequest(BatchAnnotateImagesResponse bAIR) {

		GVisionResponse gVR = new GVisionResponse();
		List<String> logos = new ArrayList<String>();
		gVR.setLogoDetails(logos);
		List<String> labels = new ArrayList<String>();
		gVR.setLabelDetails(labels);
		List<String> texts = new ArrayList<String>();
		gVR.setTextDeatils(texts);
		List<String> colors = new ArrayList<String>();
		gVR.setTextDeatils(colors);

		for (AnnotateImageResponse annotateImageResponse : bAIR.getResponses()) {
			getLogoDeatils(annotateImageResponse, gVR);
			getLabelDeatils(annotateImageResponse, gVR);
			getTextDeatils(annotateImageResponse, gVR);
			getColorDetails(annotateImageResponse, gVR);
		}
		return gVR;
	}

	private static void getColorDetails(AnnotateImageResponse annotateImageResponse, GVisionResponse gVR) {
		ImageProperties imagePropertiesAnnotation = annotateImageResponse.getImagePropertiesAnnotation();
		if (null != imagePropertiesAnnotation) {
			ColorInfo colorInfo = imagePropertiesAnnotation.getDominantColors().getColors().get(0);
			ColorUtils colorUtils = new ColorUtils();
			String myColor = colorUtils.getColorNameFromRgb(Math.round(colorInfo.getColor().getRed()), Math.round(colorInfo.getColor().getGreen()),
					Math.round(colorInfo.getColor().getBlue()));

			gVR.getColorDeatils().add(myColor);
		}
	}

	private static void getTextDeatils(AnnotateImageResponse annotateImageResponse, GVisionResponse gVR) {
		StringBuilder textBuilder = new StringBuilder();
		if (null != annotateImageResponse.getTextAnnotations()) {
//			for (EntityAnnotation entity : annotateImageResponse.getTextAnnotations()) {
//				textBuilder.append(entity.getDescription());
//				textBuilder.append(" ");
//			}
			gVR.getTextDeatils().add(annotateImageResponse.getTextAnnotations().get(0).getDescription().replaceAll("\n", " "));
		}
		
		List<EntityAnnotation> textAnnos = annotateImageResponse.getTextAnnotations();
		String fullText = textAnnos.get(0).getDescription();
		gVR.getTextDeatilsFormatted().add(processOCRText(fullText, annotateImageResponse));
	}

	private static String processOCRText(String text, AnnotateImageResponse annotateImageResponse) {

		List<String> processedText = FormatOCRText.processGoogleResponse(annotateImageResponse);
		StringBuilder sentenceBuilder = new StringBuilder();

		for (String sentence : processedText) {
			sentenceBuilder.append(sentence);
			sentenceBuilder.append(" ");
		}
		return sentenceBuilder.toString();
	}

	private static void getLabelDeatils(AnnotateImageResponse annotateImageResponse, GVisionResponse gVR) {
		StringBuilder labelBuilder = new StringBuilder();
		if (null != annotateImageResponse.getLabelAnnotations()) {
			for (EntityAnnotation entity : annotateImageResponse.getLabelAnnotations()) {
				labelBuilder.append(entity.getDescription());
				labelBuilder.append(" ");
			}
		}
		gVR.getLabelDetails().add(labelBuilder.toString());
	}

	private static void getLogoDeatils(AnnotateImageResponse annotateImageResponse, GVisionResponse gVR) {
		StringBuilder logoBuilder = new StringBuilder();

		if (null != annotateImageResponse.getLogoAnnotations()) {
			for (EntityAnnotation entity : annotateImageResponse.getLogoAnnotations()) {
				logoBuilder.append(entity.getDescription());
				logoBuilder.append(" ");
			}
		}

		gVR.getLogoDetails().add(logoBuilder.toString());
	}

	public static String toOCRString(GVisionResponse gVisionResponse) {
		StringBuilder ocrStringBuilder = new StringBuilder();
		ocrStringBuilder.append("LogoDetails: ");
		for (String logo : gVisionResponse.getLogoDetails()) {
			ocrStringBuilder.append(logo.replaceAll("\n", BR));
			ocrStringBuilder.append(" ");
		}
		ocrStringBuilder.append("LabelDetails: ");
		for (String label : gVisionResponse.getLabelDetails()) {
			ocrStringBuilder.append(label.replaceAll("\n", BR));
			ocrStringBuilder.append(" ");
		}
		ocrStringBuilder.append("TextDetails: ");
		for (String text : gVisionResponse.getTextDeatils()) {
			ocrStringBuilder.append(text.replaceAll("\n", BR));
			ocrStringBuilder.append(" ");
		}
		return ocrStringBuilder.toString();
	}

	public static ParseRequest toParseRequest(GVisionResponse gVisionResponse) {

		ParseRequest parseRequest = new ParseRequest();
		StringBuilder ocrStringBuilder = new StringBuilder();
		StringBuilder ocrStringBuilder1 = new StringBuilder();
		int count = 1;
		for (String logo : gVisionResponse.getLogoDetails()) {
			if (count == 1) {
				ocrStringBuilder.append("Logo Details: ");
				ocrStringBuilder.append(logo.replaceAll("\n", BR));
				ocrStringBuilder.append(" ");
				count = 0;
			} else {
				ocrStringBuilder1.append("Logo Details: ");
				ocrStringBuilder1.append(logo.replaceAll("\n", BR));
				ocrStringBuilder1.append(" ");
			}
		}
		count = 1;

		for (String label : gVisionResponse.getLabelDetails()) {
			if (count == 1) {
				ocrStringBuilder.append("Label Details: ");
				
				ocrStringBuilder.append(label.replaceAll("\n", BR));
				ocrStringBuilder.append(" ");
				count = 0;
			} else {
				ocrStringBuilder1.append("Label Details: ");
				ocrStringBuilder1.append(label.replaceAll("\n", BR));
				ocrStringBuilder1.append(" ");

			}
		}
		

		ocrStringBuilder.append("Text Details: ");
		ocrStringBuilder1.append("Text Details: ");

		String formattedText;
		if (null != gVisionResponse.getTextDeatilsFormatted().get(0)) {
			formattedText = gVisionResponse.getTextDeatilsFormatted().get(0);

			ocrStringBuilder.append(formattedText);
			formattedText = formattedText.replaceAll("\n", BR);
			formattedText = formattedText.replaceAll(" ", "&nbsp;");
		}
		if (gVisionResponse.getTextDeatilsFormatted().size() > 1) {
			if (null != gVisionResponse.getTextDeatilsFormatted().get(1)) {
				formattedText = gVisionResponse.getTextDeatilsFormatted().get(1);
				ocrStringBuilder1.append(formattedText);
				formattedText = formattedText.replaceAll("\n", BR);
				formattedText = formattedText.replaceAll(" ", "&nbsp;");
			}
		}
		count = 1;
		for (String color : gVisionResponse.getColorDeatils()) {
			if (count == 1) {
				ocrStringBuilder.append("Color Details: ");
				ocrStringBuilder.append(color.replaceAll("\n", BR));
				ocrStringBuilder.append(" ");
				count = 0;
			} else {
				ocrStringBuilder1.append("Color Details: ");
				ocrStringBuilder1.append(color.replaceAll("\n", BR));
				ocrStringBuilder1.append(" ");
			}
		}

		parseRequest.setFrontText(ocrStringBuilder.toString().replaceAll("\n", BR));
		parseRequest.setBackText(ocrStringBuilder1.toString().replaceAll("\n", BR));
		parseRequest.setId(Long.toHexString(Double.doubleToLongBits(Math.random())));
		return parseRequest;
	}
	
	public static ParseRequest parseResponse(GVisionResponse gVisionResponse) {
		ParseRequest parseRequest = new ParseRequest();
		int size = gVisionResponse.getTextDeatils().size();
		for (int index = 0; index < size; index++) {
			StringBuilder parseText = new StringBuilder();
			parseText.append("Logo Details : ");
			parseText.append(gVisionResponse.getLogoDetails().get(index).toString());
			parseText.append(BR);
			
			parseText.append("Label Details : ");
			parseText.append(gVisionResponse.getLabelDetails().get(index).toString());
			parseText.append(BR);
			
			parseText.append("Text Details : ");
			parseText.append(gVisionResponse.getTextDeatils().get(index).toString());
			parseText.append(BR);
			
			parseText.append("Color Details : ");
			parseText.append(gVisionResponse.getColorDeatils().get(index).toString());
			parseText.append(BR);
//			parseText.toString().replaceAll("\n", "&nbsp;");
//			parseText.toString().replaceAll(" ", "&nbsp;");
			if((size>=2 && index==0) || (size==1 && index==0)){
				//set front text
				parseRequest.setFrontText(parseText.toString());
			}
			else if(size>=2 && index==1){
				//set back side text
				parseRequest.setBackText(parseText.toString());
			}
			else if(size>2){
				if(index==2)
					parseRequest.setLeftSideText(parseText.toString());		//Set left side text
				if(index==3)
					parseRequest.setRightSideText(parseText.toString());	//Set right side text
				if(index==4)
					parseRequest.setTopSideText(parseText.toString());	//Set top side text
				if(index==5)
					parseRequest.setBottomSideText(parseText.toString());	//Set bottom side text
			}
		}
		
		parseRequest.setId(Long.toHexString(Double.doubleToLongBits(Math.random())));
		
		return parseRequest;
	}
	
	final static ObjectMapper mapper = new ObjectMapper();
	public static String parseRequestObjectTOJsonString(ParseRequest request) {
		String response = null; 
		try {
			response = mapper.writeValueAsString(request);
		} catch (Exception e) {
			response = "{}";
			e.printStackTrace();
		}
		
		return response;
	}
}
