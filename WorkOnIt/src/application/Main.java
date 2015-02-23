package application;
	
import java.io.IOException;
import java.util.ArrayList;

import entity.Task;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import logic.Validator;


public class Main extends Application {
	
	//need a global variable for the input
	static ArrayList<String> elementList = new ArrayList();
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Pane root = new Pane();
			
			Scene scene = new Scene(root,300,100);
			scene.getStylesheets().add(Main.class.getResource("../css/application.css").toExternalForm());
			scene.setFill(null);
			
			final TextField txtF = new TextField();
			txtF.setId("textField");
			txtF.setLayoutX(0);
			txtF.setLayoutY(0);
			txtF.setPrefWidth(300);
			txtF.setText("input here");
			txtF.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent event) {
	            	System.out.println("textfield Text: "+txtF.getText());
	            	wordHandler(txtF.getText());
	            	executeCommand(txtF.getText());
	            	txtF.clear();
	            }
	        });
			
			//onKeyPressed for each char entered
			txtF.setOnKeyPressed(new EventHandler<KeyEvent>() {
	            public void handle(KeyEvent event) {
	            	commandHandler(event, txtF.getText());
		            }
		        });

			root.getChildren().add(txtF);
			
	        //primaryStage.initStyle(StageStyle.TRANSPARENT);		
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void executeCommand(String commandString) {
		Task task = null;
		Validator commandValidator = new Validator();
		Task obj  = (Task) commandValidator.parseCommand(commandString);
		//System.out.println(obj.getTaskName()
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public static void commandHandler(KeyEvent event, String textFieldText){
		
		//detects a space, handle new word
		if(event.getText().equals(" ")){
			wordHandler(textFieldText);
		}
		
		
	}

	private static void wordHandler(String textFieldText) {
		
		Validator commandValidator = new Validator();
		String[] stringArr = textFieldText.trim().split(" ");

		//iterate thru the input
		for(int i =0;i<stringArr.length;i++){
			//find and add new word to a list if not already in it
			if(!elementList.contains(stringArr[i])){
				elementList.add(stringArr[i]);
				
				//check if current word is a keyword
				if(commandValidator.validateKeyword(stringArr[i])){
					
					//this method is for fencing the keyword (will implement later)
					handleMethod("Handling: "+stringArr[i]);
					
				}
			}
		}
	}
	
	
	
	//just a proof of concept the thing works
	private static void handleMethod(String input){
		System.out.println(input);
	}
	
}
