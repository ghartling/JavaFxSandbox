package com.hartling.app.weather.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hartling.app.weather.entity.GhcndDailyView;
import com.hartling.app.weather.entity.StationIdMap;

public class ObservedWeatherService {
	private static final Logger logger = Logger.getLogger(ObservedWeatherService.class);
	HttpUtilsService httpUtilsService = new HttpUtilsServiceImpl();
	WeatherHttpService weatherHttpService = new WeatherHttpService();
	ObjectMapper mapper = new ObjectMapper();

	/**
	 * Get the daily data with normals
	 * 
	 * @param stnId
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public List<GhcndDailyView> findUsinfoByStn(String stnId) throws ClientProtocolException, IOException {
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime startDate = today.minusDays(20);
		LocalDateTime endDate = startDate.plusDays(10);
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		String url = String.format("http://localhost:8080/weather/stn/%s", stnId);
		String json = weatherHttpService.getJson(url);

		// map to real object
		List<GhcndDailyView> ghcndMapped = mapper.readValue(json, new TypeReference<List<GhcndDailyView>>() {
		});

		return ghcndMapped;
	}

	public List<StationIdMap> getStationList() throws JsonParseException, JsonMappingException, IOException {

		String url = String.format("http://localhost:8080/weather/stn/id/map");
		String json = weatherHttpService.getJson(url);

		// map to real object
		List<StationIdMap> mapped = mapper.readValue(json, new TypeReference<List<StationIdMap>>() {
		});

		return mapped;
	}

}
