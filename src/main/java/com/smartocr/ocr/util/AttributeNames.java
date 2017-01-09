package com.smartocr.ocr.util;

import java.util.HashMap;
import java.util.Map;

public final class AttributeNames {

	private final static Map<String, Integer> attributes = new HashMap<String, Integer>();
	
	public final static String DISPLAY_ORDER = "displayOrder";
	
	private static int RND_DISPLAY_ORDER = 25;
	static {
		attributes.put("UPC", 1);
		attributes.put("Extracted_UPC", 2);
		attributes.put("Product_Category", 3);
		attributes.put("Product_Sub_Category", 4);
		attributes.put("Brand", 5);
		attributes.put("Product_Name", 6);
		attributes.put("Units_Per_Consumer_Unit", 7);
		attributes.put("Product_Short_Description", 8);
		attributes.put("Product_Long_Description", 9);
		attributes.put("Product_Type", 10);
		attributes.put("Direction_Of_Use", 11);
		attributes.put("Warning", 12);
		attributes.put("Manufacturing_Country", 13);
		attributes.put("Ingredients", 14);
		
	}
	
	public static int getDisplayOrder(String attributeName) {
		Integer displayOrder = attributes.get(attributeName);
		return displayOrder != null ? displayOrder : getDisplayOrder();
	}
	
	private static int getDisplayOrder() {
		
		++RND_DISPLAY_ORDER;
		
		if (RND_DISPLAY_ORDER > 100) {
			RND_DISPLAY_ORDER = 25;
		}
		
		return RND_DISPLAY_ORDER;
	}
}
