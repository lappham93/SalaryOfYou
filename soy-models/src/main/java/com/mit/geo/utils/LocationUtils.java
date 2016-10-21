package com.mit.geo.utils;

import org.apache.commons.lang.StringUtils;

public class LocationUtils
{
	public static boolean checkValidLonLat(double lat, double lon)
	{
		if (lat > 90 || lat < -90 || Double.isNaN(lat)) {
			return false;
		}
		
		if ( lon > 180 || lon < -180 || Double.isNaN(lon)) {
			return false;
		}

		if (lat == 0.0f && lon == 0.0f) {
			return false;
		}

		return true;
	}

	public static boolean checkValidCell(String cellId, String strLac)
	{
		if (cellId != null && !cellId.isEmpty() && !"0".equals(cellId) && !"-1".equals(cellId) && strLac != null && !strLac.isEmpty())
		{
			try
			{
				int cell = Integer.parseInt(cellId);
				if (cell <= 0) {
					return false;
				}

				int lac = Integer.parseInt(strLac);
				if (lac <= 0) {
					return false;
				}
			}
			catch (Exception e) {
				return false;
			}

			return true;
		}

		return false;
	}

	/** Reference WIKIPEDIA for correct format of MAC Address */
	public static boolean checkValidMACAddress(String wifiMac)
	{
		if (wifiMac != null && !wifiMac.isEmpty())
		{
			if (wifiMac.length() == 14 && StringUtils.countMatches(wifiMac, ".") == 2) {
				return true;
			}

			if (wifiMac.length() == 17 && (StringUtils.countMatches(wifiMac, ":") == 5 || StringUtils.countMatches(wifiMac, "-") == 5)) {
				return true;
			}
		}

		return false;
	}

	public static String getDisplayDistance(double distance) {
		String displayDistance = " --- ";
		if (Double.isNaN(distance) || distance < 0) {
		} else if (distance < 0.95) {
			if(distance < 0.025) {
				distance = 0.025 + distance;
			}
			displayDistance = Math.round(distance * 20) * 50 + " m";
		} else if (distance < 5) {
			displayDistance = Math.round(distance * 10) / 10.0 + " km";
		} else if (distance < 10) {
			displayDistance = Math.round(distance * 2) / 2.0 + " km";
		} else {
			displayDistance = Math.round(distance) + " km";
		}

		return displayDistance;
	}

	public static double format(double val) {
		int fs = 10000;
		int cval = (int)(val * fs);
		double rs = (double)cval / fs;
		return rs;
	}
}
