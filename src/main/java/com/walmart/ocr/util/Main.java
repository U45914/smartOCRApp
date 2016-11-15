package com.walmart.ocr.util;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walmart.ocr.dao.OcrInfoDao;
import com.walmart.ocr.model.SmartOCRDataModel;
/**
 * 
 * @author abkha1
 *
 */
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
		ocrDataModel.setStatus("waiting");
		id = dao.createOcrData(ocrDataModel);
		System.out.println("************************* xxxxxxxxx *************************");
		System.out.println("****************************"+id+"***************************");
		System.out.println("************************* xxxxxxxxx *************************");
		ocrDataModel = dao.findOcrDataById(id);
		System.out.println("*  status 1 *"+ocrDataModel.getStatus());
		System.out.println("*  status 1 *"+ocrDataModel.getAbsoobaResponse());

		 ocrDataModel.setStatus("modified status");
		 ocrDataModel.setAbsoobaResponse("modified absooba");
		ocrDataModel.setImageUploadDate(new Date());
		dao.updateOcrData(ocrDataModel);
		ocrDataModel = dao.findOcrDataById(ocrDataModel.getOcrRequestId());
		System.out.println("*  status 2*"+ocrDataModel.getStatus());
		System.out.println("*  status 2*"+ocrDataModel.getAbsoobaResponse());
		//File imageFile=ImageToByteConverter.convertByteArraytoImages(ocrDataModel.getImage(), ocrDataModel.getImageUrls(), "jpg");
		System.out.println("----- conversion completed from byte to image file");
		System.out.println("************************* xxxxxxxxx *************************");
		System.out.println("****************************"+id+"***************************");
		System.out.println("************************* xxxxxxxxx *************************");
		
		List<SmartOCRDataModel> dataModels= dao.findOcrDataByStatus("waiting", 1, 10);

		System.out.println("** "+dataModels+" **\n");
		System.out.println("** "+dataModels.size()+" **");
		
		ocrDataModel = dao.findOcrDataById(17);
		System.out.println("*  status 2*"+ocrDataModel.getStatus());
		System.out.println("*  status 2*"+ocrDataModel.getAbsoobaResponse());
	}

}
