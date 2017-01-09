package com.smartocr.ocr.job;

import java.util.List;
import java.util.Map;

import com.smartocr.ocr.model.AbzoobaCompareModel;
import com.smartocr.ocr.model.ParseRequest;

public class TaskDto {

	private String smartOcrId;
	private List<OcrImageDto> images;
	private List<Map<String, Object>> attributes;
	private List<Map<String, Object>> editedAttributes;
	private ParseRequest ocrRequest;
	private List<AbzoobaCompareModel> attributeDiff;

	public String getSmartOcrId() {
		return smartOcrId;
	}

	public void setSmartOcrId(String smartOcrId) {
		this.smartOcrId = smartOcrId;
	}

	public List<OcrImageDto> getImages() {
		return images;
	}

	public void setImages(List<OcrImageDto> images) {
		this.images = images;
	}

	public List<Map<String, Object>> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Map<String, Object>> attributes) {
		this.attributes = attributes;
	}

	public List<Map<String, Object>> getEditedAttributes() {
		return editedAttributes;
	}

	public void setEditedAttributes(List<Map<String, Object>> editedAttributes) {
		this.editedAttributes = editedAttributes;
	}

	public ParseRequest getOcrRequest() {
		return ocrRequest;
	}

	public void setOcrRequest(ParseRequest ocrRequest) {
		this.ocrRequest = ocrRequest;
	}

	public List<AbzoobaCompareModel> getAttributeDiff() {
		return attributeDiff;
	}

	public void setAttributeDiff(List<AbzoobaCompareModel> attributeDiff) {
		this.attributeDiff = attributeDiff;
	}

}
