package application;

import java.awt.SystemTray;
import java.util.ArrayList;
import java.util.List;

import data.InitFileIO;
import resource.FileName;
import resource.Message;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
import entity.Success;
import entity.Task;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import logic.Validator;

public class Main extends Application {

	// need a global variable for the input
	private static List<String> elementList = null;
	private static List<String> secondaryList = null;
	private static Validator commandValidator = null;
	private static Success successObj = null;

	@Override
	public void start(final Stage primaryStage) {

		try {

			Pane root = new Pane();
			primaryStage.initStyle(StageStyle.TRANSPARENT);

			Scene scene = new Scene(root, 600, 550);
			root.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-background-radius: 10;");

			scene.getStylesheets().add(
					Main.class.getResource("../css/application.css")
							.toExternalForm());
			scene.setFill(null);

			// UI DECLARE
			final TextField txtF = new TextField();
			final TextFieldListCell cellText = new TextFieldListCell();
			final ListView listView = new ListView();

			initializeGlobals();

			// UI - LIST VIEW
			listView.setId("listView");
			listView.setPrefSize(600, 250);
			listView.setOpacity(0);
			listView.setEditable(true);
			listView.setLayoutY(50);
			listView.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					// addDataToListView();
					listHandler(event, primaryStage, txtF,  listView);
				}
			});

			
			// UI - TEXT FIELD
			txtF.setId("textField");
			txtF.setLayoutX(0);
			txtF.setLayoutY(0);
			txtF.setPrefHeight(50);
			txtF.setPrefWidth(600);
			txtF.setText(Message.UI_INPUT_HERE);
			txtF.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {

					System.out.println("textfield Text: " + txtF.getText());
					wordHandler(txtF, txtF.getText());
					executeCommand(txtF, txtF.getText());
					addTaskToListView(listView, successObj);
					switchListView(listView, txtF);

				}
			});

			// onKeyPressed for each char entered
			txtF.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					// addDataToListView();
					commandHandler(event, txtF.getText(), primaryStage, txtF,
							listView);
					switchListView(listView, txtF);
				}
			});
			// UI - TEXT FIELD

			root.getChildren().add(txtF);
			root.getChildren().add(listView);

			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initializeGlobals() {
		elementList = new ArrayList<String>();
		secondaryList = new ArrayList<String>();
		commandValidator = new Validator();
	}

	protected void executeCommand(TextField txtF, String commandString) {

		Success status = null;

		if (commandValidator.validateKeywordSequence(secondaryList) == true) {
			status = commandValidator.parseCommand(commandString);

			if (status.isSuccess() == false) {
				System.out.println(status.getMessage());
			} else {
				if (status.getObj() instanceof String) {
					String updateCommand = (String) status.getObj();
					txtF.setText(updateCommand);
				} else {
					txtF.clear();
				}
				System.out.println(Message.SUCCESS_COMMAND);
			}
		} else {
			System.out.println(Message.FAIL_PARSE_COMMAND);
		}

		successObj = status;
		// System.out.println(obj.getTaskName()

	}

	public static void commandHandler(KeyEvent event, String textFieldText,
			final Stage stage, TextField txtF, ListView listView) {
		if (event.getCode().equals(KeyCode.ESCAPE)) {
			hide(stage);
		}

		if (event.getCode().equals(KeyCode.UP)
				|| event.getCode().equals(KeyCode.DOWN)) {
			listView.requestFocus();
		}

		// detects a space, handle new word
		if (event.getText().equals(" ")) {
			wordHandler(txtF, textFieldText);
		}
	}

	public static void listHandler(KeyEvent event, final Stage stage,
			TextField txtF, ListView listView) {
		if (event.getCode().equals(KeyCode.ESCAPE)) {
			hide(stage);
		}

		if (event.getCode().equals(KeyCode.LEFT)
				|| event.getCode().equals(KeyCode.RIGHT)) {
			txtF.requestFocus();
		}
	}

	private static void wordHandler(TextField txtF, String textFieldText) {

		String[] stringArr = textFieldText.trim().split(" ");

		elementList.clear();
		secondaryList.clear();

		// iterate thru the input
		for (int i = 0; i < stringArr.length; i++) {
			elementList.add(stringArr[i]);

			// check if current word is a keyword
			if (commandValidator.validateKeyword(stringArr[i])) {
				txtF.setStyle("-fx-text-fill: red;");
				String currentKeyword = stringArr[i];

				secondaryList.add(currentKeyword);
				// this method is for fencing the keyword (will implement later)
				// handleMethod("Handling: " + stringArr[i]);

			} else {
				txtF.setStyle("-fx-text-fill: black;");
			}

		}
	}

	// just a proof of concept the thing works
	private static void handleMethod(String input) {
		System.out.println(input);
	}

	private static void hide(final Stage stage) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (SystemTray.isSupported()) {
					stage.setOnHiding(null);
					stage.hide();
					System.out.println("hide");
				} else {
					System.exit(0);
				}
			}
		});
	}

	// for running via Main, bypassing hotkey
	public static void main(String[] args) {
		InitFileIO initFile = new InitFileIO();
		initFile.checkAndProcessFile();
		launch();
	}

	private static void switchListView(ListView listView, TextField textField) {
		if (listView.getItems().size() != 0) {
			listView.setOpacity(1);
		} else {
			listView.setOpacity(0);
		}
	}

	private static void addTaskToListView(ListView listView, Success successObj) {
		if (successObj.getObj() == null) {
			listView.getItems().clear();
		} else {
			ObservableList task = FXCollections.observableArrayList();
			Object obj = successObj.getObj();
			if (obj instanceof ArrayList<?>) {
				ArrayList<Task> allTask = (ArrayList<Task>) obj;
				for (int i = 0; i < allTask.size(); i++)
				{
					String displayDate = getDateFromTask(allTask.get(i));
					
					Pane pane = new Pane();
					Label taskName = new Label();
					Label taskDate = new Label();
					pane.setMinWidth(600);
					
					taskName.setText((i + 1) + ") " + allTask.get(i).getTaskName());
					taskName.setAlignment(Pos.CENTER_RIGHT);
					taskName.setLayoutX(0);
					
					taskDate.setText(displayDate);
					taskDate.setAlignment(Pos.CENTER_RIGHT);
					taskDate.setLayoutX(300);
					
					pane.getChildren().add(taskName);
					pane.getChildren().add(taskDate);
					
					task.add(pane);
				}

				listView.setItems(task);
			} else if (obj instanceof String) {
				listView.getItems().clear();
			}
		}
	}
	
	public static String getDateFromTask(Task taskObj)
	{
		String displayText = null;
		if(taskObj instanceof NormalTask)
		{
			String startDate = Integer.toString(((NormalTask) taskObj).getStartDateTime().getDate());
			String startMonth = intToMonth(((NormalTask) taskObj).getStartDateTime().getMonth());
			
			String endDate = Integer.toString(((NormalTask) taskObj).getEndDateTime().getDate());
			String endMonth = intToMonth(((NormalTask) taskObj).getEndDateTime().getMonth());
			
			displayText = startMonth + " " + startDate + " to " + endMonth + " " + endDate ;		
		}
		
		if(taskObj instanceof FloatingTask)
		{
			displayText = "-";
		}
		
		if(taskObj instanceof RecurrenceTask)
		{
			
		}
		
		if(taskObj instanceof DeadlineTask)
		{
			String dueDate = Integer.toString(((DeadlineTask) taskObj).getDeadline().getDate());
			String dueMonth = intToMonth(((DeadlineTask) taskObj).getDeadline().getMonth());
			String dueHour =  Integer.toString(((DeadlineTask) taskObj).getDeadline().getHours());
			String dueMinute =  Integer.toString(((DeadlineTask) taskObj).getDeadline().getMinutes());
			
			displayText = "Due on " + dueMonth + " " + dueDate + " " + dueHour + dueMinute;
			
		}
		
		return displayText;
	}
	
	public static String intToMonth(int month)
	{
		String monthString;
        switch (month) {
            case 0:  monthString = "January";       break;
            case 1:  monthString = "February";      break;
            case 2:  monthString = "March";         break;
            case 3:  monthString = "April";         break;
            case 4:  monthString = "May";           break;
            case 5:  monthString = "June";          break;
            case 6:  monthString = "July";          break;
            case 7:  monthString = "August";        break;
            case 8:  monthString = "September";     break;
            case 9: monthString = "October";       break;
            case 10: monthString = "November";      break;
            case 11: monthString = "December";      break;
            default: monthString = "Invalid month"; break;
        }
        return monthString;
		
	}
	

}
