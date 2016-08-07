package com.walmart.ocr.util;

import java.util.ArrayList;
import java.util.List;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.walmart.ocr.model.GVisionResponse;

public class GvisionResponseToOCRResponseConverter {
	public static GVisionResponse convert(BatchAnnotateImagesResponse bAIR) {
		GVisionResponse gVR = new GVisionResponse();
		List<String> logos = new ArrayList<String>();
		gVR.setLogoDetails(logos);
		List<String> labels = new ArrayList<String>();
		gVR.setLabelDetails(labels);
		List<String> texts = new ArrayList<String>();
		gVR.setTextDeatils(texts);

		for (AnnotateImageResponse annotateImageResponse : bAIR.getResponses()) {
			getLogoDeatils(annotateImageResponse, gVR);
			getLabelDeatils(annotateImageResponse, gVR);
			getTextDeatils(annotateImageResponse, gVR);
		}
		return gVR;
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
}
