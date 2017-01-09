package com.smartocr.ocr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class BarcodeReaderUtil {

	public static Map<String, String> readBarcodeFromImage(List<File> imageFiles) {

		Map<String, String> fileGtinMap = new HashMap<String, String>();
		for (File imgFile : imageFiles) {
			try {
				if (!imgFile.isDirectory()) {
					System.out.println(imgFile);

					BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
							new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(imgFile)))));

					Map<DecodeHintType, Object> tmpHintsMap = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
					tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
					tmpHintsMap.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
					tmpHintsMap.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);

					MultiFormatReader mfr = new MultiFormatReader();
					Result result = mfr.decode(binaryBitmap, tmpHintsMap);
					BarcodeFormat barcodeFormat = result.getBarcodeFormat();
					System.out.println(barcodeFormat.name());
					System.out.println("File_Name : " + imgFile.getName() + "Bar Code " + result.getText());
					fileGtinMap.put(imgFile.getName(), result.getText());
				}
			} catch (IOException e) {
				System.out.println("Failed FileName : "+ imgFile.getName());
				e.printStackTrace();
			} catch (NotFoundException e) {
				System.out.println("Failed FileName : "+ imgFile.getName());
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Failed FileName : "+ imgFile.getName());
			}
		}
		
		return fileGtinMap;
	}
}
