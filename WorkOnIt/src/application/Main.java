package application;

import java.util.ArrayList;
import java.util.List;

import entity.Success;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import logic.Validator;

import org.jnativehook.GlobalScreen;
public class Main extends Application {

	// need a global variable for the input
	static List<String> elementList = new ArrayList<String>();
	static List<String> secondaryList = new ArrayList<String>();
	KeyListener listener = new KeyListener();
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			listener.registerHook();
			Pane root = new Pane();
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			Scene scene = new Scene(root, 300, 100);
			scene.getStylesheets().add(
					Main.class.getResource("../css/application.css")
							.toExternalForm());
			scene.setFill(null);

			final TextField txtF = new TextField();
			txtF.setId("textField");
			txtF.setLayoutX(0);
			txtF.setLayoutY(0);
			txtF.setPrefWidth(300);
			txtF.setText("input here");
			txtF.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {

					System.out.println("textfield Text: " + txtF.getText());
					wordHandler(txtF.getText());
					executeCommand(txtF.getText());
					txtF.clear();

				}
			});

			// onKeyPressed for each char entered
			txtF.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					commandHandler(event, txtF.getText());
				}
			});

			root.getChildren().add(txtF);

			// primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void executeCommand(String commandString) {
		Success successCheck = null;
		Validator commandValidator = new Validator();
		Object temp = commandValidator.parseCommand(commandString);

		if (temp instanceof Success) {
			successCheck = (Success) temp;
			if (successCheck.isSuccess() == false) {
				System.out.println(successCheck.getMessage());
			} else {
				System.out.println("Command executed successfully");
			}
		} else {
			System.out.println("failed");
		}

		// System.out.println(obj.getTaskName()

	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void commandHandler(KeyEvent event, String textFieldText) {

		// detects a space, handle new word
		if (event.getText().equals(" ")) {
			wordHandler(textFieldText);
		}

	}

	private static void wordHandler(String textFieldText) {

		Validator commandValidator = new Validator();
		String[] stringArr = textFieldText.trim().split(" ");

		// iterate thru the input
		for (int i = 0; i < stringArr.length; i++) {
			elementList.add(stringArr[i]);

			// check if current word is a keyword
			if (commandValidator.validateKeyword(stringArr[i])) {

				// this method is for fencing the keyword (will implement later)
				handleMethod("Handling: " + stringArr[i]);

			}

		}
	}

	// just a proof of concept the thing works
	private static void handleMethod(String input) {
		System.out.println(input);
	}

}
