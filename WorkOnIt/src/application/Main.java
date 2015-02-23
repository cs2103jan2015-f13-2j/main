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
	static String inputText = "";
	static String commandText = "";
	static String tempText ="";
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
		//get current char
		inputText+=event.getText();
		Validator commandValidator = new Validator(); 
		
		//detects a space
		if(event.getText().equals(" ")){
			
			tempText = textFieldText.trim();
			String[] stringArr = tempText.split(" ");
	
			
			for(int i =0;i<stringArr.length;i++){
				if(!elementList.contains(stringArr[i])){
					elementList.add(stringArr[i]);
					if(commandValidator.validateKeyword(stringArr[i])){
						
						
						//this method is for fencing the keyword (will implement later)
						handleMethod("Handling: "+tempText);
						
						commandText+=tempText;
						
					}
				}
			}
			
			for(int i=0;i<elementList.size();i++){
				System.out.println(elementList.get(i));
			}
			
			//if program detects a keyword, call a handler
			
			
			inputText = "";
		}
		
		
	}
	
	//just a proof of concept the thing works
	private static void handleMethod(String input){
		System.out.println(input);
	}
	
}
