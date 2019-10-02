package com.hartling.app.weather.ncdc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hartling.BaseTest;
import com.hartling.app.common.util.CommonUtils;
import com.hartling.app.weather.entity.GhcndDailyView;
import com.hartling.app.weather.service.HttpUtilsService;
import com.hartling.app.weather.service.HttpUtilsServiceImpl;

public class GhcndDailyTest extends BaseTest {
	private static final Logger logger = Logger.getLogger(GhcndDailyTest.class);
	private static boolean oneTimeSetupComplete = false;

	HttpUtilsService httpUtilsService = new HttpUtilsServiceImpl();

	@Before
	public final void setupBeforeMethod() throws IOException {
		logger.info("setupBeforeMethod");

		if (!oneTimeSetupComplete) {
			// httpUtilsService.buildGetAutoDetectProxy("http://www.google.com");

			oneTimeSetupComplete = true;
		}
	}

	@After
	public final void tearDownAfterMethod() {
	}

	/**
	 * test retrieving a user
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDaily() throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime startDate = today.minusDays(20);
		LocalDateTime endDate = startDate.plusDays(10);
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String stnId = "KBOS";

		String url = String.format("http://localhost:8080/normal/stn/%s", stnId);

		HttpGet get = httpUtilsService.buildGetWithoutProxy(url);
		// add header
//		get.setHeader("token", ncdcToken);
		get.setHeader("Accept", "application/json");
		get.setHeader("Content-type", "application/json");

		logger.info("Sending 'GET' request to URL : " + url);
		CloseableHttpResponse response = client.execute(get);

		// get the JSON from the response
		String json = inputStreamToString(response.getEntity().getContent());
		ObjectMapper mapper = new ObjectMapper();
		Object jsonMapped = mapper.readValue(json, Object.class);
		String jsonFormatted = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMapped);
		System.out.println(jsonFormatted);

		// map to real object
//		GhcndDataset ghcndData = new 
//		List<GhcndDailyView> ghcndMapped = (List<GhcndDailyView>) mapper.readValue(json, GhcndDailyView.class);
		List<GhcndDailyView> ghcndMapped = mapper.readValue(json, new TypeReference<List<GhcndDailyView>>() {
		});
//		System.out.println(
//				String.format(" day = %d, month: %d", ghcndMapped.getNormalDay(), ghcndMapped.getNormalMonth()));
		// System.out.println("number of results = " + ghcndMapped.getResults().size());

		CommonUtils.saveStnDataToFile(new ByteArrayInputStream(jsonFormatted.getBytes()), stnId);
	}

	public static String inputStreamToString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		return sb.toString();
	}

}
