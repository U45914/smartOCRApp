package com.walmart.ocr.rest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.ColorInfo;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.ImageProperties;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.walmart.ocr.model.GVisionResponse;
import com.walmart.ocr.model.OCRResult;
import com.walmart.ocr.util.ColorUtils;
import com.walmart.ocr.util.GVision;
import com.walmart.ocr.util.GvisionResponseToOCRResponseConverter;

@Path("/smartOCR")
public class GoogleVisionResource {

	private static final Logger logger = Logger
			.getLogger(GoogleVisionResource.class);
	private static final String FILE_UPLOAD_PATH = "ImagesToProcess/";
	private int fileCount = 1;

	@POST
	@Path("/convertImagesToText")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadMultiFile(@Context HttpServletRequest request) {

		String result = null;
		try {

			logger(" ");
			logger("******** New Conversion Started *******");
			saveFiles(request);
			logger("******** Saved Files *******");
			List<File> imageFiles = new ArrayList<File>();
			for (int file = 1; file < fileCount; file++) {
				imageFiles.add(new File(FILE_UPLOAD_PATH + "image-" + file));
			}

			GVision gvision = new GVision();
			BatchAnnotateImagesResponse batchImageResponse = gvision.doOCR(imageFiles);
			GVisionResponse gVisionResponse = GvisionResponseToOCRResponseConverter.convert(batchImageResponse);
			
			result = GvisionResponseToOCRResponseConverter.toOCRString(gVisionResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(200).entity(result).build();

	}

	private void saveFiles(HttpServletRequest request) {
		String name = null;
		/* Check whether request is multipart or not. */
		if (ServletFileUpload.isMultipartContent(request)) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload fileUpload = new ServletFileUpload(factory);
			try {

				List<FileItem> items = fileUpload.parseRequest(request);

				if (items != null) {
					Iterator<FileItem> iter = items.iterator();
					/*
					 * Return true if the instance represents a simple form
					 * field. Return false if it represents an uploaded file.
					 */

					while (iter.hasNext()) {
						final FileItem item = iter.next();
						final String fieldName = item.getFieldName();
						final String fieldValue = item.getString();
						if (item.isFormField()) {
							name = fieldValue;
							System.out.println("Field Name: " + fieldName + ", Field Value: " + fieldValue);
							System.out.println("Candidate Name: " + name);
						} else {
							final File file = new File(FILE_UPLOAD_PATH + "image-" + fileCount++);

							System.out.println("Saving the file: " + file.getName());
							item.write(file);
						}
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	@POST
	@Path("/convertImageToText")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile1(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		String uploadedFileLocation = fileDetail.getFileName();
		String result = null;
		StringBuilder resultString = new StringBuilder();
		try {

			logger(" ");
			logger("******** New Conversion Started *******");
			// save it
			writeToFile(uploadedInputStream, uploadedFileLocation);
			logger("File uploaded to : " + uploadedFileLocation);
			File imageFile = new File(uploadedFileLocation);
			GVision gvision = new GVision();
			AnnotateImageResponse annotateImageResponse = gvision
					.doOCR(imageFile);
			List<EntityAnnotation> labelAnnotations = annotateImageResponse
					.getLabelAnnotations();
			if (null != labelAnnotations) {
				resultString.append("Label Details : ");
				for (EntityAnnotation labelAnnotation : labelAnnotations) {
					System.out.println(labelAnnotation.getDescription());
					resultString.append(labelAnnotation.getDescription());
					resultString.append(" ");
				}
			}
			List<EntityAnnotation> logoAnnotations = annotateImageResponse
					.getLogoAnnotations();
			if (null != logoAnnotations) {
				resultString.append("Logo Details : ");
				for (EntityAnnotation logoAnnotation : logoAnnotations) {
					resultString.append(logoAnnotation.getDescription());
					System.out.println(logoAnnotation.getDescription());
					resultString.append(" ");
				}
			}
			List<EntityAnnotation> textAnnotations = annotateImageResponse
					.getTextAnnotations();
			if (null != textAnnotations) {
				resultString.append("Text Details : ");
				if (null != textAnnotations.get(0)) {
					resultString
							.append(textAnnotations.get(0).getDescription());
				}
				for (EntityAnnotation textAnnotation : textAnnotations) {
					// resultString.append(textAnnotation.getDescription());
					System.out.println(textAnnotation.getDescription());
					resultString.append(" ");
				}
			}
			ImageProperties imagePropertiesAnnotation = annotateImageResponse
					.getImagePropertiesAnnotation();
			if (null != imagePropertiesAnnotation) {
				ColorInfo colorInfo = imagePropertiesAnnotation
						.getDominantColors().getColors().get(0);
				ColorUtils colorUtils = new ColorUtils();
				String myColor = colorUtils.getColorNameFromRgb(
						Math.round(colorInfo.getColor().getRed()),
						Math.round(colorInfo.getColor().getGreen()),
						Math.round(colorInfo.getColor().getBlue()));
				resultString.append("Color Details : ");
				resultString.append(myColor);
			}
			result = resultString.toString();
			uploadedInputStream.close();

			if (imageFile.delete())
				logger(uploadedFileLocation + " Deleted");
			else {
				logger("Failed to delete File");
			}

			saveToTXT(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(200).entity(result).build();

	}

	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public OCRResult getOCRJSON() throws IOException {

		OCRResult ocrResult = new OCRResult();
		String everything = null;
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream("ConvertedSmart.txt");
			everything = IOUtils.toString(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			inputStream.close();
		}
		ocrResult.setResultString(everything);
		return ocrResult;
	}

	// save uploaded file to new location
	private void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void saveToTXT(String content) {

		try {
			File file = new File("ConvertedSmart.txt");

			// if file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();

			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			logger("Saved as TXT ");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void logger(String log) {
		logger.debug(log);
	}
}
