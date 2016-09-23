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
			for (EntityAnnotation entity : annotateImageResponse.getTextAnnotations()) {
				textBuilder.append(entity.getDescription());
				textBuilder.append(" ");
			}
		}
		gVR.getTextDeatils().add(textBuilder.toString());
		List<EntityAnnotation> textAnnos = annotateImageResponse
				.getTextAnnotations();
		String fullText = textAnnos.get(0).getDescription();
		gVR.getTextDeatilsFormatted().add(FormatOCRText.processX(fullText, annotateImageResponse));
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
			ocrStringBuilder.append("Logo Details: ");
			ocrStringBuilder.append(logo);
			ocrStringBuilder.append(" ");
			count=0;
			}
			else{
				ocrStringBuilder1.append("Logo Details: ");
				ocrStringBuilder1.append(logo);
				ocrStringBuilder1.append(" ");
			}
		}
		count =1;
		
		for (String label : gVisionResponse.getLabelDetails()) {
			if(count==1){
			ocrStringBuilder.append("Label Details: ");
			ocrStringBuilder.append(label);
			ocrStringBuilder.append(" ");
			count=0;
			}
			else{
				ocrStringBuilder1.append("Label Details: ");
				ocrStringBuilder1.append(label);
				ocrStringBuilder1.append(" ");
				
			}
		}
		count =1;
		for (String text : gVisionResponse.getTextDeatils()) {
			if(count==1){
			ocrStringBuilder.append("Text Details: ");
			ocrStringBuilder.append(text);
			ocrStringBuilder.append(" ");
			count=0;
			}
			else{
				ocrStringBuilder1.append("Text Details: ");
				ocrStringBuilder1.append(text);
				ocrStringBuilder1.append(" ");
			}
		}
		count =1;
		for (String color : gVisionResponse.getColorDeatils()) {
			if(count==1){
			ocrStringBuilder.append("Color Details: ");
			ocrStringBuilder.append(color);
			ocrStringBuilder.append(" ");
			count=0;
			}
			else{
				ocrStringBuilder1.append("Color Details: ");
				ocrStringBuilder1.append(color);
				ocrStringBuilder1.append(" ");
			}
		}
		parseRequest.setFrontText(ocrStringBuilder.toString());
		String formattedText;
		if(null!=gVisionResponse.getTextDeatilsFormatted().get(0)){
			formattedText=gVisionResponse.getTextDeatilsFormatted().get(0);
			System.out.println("Front formattedText : " + formattedText);
			formattedText.replaceAll("\n", "<br/>");
			formattedText.replaceAll(" ", "&nbsp;");
			parseRequest.setFrontTextFormatted(formattedText);
			System.out.println("Front formattedText html : " + formattedText);
		}
		parseRequest.setBackText(ocrStringBuilder1.toString());
		if(null!=gVisionResponse.getTextDeatilsFormatted().get(1)){
			formattedText=gVisionResponse.getTextDeatilsFormatted().get(1);
			System.out.println("Back formattedText : " + formattedText);
			formattedText.replaceAll("\n", "<br/>");
			formattedText.replaceAll(" ", "&nbsp;");
			parseRequest.setBackTextFormatted(formattedText);
			System.out.println("Back formattedText html : " + formattedText);
		}
		parseRequest.setId(Long.toHexString(Double.doubleToLongBits(Math.random())));
		return parseRequest;
	}
}
