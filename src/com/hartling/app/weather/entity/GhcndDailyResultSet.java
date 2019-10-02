package com.hartling.app.weather.entity;

import java.util.ArrayList;
import java.util.List;

public class GhcndDailyResultSet {
	private List<GhcndDailyView> results = new ArrayList<>();

	public List<GhcndDailyView> getResults() {
		return results;
	}

	public void setResults(List<GhcndDailyView> results) {
		this.results = results;
	}

}
