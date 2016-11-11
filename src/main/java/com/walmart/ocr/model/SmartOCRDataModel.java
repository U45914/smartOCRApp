/**
 * 
 */
package com.walmart.ocr.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ranai1
 *
 */
@Entity
@Table(name="OCR_INFO")
public class SmartOCRDataModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OCR_REQUEST_ID")
	private String ocrRequestId;// This will be a unique UUID
	
	@Column(name="GI_VISION_RESPONSE")
	private String givisionResponse;
	
	@Column(name="CROWD_SOURCE_RESPONSE")
	private String cloudSourceResponse;
	@Column(name="")
	private String absoobaRequestInfo;
	@Column(name="ABZOOBA_REQUEST_RESPONSE")
	private String absoobaResponse;
	@Column(name="CLOUD_SOURCE_USER_ID")
	private String cloudSourceUserId;
	@Column(name="STATUS")
	private String status;
	@Column(name="")
	private String imageUrls;
	@Column(name="")
	private byte[] image;
	@Column(name="")
	private Date imageUploadDate;
	@Column(name="")
	private Date imageProcessedDate;
	@Column(name="")
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
