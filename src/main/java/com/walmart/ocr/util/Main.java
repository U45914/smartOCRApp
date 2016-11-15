package com.walmart.ocr.util;

import java.io.Serializable;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walmart.ocr.dao.OcrInfoDao;
import com.walmart.ocr.model.SmartOCRDataModel;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/config/smartOcrApp.xml");
		OcrInfoDao dao = context.getBean("ocrInfoDaoImpl", OcrInfoDao.class);
		SmartOCRDataModel ocrDataModel = new SmartOCRDataModel();
		ocrDataModel.setImageUrls("test Image");
		Serializable id = null;
		id = dao.createOcrData(ocrDataModel);
		System.out.println("---- > " + id);
		ocrDataModel = dao.findOcrDataById(id);
		System.out.println(ocrDataModel);

		ocrDataModel.setImageUrls("image is modified");
		ocrDataModel.setImageUploadDate(new Date());
		dao.updateOcrData(ocrDataModel);
		ocrDataModel = dao.findOcrDataById(ocrDataModel.getOcrRequestId());
		System.out.println("------------ > modified ocr data " + ocrDataModel);
	}

}
