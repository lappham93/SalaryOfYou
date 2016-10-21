package com.mit.importDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.WordUtils;

import com.mit.dao.address.CityDAO;
import com.mit.dao.address.StateDAO;
import com.mit.entities.address.City;
import com.mit.entities.address.State;

public class CityImport {

	public static void main(String[] args) {
		List<City> lstState = new ArrayList<City>();
		String[][] stateData = CSVParser.readCity();
		Set<String> cityCode = new HashSet<>();
		List<State> states = StateDAO.getInstance().getListByCountry("US");
		Map<String, String> stateMap = new HashMap<>();
		for (State state: states) {
			stateMap.put(state.getIsoCode(), state.getName());
		}
		
		for (int idx = 0; idx < stateData.length; idx++) {
			if (stateData[idx][0] != null) {
				String ccode = stateData[idx][1] + "-" + stateData[idx][0];
				if (cityCode.contains(ccode)) {
					continue;
				}
				String stateName = stateMap.get(stateData[idx][1]);
				City state = new City(ccode, "US", stateData[idx][1], WordUtils.capitalizeFully(stateName), WordUtils.capitalizeFully(stateData[idx][0]));
				lstState.add(state);
				cityCode.add(ccode);

			}
		}
		CityDAO.getInstance().insertBatch(lstState);
		
//		for (City city:CityDAO.getInstance().getList()) {
//			city.setName(WordUtils.capitalizeFully(city.getName()));
//			city.setStateName(WordUtils.capitalizeFully(stateMap.get(city.getStateIsoCode())));
//			CityDAO.getInstance().update(city);
//		}
		System.out.println("Done");
	}

}
