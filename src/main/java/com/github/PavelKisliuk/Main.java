package com.github.PavelKisliuk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public void start(Stage primaryStage) throws Exception {
		String title = "Log Aggregator";
		String path = "/fxml/MainWindow.fxml";
		//primaryStage adjustment
		//-----------------------------------------------
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.setTitle(title);
		primaryStage.centerOnScreen();

		//FXML adjustment
		//-----------------------------------------------
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource(path));
		Parent fxmlMainWindow = fxmlLoader.load();

		//start-up window
		//-----------------------------------------------
		Scene s = new Scene(fxmlMainWindow);
		primaryStage.setScene(s);
		primaryStage.show();
	}

	public static void main(String... args) {
		launch(args);
	}
}