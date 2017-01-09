package com.smartocr.ocr.util;

import java.util.Comparator;
import java.util.Map;

public class AttributeComparator implements Comparator<Map<String, Object>>{

	private final static String DISPLAY_ORDER = "displayOrder";
	
	
	@Override
	public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		
		Integer displayLevel1 = (Integer)o1.get(DISPLAY_ORDER);
		Integer displayLevel2 = (Integer)o2.get(DISPLAY_ORDER);
		
		if ( !(displayLevel1 > 14 && displayLevel2 > 14) && displayLevel1 < displayLevel2) {
			return -1;
		} else if (displayLevel1 > 14 && displayLevel2 > 14) {
			Double cScore1 = (Double)o1.get("CLevel"); 
			Double cScore2 = (Double)o2.get("CLevel"); 
			return cScore2.compareTo(cScore1);
		} else {
			return 0;
		}
		
	}

}
