package com.walmart.ocr.util;

import java.io.File;
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
		// File imageFile=new File("ImagesToProcess/test.jpg");
		ocrDataModel.setImageUrls("ImagesToProcess/test.jpg");
		//ocrDataModel.setImage(ImageToByteConverter.convertImageToByte(ocrDataModel.getImageUrls(), "jpg"));
		Serializable id = null;

		id=3;//id = dao.createOcrData(ocrDataModel);
		System.out.println("---- > " + id);
		ocrDataModel = dao.findOcrDataById(id);
		System.out.println(ocrDataModel);

		// ocrDataModel.setImageUrls("image is modified");
		ocrDataModel.setImageUploadDate(new Date());
		dao.updateOcrData(ocrDataModel);
		ocrDataModel = dao.findOcrDataById(ocrDataModel.getOcrRequestId());
		System.out.println("------------ > modified ocr data " + ocrDataModel);
		File imageFile=ImageToByteConverter.convertByteArraytoImages(ocrDataModel.getImage(), ocrDataModel.getImageUrls(), "jpg");
		System.out.println("----- conversion completed from byte to image file");

	}

}
