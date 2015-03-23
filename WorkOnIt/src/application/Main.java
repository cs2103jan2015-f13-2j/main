package application;

import java.awt.SystemTray;
import java.util.ArrayList;
import java.util.List;

import data.InitFileIO;
import resource.FileName;
import resource.Message;
import entity.Success;
import entity.Task;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
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
			
			// UI 
			
			
			// UI - LIST VIEW    
		    listView.setId("listView");
	        listView.setPrefSize(600, 250);
	        listView.setOpacity(0);
	        listView.setEditable(true);
			listView.setLayoutY(50);       
		
			
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
					addTaskToListView(listView,successObj);

				}
			});

			// onKeyPressed for each char entered
			txtF.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					switchListView(listView , txtF);
					//addDataToListView();
					commandHandler(event, txtF.getText(), primaryStage, txtF);
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
			final Stage stage, TextField txtF) {
		if (event.getCode().equals(KeyCode.ESCAPE)) {
			hide(stage);
		} 
		// detects a space, handle new word
		if (event.getText().equals(" ")) {
			wordHandler(txtF, textFieldText);
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

	//for running via Main, bypassing hotkey
	public static void main(String[] args) {
		InitFileIO initFile = new InitFileIO();
		initFile.checkAndProcessFile();
		launch();
	}
	
	private static void switchListView(ListView listView , TextField textField) {
		if (textField.getText().length() != 0 )
		{			
			listView.setOpacity(1);
		}
		else
		{
			listView.setOpacity(0);
		}
	}
	
	private static void addTaskToListView(ListView listView ,  Success successObj) {
		 ObservableList task =  FXCollections.observableArrayList();
		 ArrayList<Task> allTask = (ArrayList<Task>)successObj.getObj();
		 for(int i = 0 ;  i < allTask.size() ; i ++)
		 {
			 task.addAll( (i+1) + " ." + allTask.get(i).getTaskName());
			 
		 }
		 
		 listView.setItems(task);
	}
	
	
}
