/**
 * 
 */
package com.walmart.ocr.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author ranai1
 *
 */
public class MyTaskModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String smartId;
	private byte[] image;
	private String imageName;
	private List<AbzoobaCompareModel> abzoobaResponse;

	/**
	 * @return the smartId
	 */
	public String getSmartId() {
		return smartId;
	}

	/**
	 * @param smartId
	 *            the smartId to set
	 */
	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	/**
	 * @return the image
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}

	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @param imageName
	 *            the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * @return the abzoobaResponse
	 */
	public List<AbzoobaCompareModel> getAbzoobaResponse() {
		return abzoobaResponse;
	}

	/**
	 * @param abzoobaResponse the abzoobaResponse to set
	 */
	public void setAbzoobaResponse(List<AbzoobaCompareModel> abzoobaResponse) {
		this.abzoobaResponse = abzoobaResponse;
	}

	

}
