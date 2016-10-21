package com.mit.importDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mit.dao.address.CountryDAO;
import com.mit.entities.address.Country;

public class CountryImport {

	public static void main(String[] args) {
		HashMap<String, String> lstZone = CSVParser.readzone_code(true);
		HashMap<String, String> lstCode = CSVParser.readzone_code(false);
		List<Country> lstCountry = new ArrayList<Country>();
		for (String countryName : lstZone.keySet()) {
			String countryCode = lstCode.get(countryName) != null ? lstCode.get(countryName) : countryName;
			if (countryName.contains("\"")) {
				countryName = countryName.replaceAll("\"", "");
				countryName = countryName.replaceAll("\\.", ",");
			}
			Country country = new Country(countryCode, countryName);
			lstCountry.add(country);
		}
		//add US
		lstCountry.add(new Country("US", "United States"));
		CountryDAO.getInstance().insertBatch(lstCountry);
		System.out.println("Done");
		System.out.println(CountryDAO.getInstance().getList().size());
	}

}
