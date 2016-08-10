package com.walmart.ocr.model;

public class ParseRequest {
	private String id;
	private String FrontText;
	private String BackText;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFrontText() {
		return FrontText;
	}
	public void setFrontText(String frontText) {
		FrontText = frontText;
	}
	public String getBackText() {
		return BackText;
	}
	public void setBackText(String backText) {
		BackText = backText;
	}
	
	

}
