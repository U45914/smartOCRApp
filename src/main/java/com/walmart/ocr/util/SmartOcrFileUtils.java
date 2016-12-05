/**
 * 
 */
package com.walmart.ocr.util;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

/**
 * @author Rahul
 * 
 */
public class SmartOcrFileUtils {

	private static final Logger _LOGGER = Logger.getLogger(SmartOcrFileUtils.class);

	public static String saveFiles(HttpServletRequest request, String pathToSaveFiles) {
		StringBuilder imagePathBuilder = new StringBuilder();
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
							_LOGGER.info("Field Name: " + fieldName + ", Field Value: " + fieldValue);
							_LOGGER.info("Candidate Name: " + name);
						} else {
							final File file = new File(pathToSaveFiles + item.getName());
							_LOGGER.info("Saving the file: " + file.getName());
							item.write(file);
							imagePathBuilder.append(file.getPath());
							imagePathBuilder.append(";");
						}
					}
				}
			} catch (FileUploadException e) {
				_LOGGER.error("Exception while processing files", e);
			} catch (Exception e) {
				_LOGGER.error("Exception while processing files", e);
			}
		}
		return imagePathBuilder.toString();

	}

	public static String getUpcFromFileName(String fileName) {
		String upc = fileName;
		if (fileName.contains("-")) {
			upc = fileName.substring(0, fileName.indexOf("-"));
		} else {
			if (upc.contains(".")) {
				upc = fileName.substring(0, fileName.indexOf('.'));
			}
		}

		return upc;
	}

	public static List<File> sortFiles(List<File> imageFiles) {

		Collections.sort(imageFiles, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		return imageFiles;
	}
}
