package com.walmart.ocr.model;

public class ParseRequest {
	private String smartOcrId;
	private String id;
	private String FrontText;
	private String BackText;
	private String FrontTextFormatted;
	private String BackTextFormatted;
	private String leftSideText;
	private String rightSideText;
	private String topSideText;
	private String bottomSideText;
	
	public String getSmartOcrId() {
		return smartOcrId;
	}
	public void setSmartOcrId(String smartOcrId) {
		this.smartOcrId = smartOcrId;
	}
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
	public String getFrontTextFormatted() {
		return FrontTextFormatted;
	}
	public void setFrontTextFormatted(String frontTextFormatted) {
		FrontTextFormatted = frontTextFormatted;
	}
	public String getBackTextFormatted() {
		return BackTextFormatted;
	}
	public void setBackTextFormatted(String backTextFormatted) {
		BackTextFormatted = backTextFormatted;
	}
	public String getLeftSideText() {
		return leftSideText;
	}
	public void setLeftSideText(String leftSideText) {
		this.leftSideText = leftSideText;
	}
	public String getRightSideText() {
		return rightSideText;
	}
	public void setRightSideText(String rightSideText) {
		this.rightSideText = rightSideText;
	}
	public String getTopSideText() {
		return topSideText;
	}
	public void setTopSideText(String topSideText) {
		this.topSideText = topSideText;
	}
	public String getBottomSideText() {
		return bottomSideText;
	}
	public void setBottomSideText(String bottomSideText) {
		this.bottomSideText = bottomSideText;
	}
	
	

}
