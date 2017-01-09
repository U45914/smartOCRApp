/**
 * 
 */
package com.smartocr.ocr.util;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @author Rahul
 *
 */
public class ImageUtils {

	public static byte[] convertImageToByteArray() {
		return null;
	}
	
	 public static byte[] getBitmapAsByteArray(File bitmap) {
		    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    //bitmap.compress(CompressFormat.PNG, 0, outputStream);       
		    return outputStream.toByteArray();
}
}
