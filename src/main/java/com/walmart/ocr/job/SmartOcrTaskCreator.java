package com.walmart.ocr.job;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.walmart.ocr.dao.OcrInfoDao;
import com.walmart.ocr.model.SmartOCRDataModel;
import com.walmart.ocr.rabbit.provider.RabbitMQProvider;
import com.walmart.ocr.util.ImageToByteConverter;
import com.walmart.ocr.util.MessageConverter;
import com.walmart.ocr.util.SmartOCRStatus;
import com.walmart.ocr.util.SmartOcrFileUtils;

public class SmartOcrTaskCreator {
	
	private static final Logger _LOGGER = Logger.getLogger(SmartOcrTaskCreator.class);

	private static final String FILE_UPLOAD_PATH = "ImagesToProcess";
	
	@Autowired
	private OcrInfoDao ocrInfoDao;
	
	@Autowired
	private ThreadPoolTaskExecutor ocrTaskExecutor;
	
	@Autowired
	RabbitMQProvider rabbitMqProvider;


	@SuppressWarnings("unchecked")
	public String submitJob(HttpServletRequest request) {
		_LOGGER.info("Request recived for processing OCR @ " + System.currentTimeMillis());
		// Saving files to disc location

		_LOGGER.info("******** Save Files *******");
		String imagesDir = getPathToSaveFiles();
		File file = new File(imagesDir);
		file.mkdirs();
		
		SmartOcrFileUtils.saveFiles(request, imagesDir);
		
		
		List<File> imageFiles = new ArrayList<File>();
		imageFiles = (List<File>) FileUtils.listFiles(file, null, false);
		
		SmartOcrFileUtils.sortFiles(imageFiles);
		
		SmartOCRDataModel ocrDataModel = new SmartOCRDataModel();
		ocrDataModel.setImageUrls(imagesDir);
		ocrDataModel.setStatus(SmartOCRStatus.PRODUCT_IMAGE_PARSE_REQUEST_RECEIVED);
		ocrDataModel = saveImagesToDatabase(imageFiles, ocrDataModel);		
		
		// Save initial object to database and get a ID
		Integer ocrId = (Integer) ocrInfoDao.createOcrData(ocrDataModel);
		
		String smartId = MessageConverter.getSmartOCRId(ocrId);
		// Send ocrId to queue
		rabbitMqProvider.sendMessage(smartId);
		
		SmartOcrTaskRunner ocrTaskRunner = new SmartOcrTaskRunner(smartId, imageFiles, ocrInfoDao, ocrDataModel);
		
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.submit(ocrTaskRunner);
		
		return smartId;
	}


	/**
	 * Save images to database
	 * 
	 * @param imageFiles is the file to save to the database
	 * @param ocrDataModel is the actual datamodel
	 * @return updated datamodel
	 */
	private SmartOCRDataModel saveImagesToDatabase(List<File> imageFiles, SmartOCRDataModel ocrDataModel) {
		if(imageFiles!=null && !imageFiles.isEmpty()){
			
			for (int i = 0; i < imageFiles.size(); i++) {
				if(i==0){
					ocrDataModel.setImage(ImageToByteConverter.convertImageToByte(imageFiles.get(i), "jpg"));
				}
				else if(i==1){
					ocrDataModel.setBackImage(ImageToByteConverter.convertImageToByte(imageFiles.get(i), "jpg"));
				}
				else if(i==2){
					ocrDataModel.setLeftImage(ImageToByteConverter.convertImageToByte(imageFiles.get(i), "jpg"));
				}
				else if(i==3){
					ocrDataModel.setRightImage(ImageToByteConverter.convertImageToByte(imageFiles.get(i), "jpg"));
				}
				else if(i==4){
					ocrDataModel.setTopImage(ImageToByteConverter.convertImageToByte(imageFiles.get(i), "jpg"));
				}
				else if(i==5){
					ocrDataModel.setBottomImage(ImageToByteConverter.convertImageToByte(imageFiles.get(i), "jpg"));
				}
			}
		}
		
		return ocrDataModel;
	}
	
	
	private String getPathToSaveFiles() {
		return FILE_UPLOAD_PATH + File.separator + System.currentTimeMillis() + File.separator;
	}


	public void setOcrInfoDao(OcrInfoDao ocrInfoDao) {
		this.ocrInfoDao = ocrInfoDao;
	}


	public void setOcrTaskExecutor(ThreadPoolTaskExecutor ocrTaskExecutor) {
		this.ocrTaskExecutor = ocrTaskExecutor;
	}


	public void setRabbitMqProvider(RabbitMQProvider rabbitMqProvider) {
		this.rabbitMqProvider = rabbitMqProvider;
	}

	
}
