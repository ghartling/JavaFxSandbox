package com.hartling.fx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hartling.app.weather.entity.GhcndDailyView;
import com.hartling.app.weather.service.HttpUtilsService;
import com.hartling.app.weather.service.HttpUtilsServiceImpl;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GhcndDailyViewTableView extends Application {
	private static final Logger logger = Logger.getLogger(GhcndDailyViewTableView.class);
	HttpUtilsService httpUtilsService = new HttpUtilsServiceImpl();

	public static void main(String[] args) {
		Application.launch(GhcndDailyViewTableView.class);
	}

	@Override
	public void start(Stage primaryStage) {
		// List<GhcndDailyView> data;
		try {
			List<GhcndDailyView> data = getData();

			String fields[] = { "stationId", "icaoId", "occurDate", "tmax", "tmin", "prcp", "snow", "normalMax", "normalMin" };

			// create the table
			TableView<GhcndDailyView> tableView = new TableView<>();

			// add the columns
			int i = 0;
			for (String s : fields) {
				TableColumn<GhcndDailyView, String> column1 = new TableColumn<>(fields[i]);
				column1.setCellValueFactory(new PropertyValueFactory<>(fields[i]));

				tableView.getColumns().add(column1);
				++i;
			}

			// add the data

			tableView.getItems().addAll(data);

			VBox vbox = new VBox(tableView);
			Scene scene = new Scene(vbox);
			primaryStage.setScene(scene);

			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<GhcndDailyView> getData() throws ClientProtocolException, IOException {
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

		return ghcndMapped;
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
