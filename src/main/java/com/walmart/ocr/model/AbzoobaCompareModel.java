package com.walmart.ocr.model;

import java.io.Serializable;

public class AbzoobaCompareModel implements Serializable {


	private String attributeName;
	private String valueFromCrowdSource;
	private String valueFromGivision;
	private String confidenceLevel;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}
	/**
	 * @param attributeName the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	/**
	 * @return the valueFromCrowdSource
	 */
	public String getValueFromCrowdSource() {
		return valueFromCrowdSource;
	}
	/**
	 * @param valueFromCrowdSource the valueFromCrowdSource to set
	 */
	public void setValueFromCrowdSource(String valueFromCrowdSource) {
		this.valueFromCrowdSource = valueFromCrowdSource;
	}
	/**
	 * @return the valueFromGivision
	 */
	public String getValueFromGivision() {
		return valueFromGivision;
	}
	/**
	 * @param valueFromGivision the valueFromGivision to set
	 */
	public void setValueFromGivision(String valueFromGivision) {
		this.valueFromGivision = valueFromGivision;
	}
	
	/**
	 * @return the confidenceLevel
	 */
	public String getConfidenceLevel() {
		return confidenceLevel;
	}
	/**
	 * @param confidenceLevel the confidenceLevel to set
	 */
	public void setConfidenceLevel(String confidenceLevel) {
		this.confidenceLevel = confidenceLevel;
	}

}
