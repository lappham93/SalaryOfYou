package com.mit.utils;

import java.util.HashMap;
import java.util.Map;

import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

public class UnitUtils {
	private static final Map<DUnit, Unit<Length>> dUnitMap;
	private static final Map<WUnit, Unit<Mass>> wUnitMap;
	public static final UnitUtils Instace = new UnitUtils();
	
	static{
		dUnitMap = new HashMap<DUnit, Unit<Length>>();
		dUnitMap.put(DUnit.INCH, NonSI.INCH);
		dUnitMap.put(DUnit.FEET, NonSI.FOOT);
		dUnitMap.put(DUnit.MILIMETER, SI.MILLIMETRE);
		dUnitMap.put(DUnit.CENTIMETER, SI.CENTIMETER);
		dUnitMap.put(DUnit.METER, SI.METRE);	
		
		wUnitMap = new HashMap<WUnit, Unit<Mass>>();
		wUnitMap.put(WUnit.OZ, NonSI.OUNCE);
		wUnitMap.put(WUnit.POUND, NonSI.POUND);
		wUnitMap.put(WUnit.GRAM, SI.GRAM);
		wUnitMap.put(WUnit.KILOGRAM, SI.KILOGRAM);
	}
	
	public double convert(DUnit src, DUnit dest, double value) {
		if (dUnitMap.get(src) != null && dUnitMap.get(dest) != null) {
			return dUnitMap.get(src).getConverterTo(dUnitMap.get(dest)).convert(value);
		}
		
		return value;
	}
	
	public double convert(WUnit src, WUnit dest, double value) {
		if (wUnitMap.get(src) != null && wUnitMap.get(dest) != null) {
			return wUnitMap.get(src).getConverterTo(wUnitMap.get(dest)).convert(value);
		}
		
		return value;
	}
	
	public static void main(String[] args) {
		System.out.println(UnitUtils.Instace.convert(DUnit.METER, DUnit.MILIMETER, 1));
	}
}
