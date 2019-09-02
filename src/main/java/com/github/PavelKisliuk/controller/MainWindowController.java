package com.github.PavelKisliuk.controller;

import com.github.PavelKisliuk.evaluator.LogEvaluator;
import com.github.PavelKisliuk.io.LogReader;
import com.github.PavelKisliuk.io.LogWriter;
import com.github.PavelKisliuk.model.Log;
import com.github.PavelKisliuk.predicate.LogPredicate;
import com.github.PavelKisliuk.util.LogStatistic;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainWindowController {
	@FXML
	private Button filterButton;

	@FXML
	private TextArea timeStatisticArea;

	@FXML
	private TextArea logArea;

	@FXML
	private TextArea nameStatisticArea;

	private InputController inputController;

	@FXML
	void initialize() {
		filterButton.setOnAction(actionEvent -> filterButtonOnAction());
	}

	private void filterButtonOnAction() {
		String path = "/fxml/Input.fxml";
		try {
			//primaryStage adjustment
			//-----------------------------------------------
			Stage dialogueStage = new Stage();
			dialogueStage.setResizable(false);
			dialogueStage.sizeToScene();
			dialogueStage.centerOnScreen();

			//FXML adjustment
			//-----------------------------------------------
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource(path));
			Parent fxmlDialogueWindow = fxmlLoader.load();

			inputController = fxmlLoader.getController();

			//modality adjustment
			//-----------------------------------------------
			dialogueStage.initModality(Modality.WINDOW_MODAL);
			dialogueStage.initOwner(filterButton.getScene().getWindow());

			//start-up window
			//-----------------------------------------------
			Scene choice = new Scene(fxmlDialogueWindow);
			dialogueStage.setScene(choice);
			dialogueStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!inputController.isCancel()) {
			timeStatisticArea.clear();
			nameStatisticArea.clear();
			logArea.clear();
			aggregate();
		}
	}

	private void aggregate() {
		String pathToLog = inputController.getPathToLog();
		final String FILE_NAME = "/log_";
		LogReader logReader = new LogReader(pathToLog, FILE_NAME);
		HashMap<Integer, List<Log>> logGroup = new HashMap<>();
		int threadAmount = inputController.getAmountOfThread();
		ExecutorService executorService = Executors.newCachedThreadPool();
		for(int i = 0; i < threadAmount; i++) {
			executorService.execute(logReader);
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for(int i = 1; i <= logReader.size(); i++) {
			logGroup.put(i, logReader.get(i));
		}

		LogPredicate logPredicate = new LogPredicate();
		logPredicate.setDateBefore(inputController.getDateBefore());
		logPredicate.setDateAfter(inputController.getDateAfter());
		logPredicate.setUserName(inputController.getUserName());
		logPredicate.setPattern(inputController.getPattern());

		LogEvaluator logEvaluator = new LogEvaluator(logGroup, logPredicate);
		executorService = Executors.newCachedThreadPool();
		for(int i = 0; i < threadAmount; i++) {
			executorService.execute(logEvaluator);
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		List<Log> list = logEvaluator.toList();
		LogStatistic logStatistic = new LogStatistic(list);
		if(inputController.isNameStatistic()) {
			Map<String, Integer> map = logStatistic.nameStatistic();
			if(!map.isEmpty()) {
				map.forEach((k, v) -> nameStatisticArea.appendText(k + "-----" + v + "\n"));
			} else {
				nameStatisticArea.appendText("No such data");
			}
		}
		if(inputController.isTimeStatistic()) {
			Map<String, Integer> map = logStatistic.timeStatistic(inputController.getTimePeriod());
			if(!map.isEmpty()) {
				map.forEach((k, v) -> timeStatisticArea.appendText(correctDate(k, inputController.getTimePeriod()) +
						"-----" + v + "\n"));
			} else {
				timeStatisticArea.appendText("No such data");
			}
		}

		if(!list.isEmpty()) {
			for(int i = 0; i < list.size() && i < 1_000; i++) {
				logArea.appendText(list.get(i).toString() + "\n");
			}
		} else {
			logArea.appendText("No such data");
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		LogWriter logWriter = new LogWriter(list);
		logWriter.write(inputController.getOutputPath());
	}

	private String correctDate(String date, LogStatistic.TimePeriodType timePeriod) {
		String correct;
		switch (timePeriod) {
			case HOUR:
				correct = date.substring(0, date.length() - 3);
				break;
			case DAY:
				correct = date.substring(0, date.length() - 6);
				break;
			case MONTH:
				correct = date.substring(0, date.length() - 9);
				break;
			case YEAR:
				correct = date.substring(0, date.length() - 12);
				break;
			default:
				throw new EnumConstantNotPresentException(LogStatistic.TimePeriodType.class,
						"Incorrect TimePeriodType in MainWindowController -> correctDate(String, TimePeriodType)");
		}
		return correct;
	}
}