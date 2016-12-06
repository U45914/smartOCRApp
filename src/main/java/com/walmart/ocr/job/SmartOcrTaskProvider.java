package com.walmart.ocr.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.walmart.ocr.dao.OcrInfoDao;
import com.walmart.ocr.image.ImageServiceProcessor;
import com.walmart.ocr.model.SmartOCRDataModel;
import com.walmart.ocr.rabbit.listner.MQTaskProvider;
import com.walmart.ocr.util.MessageConverter;

public class SmartOcrTaskProvider {

	private static final Logger _LOGGER = Logger.getLogger(SmartOcrTaskProvider.class);
	
	@Autowired
	private MQTaskProvider myTaskProvider;
	@Autowired
	private OcrInfoDao ocrInfoDao;
	@Autowired
	private ImageServiceProcessor imageProcessor;
	
	@Autowired
	private AbzoobaAttributeRequestProcessor attributeRequestProcessor;

	public TaskDto getTaskForCrowd() {

		String jobId = myTaskProvider.getUserTask();
		if (jobId != null) {
			SmartOCRDataModel jobDetails = ocrInfoDao.findOcrDataById(MessageConverter.getIdForTask(jobId));
			
			return getTaskDtoFromOcrModel(jobId, jobDetails, false);
		} else {
			return null;
		}
	}
	
	public List<TaskDto> getTasks(String status, String start, String limit, String orderBy) {
		_LOGGER.info("Started loading all request");
		List<SmartOCRDataModel> smartOcrModels = null;
		List<TaskDto> tasks = null;
		try {
			smartOcrModels = ocrInfoDao.findOcrDataByStatus(status, Integer.parseInt(start), Integer.parseInt(limit), orderBy);
			tasks = new ArrayList<TaskDto>();
			for (SmartOCRDataModel ocrDataModel : smartOcrModels) {
				tasks.add(getTaskDtoFromOcrModel(MessageConverter.getSmartOCRId(ocrDataModel.getOcrRequestId()), ocrDataModel, true));
			}
		} catch (Exception e) {
			_LOGGER.error("Exception while fetching all task", e);
		}
		_LOGGER.info("Completed loading all request");
		
		return tasks;
	}

	private TaskDto getTaskDtoFromOcrModel(String smartOcrId, SmartOCRDataModel ocrDataModel, boolean isList) {
		TaskDto task = new TaskDto();
		task.setSmartOcrId(smartOcrId);
		
		task.setImages(imageProcessor.getImages(smartOcrId, ocrDataModel, false));
		
		task.setAttributes(attributeRequestProcessor.readAttributesFromJson(ocrDataModel.getAbsoobaResponse()));
		
		if (ocrDataModel.getAbzoobaResponse2() != null) {
			task.setAttributes(attributeRequestProcessor.readAttributesFromJson(ocrDataModel.getAbzoobaResponse2()));
		}
		task.setOcrRequest(MessageConverter.getParseRequestObjectFromJson(ocrDataModel.getGivisionResponse()));
		
		if (isList) {
			task.setAttributeDiff(attributeRequestProcessor.getAbzoobaModel(task));
		}
		
		return task;
	}

	public void setMyTaskProvider(MQTaskProvider myTaskProvider) {
		this.myTaskProvider = myTaskProvider;
	}

	public void setOcrInfoDao(OcrInfoDao ocrInfoDao) {
		this.ocrInfoDao = ocrInfoDao;
	}

	public void setImageProcessor(ImageServiceProcessor imageProcessor) {
		this.imageProcessor = imageProcessor;
	}

	public void setAttributeRequestProcessor(AbzoobaAttributeRequestProcessor attributeRequestProcessor) {
		this.attributeRequestProcessor = attributeRequestProcessor;
	}

	
}
