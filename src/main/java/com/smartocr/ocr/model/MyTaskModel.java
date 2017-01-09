/**
 * 
 */
package com.smartocr.ocr.model;

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

	private String upc;
	private String smartId;
	private byte[] image;
	private byte [] backImage;
	private String imageName;
	private List<AbzoobaCompareModel> abzoobaResponse;
	private byte[] leftImage;
	private byte [] rightImage;
	private byte[] topImage;
	private byte[] bottomImage;

	
	/**
	 * @return the upc
	 */
	public String getUpc() {
		return upc;
	}

	/**
	 * @param upc the upc to set
	 */
	public void setUpc(String upc) {
		this.upc = upc;
	}

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

	public byte[] getLeftImage() {
		return leftImage;
	}

	public void setLeftImage(byte[] leftImage) {
		this.leftImage = leftImage;
	}

	public byte[] getRightImage() {
		return rightImage;
	}

	public void setRightImage(byte[] rightImage) {
		this.rightImage = rightImage;
	}

	public byte[] getTopImage() {
		return topImage;
	}

	public void setTopImage(byte[] topImage) {
		this.topImage = topImage;
	}

	public byte[] getBottomImage() {
		return bottomImage;
	}

	public void setBottomImage(byte[] bottomImage) {
		this.bottomImage = bottomImage;
	}

	/**
	 * @return the backImage
	 */
	public byte[] getBackImage() {
		return backImage;
	}

	/**
	 * @param backImage the backImage to set
	 */
	public void setBackImage(byte[] backImage) {
		this.backImage = backImage;
	}

	

}
