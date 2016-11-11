/**
 * 
 */
package com.walmart.ocr.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ranai1
 *
 */
public class SmartOCRDataModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ocrRequestId;// This will be a unique UUID
	
	private String givisionResponse;
	private String cloudSourceResponse;
	
	private String absoobaRequestInfo;
	private String absoobaResponse;
	private String cloudSourceUserId;
	private String status;
	
	private String imageUrls;
	private byte[] image;
	
	private Date imageUploadDate;
	private Date imageProcessedDate;
	private Date cloudeSourceSubmitDate;
	
	
	public SmartOCRDataModel() {
	}


	/**
	 * @return the ocrRequestId
	 */
	public String getOcrRequestId() {
		return ocrRequestId;
	}


	/**
	 * @param ocrRequestId the ocrRequestId to set
	 */
	public void setOcrRequestId(String ocrRequestId) {
		this.ocrRequestId = ocrRequestId;
	}


	/**
	 * @return the givisionResponse
	 */
	public String getGivisionResponse() {
		return givisionResponse;
	}


	/**
	 * @param givisionResponse the givisionResponse to set
	 */
	public void setGivisionResponse(String givisionResponse) {
		this.givisionResponse = givisionResponse;
	}


	/**
	 * @return the cloudSourceResponse
	 */
	public String getCloudSourceResponse() {
		return cloudSourceResponse;
	}


	/**
	 * @param cloudSourceResponse the cloudSourceResponse to set
	 */
	public void setCloudSourceResponse(String cloudSourceResponse) {
		this.cloudSourceResponse = cloudSourceResponse;
	}


	/**
	 * @return the absoobaRequestInfo
	 */
	public String getAbsoobaRequestInfo() {
		return absoobaRequestInfo;
	}


	/**
	 * @param absoobaRequestInfo the absoobaRequestInfo to set
	 */
	public void setAbsoobaRequestInfo(String absoobaRequestInfo) {
		this.absoobaRequestInfo = absoobaRequestInfo;
	}


	/**
	 * @return the absoobaResponse
	 */
	public String getAbsoobaResponse() {
		return absoobaResponse;
	}


	/**
	 * @param absoobaResponse the absoobaResponse to set
	 */
	public void setAbsoobaResponse(String absoobaResponse) {
		this.absoobaResponse = absoobaResponse;
	}


	/**
	 * @return the cloudSourceUserId
	 */
	public String getCloudSourceUserId() {
		return cloudSourceUserId;
	}


	/**
	 * @param cloudSourceUserId the cloudSourceUserId to set
	 */
	public void setCloudSourceUserId(String cloudSourceUserId) {
		this.cloudSourceUserId = cloudSourceUserId;
	}


	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * @return the imageUrls
	 */
	public String getImageUrls() {
		return imageUrls;
	}


	/**
	 * @param imageUrls the imageUrls to set
	 */
	public void setImageUrls(String imageUrls) {
		this.imageUrls = imageUrls;
	}


	/**
	 * @return the image
	 */
	public byte[] getImage() {
		return image;
	}


	/**
	 * @param image the image to set
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}


	/**
	 * @return the imageUploadDate
	 */
	public Date getImageUploadDate() {
		return imageUploadDate;
	}


	/**
	 * @param imageUploadDate the imageUploadDate to set
	 */
	public void setImageUploadDate(Date imageUploadDate) {
		this.imageUploadDate = imageUploadDate;
	}


	/**
	 * @return the imageProcessedDate
	 */
	public Date getImageProcessedDate() {
		return imageProcessedDate;
	}


	/**
	 * @param imageProcessedDate the imageProcessedDate to set
	 */
	public void setImageProcessedDate(Date imageProcessedDate) {
		this.imageProcessedDate = imageProcessedDate;
	}


	/**
	 * @return the cloudeSourceSubmitDate
	 */
	public Date getCloudeSourceSubmitDate() {
		return cloudeSourceSubmitDate;
	}


	/**
	 * @param cloudeSourceSubmitDate the cloudeSourceSubmitDate to set
	 */
	public void setCloudeSourceSubmitDate(Date cloudeSourceSubmitDate) {
		this.cloudeSourceSubmitDate = cloudeSourceSubmitDate;
	}
	
	
	

	
}
