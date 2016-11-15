/**
 * 
 */
package com.walmart.ocr.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * @author U46591
 *
 */
public class ImageToByteConverter {

	
	public static byte [] convertImageToByte (String imageFilePath,String imageFormat){
		File image=new File(imageFilePath);
		byte [] imageInByte=null;
		try {
			BufferedImage imageStream=ImageIO.read(image);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			ImageIO.write(imageStream, imageFormat, baos);
			baos.flush();
			imageInByte=baos.toByteArray();
			baos.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return imageInByte;
	}

	public static File convertByteArraytoImages(byte [] byteImages,String imageFilePath,String imageFormat){
		InputStream is=null;
		BufferedImage bufferedImage=null;
		File imageFile=null;
		
		is=new ByteArrayInputStream(byteImages);
		try {
			imageFile=new File(imageFilePath);
			bufferedImage=ImageIO.read(is);
			ImageIO.write(bufferedImage, imageFormat,imageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return imageFile;
	}
}
