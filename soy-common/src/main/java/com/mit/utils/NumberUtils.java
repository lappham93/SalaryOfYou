package com.mit.utils;

public class NumberUtils {	
	private NumberUtils() {}
	
	public static long toPrimitive(Long val) {
		return val != null ? val : 0L;
	}
	
	public static int toPrimitive(Integer val) {
		return val != null ? val : 0;
	}
	
	public static String ordinalSuffixOf(int i) {
	    int j = i % 10,
	        k = i % 100;
	    if (j == 1 && k != 11) {
	        return i + "st";
	    }
	    if (j == 2 && k != 12) {
	        return i + "nd";
	    }
	    if (j == 3 && k != 13) {
	        return i + "rd";
	    }
	    return i + "th";
	}
}
