/**
 * 
 */
package com.smartocr.ocr.dao;

import java.io.Serializable;
import java.util.List;

import com.smartocr.ocr.model.SmartOCRDataModel;

/**
 * @author U46591
 *
 */
public interface OcrInfoDao {

	Serializable createOcrData(SmartOCRDataModel ocrDataModel);
	SmartOCRDataModel findOcrDataById(Serializable id);
	List<SmartOCRDataModel> findAllOcrData();
	List<SmartOCRDataModel> findOcrDataByStatus(String status,int start, int limit, String orderBy);
	void updateOcrData(SmartOCRDataModel ocrDataModel);
	void clearExistingData();
}
