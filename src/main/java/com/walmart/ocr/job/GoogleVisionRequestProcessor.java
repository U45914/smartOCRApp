package com.walmart.ocr.job;

import java.io.File;
import java.util.List;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.walmart.ocr.model.GVisionResponse;
import com.walmart.ocr.model.ParseRequest;
import com.walmart.ocr.util.GVision;
import com.walmart.ocr.util.GvisionResponseToOCRResponseConverter;

public class GoogleVisionRequestProcessor {

	public ParseRequest readOcrTextFromImages(List<File> imageFiles) throws Exception {
		GVision gvision = new GVision();
		BatchAnnotateImagesResponse batchImageResponse = gvision.doOCR(imageFiles);

		GVisionResponse gVisionResponse = GvisionResponseToOCRResponseConverter.convert(batchImageResponse);

		ParseRequest smartOcrRequest = GvisionResponseToOCRResponseConverter.parseResponse(gVisionResponse);

		return smartOcrRequest;
	}

}
