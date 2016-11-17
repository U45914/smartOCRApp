package com.walmart.ocr.util;

/**
 * 
 * @author U46591
 *
 */
public class HQLQueryConstants {

	public static final String UPDATE_QUERY = "UPDATE SmartOCRDataModel socr "
			+ "SET socr.givisionResponse=?,socr.absoobaRequestInfo=?,socr.absoobaResponse=?,socr.crowdSourceUserId=?,"
			+ "socr.status=?,socr.imageUrls =?, socr.image=?,socr.imageUploadDate=?,socr.imageProcessedDate=?,"
			+ "socr.cloudeSourceSubmitDate=?,socr.crowdSourceResponse=?,socr.abzoobaResponse2=?"
			+ "socr.backImage=? WHERE socr.ocrRequestId = ?";

	/**
	 * To prevent instantiation
	 */
	private HQLQueryConstants() {

	}
}