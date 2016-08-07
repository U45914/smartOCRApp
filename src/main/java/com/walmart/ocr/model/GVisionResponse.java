package com.walmart.ocr.model;

import java.util.List;

public class GVisionResponse {
	private List<String> logoDetails;
	private List<String> labelDetails;
	private List<String> textDeatils;
	public List<String> getLogoDetails() {
		return logoDetails;
	}
	public void setLogoDetails(List<String> logoDetails) {
		this.logoDetails = logoDetails;
	}
	public List<String> getLabelDetails() {
		return labelDetails;
	}
	public void setLabelDetails(List<String> labelDetails) {
		this.labelDetails = labelDetails;
	}
	public List<String> getTextDeatils() {
		return textDeatils;
	}
	public void setTextDeatils(List<String> textDeatils) {
		this.textDeatils = textDeatils;
	}
	

}
