package com.walmart.ocr.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class ImageBarcodeRunner implements Callable<String> {

	private static final Logger logger = Logger.getLogger(ImageBarcodeRunner.class);

	private boolean jobCompleted = false;
	private String barCodeText;

	private List<File> imageFiles = null;

	public ImageBarcodeRunner(List<File> imageFiles) {
		this.imageFiles = imageFiles;
	}

	@Override
	public String call() throws Exception {
		logger.info("Started Barcode reader JOB");
		return startBarcodeReader(imageFiles);
	}

	private String startBarcodeReader(List<File> imageFiles) {

		for (File imgFile : imageFiles) {
			try {
				if (!imgFile.isDirectory()) {
					BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(
							ImageIO.read(new FileInputStream(imgFile)))));

					String barcode = getBarCodeFromImage(binaryBitmap);
					if (barcode != null) {
						jobCompleted = true;
						this.barCodeText = barcode;
						return barcode;
					}
				}
			} catch (IOException e) {
				logger.error("Failed FileName : " + imgFile.getName(), e);
			} catch (NotFoundException e) {
				logger.error("Failed FileName : " + imgFile.getName(), e);
			} catch (Exception e) {
				logger.error("Failed FileName : " + imgFile.getName(), e);
			}
		}
		return barCodeText;

	}

	private String getBarCodeFromImage(BinaryBitmap binaryBitmap) throws IOException, NotFoundException, Exception {

		MultiFormatReader mfr = new MultiFormatReader();
		Result result = mfr.decode(binaryBitmap, getHintsMap());
		BarcodeFormat barcodeFormat = result.getBarcodeFormat();

		System.out.println(barcodeFormat.name());
		return result.getText();
	}

	private Map<DecodeHintType, Object> getHintsMap() {
		Map<DecodeHintType, Object> tmpHintsMap = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
		tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		tmpHintsMap.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
		tmpHintsMap.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);

		return tmpHintsMap;
	}

	public boolean isJobCompleted() {
		return jobCompleted;
	}

	public void setJobCompleted(boolean jobCompleted) {
		this.jobCompleted = jobCompleted;
	}

	public String getBarCodeText() {
		return barCodeText;
	}

	public void setBarCodeText(String barCodeText) {
		this.barCodeText = barCodeText;
	}

}
