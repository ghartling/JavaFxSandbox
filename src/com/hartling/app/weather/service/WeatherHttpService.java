package com.hartling.app.weather.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hartling.app.common.util.CommonUtils;

public class WeatherHttpService {
	private static final Logger logger = Logger.getLogger(WeatherHttpService.class);
	HttpUtilsService httpUtilsService = new HttpUtilsServiceImpl();

	public String getJson(String url) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = httpUtilsService.buildGetWithoutProxy(url);

		// add header
		get.setHeader("Accept", "application/json");
		get.setHeader("Content-type", "application/json");

		logger.info("Sending 'GET' request to URL : " + url);
		CloseableHttpResponse response = client.execute(get);

		// get the JSON from the response
		String json = CommonUtils.inputStreamToString(response.getEntity().getContent());
		ObjectMapper mapper = new ObjectMapper();
		Object jsonMapped = mapper.readValue(json, Object.class);
		String jsonFormatted = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMapped);
		logger.info(jsonFormatted);

		return json;
	}

}
