package com.mit.importDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mit.dao.address.CountryDAO;
import com.mit.dao.address.StateDAO;
import com.mit.entities.address.Country;
import com.mit.entities.address.State;

public class StateImport {

	public static void main(String[] args) {
		List<State> lstState = new ArrayList<State>();
		String[][] stateData = CSVParser.readState();
		for (int idx = 0; idx < stateData.length; idx++) {
			State state = new State(stateData[idx][0], stateData[idx][2], stateData[idx][1]);
			lstState.add(state);
		}
		StateDAO.getInstance().insertBatch(lstState);
		System.out.println("Done");
		System.out.println(StateDAO.getInstance().getListByCountry("US").size());
		System.out.println(StateDAO.getInstance().getListByCountry("GB").size());
	}

}
