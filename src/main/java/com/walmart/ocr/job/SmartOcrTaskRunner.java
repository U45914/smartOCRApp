/**
 * 
 */
package com.walmart.ocr.job;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.walmart.ocr.dao.OcrInfoDao;
import com.walmart.ocr.model.ImageBarcodeRunner;
import com.walmart.ocr.model.ParseRequest;
import com.walmart.ocr.model.SmartOCRDataModel;
import com.walmart.ocr.util.GvisionResponseToOCRResponseConverter;
import com.walmart.ocr.util.SmartOcrFileUtils;

/**
 * @author Rahul
 *
 */
public class SmartOcrTaskRunner implements Callable<String> {
	private static final Logger _LOGGER = Logger.getLogger(SmartOcrTaskRunner.class);
	
	private static GoogleVisionRequestProcessor ocrRequestProcessor = new GoogleVisionRequestProcessor();
	
	private final static String ERROR = "Error";
	
	
	private OcrInfoDao ocrInfoDao;
	private List<File> imageFiles;
	private String smartOcrId;
	private SmartOCRDataModel ocrDataModel;
	

	public SmartOcrTaskRunner(String ocrId, List<File> imageFiles, OcrInfoDao ocrInfoDao, SmartOCRDataModel ocrDataModel) {
		this.smartOcrId = ocrId;
		this.ocrInfoDao = ocrInfoDao;
		this.imageFiles = imageFiles;
		this.ocrDataModel = ocrDataModel;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public String call() throws Exception {
		_LOGGER.info("OCR Processing started");
		
		ImageBarcodeRunner barcodeRunner = new ImageBarcodeRunner(imageFiles);
		String resultBarcode = barcodeRunner.call();
		
		String upcNumber = SmartOcrFileUtils.getUpcFromFileName(imageFiles.get(0).getName());
		
		
		ParseRequest smartOcrRequest = null;
		try {
			smartOcrRequest = ocrRequestProcessor.readOcrTextFromImages(imageFiles);

			smartOcrRequest.setId(upcNumber);
			smartOcrRequest.setSmartOcrId(smartOcrId);
			smartOcrRequest.setExtractedUpc(resultBarcode);
		
			ocrDataModel.setGivisionResponse(GvisionResponseToOCRResponseConverter.parseRequestObjectTOJsonString(smartOcrRequest));
			
		} catch (Exception e) {
			_LOGGER.error("Exception while using google service for OCR processing", e);
			return ERROR;
		}
		_LOGGER.info("Update database with latest OCRDataModel : started");
		updateOcrModelToDatabase();
		_LOGGER.info("Update database with latest OCRDataModel : completed");
		
		try {
			
			FileUtils.cleanDirectory(new File(imageFiles.get(0).getParent()));
		} catch(Exception e) {
			_LOGGER.error("Failed to delete processed images from directory", e);
		}
		
		// TODO: From here start abzooba request processing
		
		
		return this.smartOcrId;
	}
	
	private void updateOcrModelToDatabase() {
		ocrInfoDao.updateOcrData(ocrDataModel);
	}

}
