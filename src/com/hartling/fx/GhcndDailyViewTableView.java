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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

			// layout
			BorderPane border = new BorderPane();

			HBox hbox = addHBox();
//			border.setTop(hbox);
//			border.setLeft(addVBox());

			VBox vbox = new VBox(tableView);
			border.setTop(hbox);
//			border.setTop(vbox);
			// border.setLeft(addVBox());
			border.setLeft(vbox);

			// scene
			// Scene scene = new Scene(vbox);
			Scene scene = new Scene(border);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Weather");

			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HBox addHBox() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #336699;");

		Button buttonCurrent = new Button("Current");
		buttonCurrent.setPrefSize(100, 20);

		Button buttonProjected = new Button("Projected");
		buttonProjected.setPrefSize(100, 20);
		hbox.getChildren().addAll(buttonCurrent, buttonProjected);

		return hbox;
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
