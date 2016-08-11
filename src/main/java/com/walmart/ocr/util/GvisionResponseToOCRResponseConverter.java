package com.walmart.ocr.util;

import java.util.ArrayList;
import java.util.List;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.ColorInfo;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.ImageProperties;
import com.walmart.ocr.model.GVisionResponse;
import com.walmart.ocr.model.ParseRequest;

public class GvisionResponseToOCRResponseConverter {
	public static GVisionResponse convert(BatchAnnotateImagesResponse bAIR) {
		GVisionResponse gVR = new GVisionResponse();
		List<String> logos = new ArrayList<String>();
		gVR.setLogoDetails(logos);
		List<String> labels = new ArrayList<String>();
		gVR.setLabelDetails(labels);
		List<String> texts = new ArrayList<String>();
		gVR.setTextDeatils(texts);
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
	
	public static  GVisionResponse convertToParseequest(BatchAnnotateImagesResponse bAIR) {
		
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
	private static void getColorDetails(AnnotateImageResponse annotateImageResponse, GVisionResponse gVR){
		ImageProperties imagePropertiesAnnotation = annotateImageResponse
				.getImagePropertiesAnnotation();
		if (null != imagePropertiesAnnotation) {
			ColorInfo colorInfo = imagePropertiesAnnotation
					.getDominantColors().getColors().get(0);
			ColorUtils colorUtils = new ColorUtils();
			String myColor = colorUtils.getColorNameFromRgb(
					Math.round(colorInfo.getColor().getRed()),
					Math.round(colorInfo.getColor().getGreen()),
					Math.round(colorInfo.getColor().getBlue()));
			
			gVR.getColorDeatils().add(myColor);
		}
	}

	
	private static void getTextDeatils(AnnotateImageResponse annotateImageResponse, GVisionResponse gVR) {
		StringBuilder textBuilder = new StringBuilder();
		if (null != annotateImageResponse.getTextAnnotations()) {
			//Google API gives 1st element as full test & reset with coordinates .
			//So take only 1st element now.
			if(null != annotateImageResponse.getTextAnnotations().get(0)){
				textBuilder.append(annotateImageResponse.getTextAnnotations()
						.get(0).getDescription());
				textBuilder.append(" ");
			}
		}
		gVR.getTextDeatils().add(textBuilder.toString());
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
			ocrStringBuilder.append(logo);
			ocrStringBuilder.append(" ");
		}
		ocrStringBuilder.append("LabelDetails: ");
		for (String label : gVisionResponse.getLabelDetails()) {
			ocrStringBuilder.append(label);
			ocrStringBuilder.append(" ");
		}
		ocrStringBuilder.append("TextDetails: ");
		for (String text : gVisionResponse.getTextDeatils()) {
			ocrStringBuilder.append(text);
			ocrStringBuilder.append(" ");
		}
		return ocrStringBuilder.toString();
	}
	public static ParseRequest toParseRequest(GVisionResponse gVisionResponse) {
		
		ParseRequest parseRequest = new ParseRequest();
		StringBuilder ocrStringBuilder = new StringBuilder();
		StringBuilder ocrStringBuilder1 = new StringBuilder();
		int count =1;
		for (String logo : gVisionResponse.getLogoDetails()) {
			if(count==1){
			ocrStringBuilder.append("LogoDetails: ");
			ocrStringBuilder.append(logo);
			ocrStringBuilder.append(" ");
			count=0;
			}
			else{
				ocrStringBuilder1.append("LogoDetails: ");
				ocrStringBuilder1.append(logo);
				ocrStringBuilder1.append(" ");
			}
		}
		count =1;
		
		for (String label : gVisionResponse.getLabelDetails()) {
			if(count==1){
			ocrStringBuilder.append("LabelDetails: ");
			ocrStringBuilder.append(label);
			ocrStringBuilder.append(" ");
			count=0;
			}
			else{
				ocrStringBuilder1.append("LabelDetails: ");
				ocrStringBuilder1.append(label);
				ocrStringBuilder1.append(" ");
				
			}
		}
		count =1;
		for (String text : gVisionResponse.getTextDeatils()) {
			if(count==1){
			ocrStringBuilder.append("TextDetails: ");
			ocrStringBuilder.append(text);
			ocrStringBuilder.append(" ");
			count=0;
			}
			else{
				ocrStringBuilder1.append("TextDetails: ");
				ocrStringBuilder1.append(text);
				ocrStringBuilder1.append(" ");
			}
		}
		count =1;
		for (String color : gVisionResponse.getColorDeatils()) {
			if(count==1){
			ocrStringBuilder.append("ColorDetails: ");
			ocrStringBuilder.append(color);
			ocrStringBuilder.append(" ");
			count=0;
			}
			else{
				ocrStringBuilder1.append("ColorDetails: ");
				ocrStringBuilder1.append(color);
				ocrStringBuilder1.append(" ");
			}
		}
		parseRequest.setFrontText(ocrStringBuilder.toString());
		parseRequest.setBackText(ocrStringBuilder1.toString());
		parseRequest.setId(Long.toHexString(Double.doubleToLongBits(Math.random())));
		return parseRequest;
	}
}
