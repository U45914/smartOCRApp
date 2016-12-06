package com.walmart.ocr.job;

public class OcrImageDto {

	private String smartOcrId;
	private byte[] image;
	private String ocrText;
	private String view;

	public String getSmartOcrId() {
		return smartOcrId;
	}

	public void setSmartOcrId(String smartOcrId) {
		this.smartOcrId = smartOcrId;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getOcrText() {
		return ocrText;
	}

	public void setOcrText(String ocrText) {
		this.ocrText = ocrText;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

}
