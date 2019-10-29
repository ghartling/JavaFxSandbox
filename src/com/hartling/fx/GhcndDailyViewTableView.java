package com.hartling.fx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

			// fields at top
			Label testlabel = new Label("test");
			HBox hbox = addHBox(testlabel);

			// table
			VBox vbox = new VBox(tableView);
			vbox.getChildren().add(testlabel);
			vbox.setPadding(new Insets(10)); // Set all sides to 10
			vbox.setSpacing(10); // Gap between nodes
			border.setTop(hbox);
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

	public HBox addHBox(Label label) {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #A0D9E9;");

		// create a label
		Label stnLabel = new Label("STN:");
		TextField stnField = createTextField(hbox, stnLabel);
		Label startDateLabel = new Label("Start:");
		DatePicker startDatePicker = createDatePicker(stnLabel);
		Label endDateLabel = new Label("End:");
		DatePicker endDatePicker = createDatePicker(stnLabel);

		Button goButton = new Button("Go");
		goButton.setPrefSize(100, 20);

		// action event
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				LocalDate start = startDatePicker.getValue();
				LocalDate end = endDatePicker.getValue();

				label.setText(stnField.getText() + ", " + start + ", " + end);
			}
		};

		goButton.setOnAction(event);

		// add fields
		hbox.getChildren().addAll(stnLabel, stnField, startDateLabel, startDatePicker, endDateLabel, endDatePicker, goButton);

		return hbox;
	}

	private DatePicker createDatePicker(Label l) {
		// create a date picker
		DatePicker d = new DatePicker();

		// action event
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// get the date picker value
				LocalDate i = d.getValue();

				// get the selected date
				l.setText("Date :" + i);
			}
		};

		// show week numbers
		d.setShowWeekNumbers(true);

		// when datePicker is pressed
		d.setOnAction(event);

		return d;
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

	private TextField createTextField(HBox hbox, Label l) {
		// create a textfield
		TextField b = new TextField("initial text");

		// action event
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				l.setText(b.getText());
			}
		};

		// when enter is pressed
		b.setOnAction(event);

		// hbox.getChildren().addAll(b, l);

		return b;
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
