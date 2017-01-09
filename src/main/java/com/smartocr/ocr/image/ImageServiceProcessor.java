/**
 * 
 */
package com.smartocr.ocr.image;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.smartocr.ocr.dao.OcrInfoDao;
import com.smartocr.ocr.job.OcrImageDto;
import com.smartocr.ocr.model.ParseRequest;
import com.smartocr.ocr.model.SmartOCRDataModel;
import com.smartocr.ocr.util.MessageConverter;

/**
 * @author Rahul
 *
 */
public class ImageServiceProcessor {

	@Autowired
	private OcrInfoDao ocrInfoDao;

	public List<OcrImageDto> getImagesById(String smartOcrId, boolean setText) {

		SmartOCRDataModel ocrDataModel = ocrInfoDao.findOcrDataById(MessageConverter.getIdForTask(smartOcrId));
		
		List<OcrImageDto> images = getImages(smartOcrId, ocrDataModel, setText);
		
		return images;

	}

	public List<OcrImageDto> getImages(String ocrId, SmartOCRDataModel ocrDataModel, boolean setText) {
		ParseRequest ocrResponse = MessageConverter.getParseRequestObjectFromJson(ocrDataModel.getGivisionResponse());
		List<OcrImageDto> finalImageDtos = new ArrayList<OcrImageDto>();
		
		if (ocrDataModel.getImage() != null) {
			OcrImageDto imageDto = new OcrImageDto();
			imageDto.setSmartOcrId(ocrId);
			imageDto.setImage(ocrDataModel.getImage());
			if (setText) {
				imageDto.setOcrText(ocrResponse.getFrontText());
			}
			imageDto.setView("Front");
			finalImageDtos.add(imageDto);
		}
		
		if (ocrDataModel.getBackImage() != null) {
			OcrImageDto imageDto = new OcrImageDto();
			imageDto.setImage(ocrDataModel.getBackImage());
			imageDto.setSmartOcrId(ocrId);
			if (setText) {
				imageDto.setOcrText(ocrResponse.getBackText());
			}
			imageDto.setView("Back");
			finalImageDtos.add(imageDto);
		}
		
		if (ocrDataModel.getLeftImage() != null) {
			OcrImageDto imageDto = new OcrImageDto();
			imageDto.setSmartOcrId(ocrId);
			imageDto.setImage(ocrDataModel.getLeftImage());
			if (setText) {
				imageDto.setOcrText(ocrResponse.getLeftSideText());
			}
			imageDto.setView("Left");
			finalImageDtos.add(imageDto);
		}
		
		if (ocrDataModel.getRightImage() != null) {
			OcrImageDto imageDto = new OcrImageDto();
			imageDto.setSmartOcrId(ocrId);
			imageDto.setImage(ocrDataModel.getRightImage());
			if (setText) {
				imageDto.setOcrText(ocrResponse.getRightSideText());
			}
			imageDto.setView("Right");
			finalImageDtos.add(imageDto);
		}
		
		if (ocrDataModel.getTopImage() != null) {
			OcrImageDto imageDto = new OcrImageDto();
			imageDto.setSmartOcrId(ocrId);
			imageDto.setImage(ocrDataModel.getTopImage());
			if (setText) {
				imageDto.setOcrText(ocrResponse.getTopSideText());
			}
			imageDto.setView("Top");
			finalImageDtos.add(imageDto);
		}
		
		if (ocrDataModel.getBottomImage() != null) {
			OcrImageDto imageDto = new OcrImageDto();
			imageDto.setSmartOcrId(ocrId);
			imageDto.setImage(ocrDataModel.getBottomImage());
			if (setText) {
				imageDto.setOcrText(ocrResponse.getBottomSideText());
			}
			imageDto.setView("Bottom");
			finalImageDtos.add(imageDto);
		}
		
		return finalImageDtos;
	}

	public void setOcrInfoDao(OcrInfoDao ocrInfoDao) {
		this.ocrInfoDao = ocrInfoDao;
	}
	
	
}
