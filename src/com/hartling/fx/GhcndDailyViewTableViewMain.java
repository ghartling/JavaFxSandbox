package com.hartling.fx;

import org.apache.log4j.Logger;

import com.hartling.app.weather.service.HttpUtilsService;
import com.hartling.app.weather.service.HttpUtilsServiceImpl;

import javafx.application.Application;

public class GhcndDailyViewTableViewMain {
	private static final Logger logger = Logger.getLogger(GhcndDailyViewTableViewMain.class);
	HttpUtilsService httpUtilsService = new HttpUtilsServiceImpl();

	public static void main(String[] args) {
		Application.launch(GhcndDailyViewTableView.class);
	}

}
