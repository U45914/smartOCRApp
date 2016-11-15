/**
 * 
 */
package com.walmart.ocr.dao;

import java.io.Serializable;
import java.util.List;

import com.walmart.ocr.model.SmartOCRDataModel;

/**
 * @author U46591
 *
 */
public interface OcrInfoDao {

	Serializable createOcrData(SmartOCRDataModel ocrDataModel);
	SmartOCRDataModel findOcrDataById(Serializable id);
	List<SmartOCRDataModel> findAllOcrData();
	List<SmartOCRDataModel> findOcrDataByStatus(String status,int start, int limit);
	void updateOcrData(SmartOCRDataModel ocrDataModel);
}
