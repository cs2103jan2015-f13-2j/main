package application;
	
import java.io.IOException;

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
	static String inputText="";
	
	
	
	
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
	            	commandHandlerOld(txtF.getText());
	               txtF.clear();
	            }
	        });
			
			//onKeyPressed for each char entered
			txtF.setOnKeyPressed(new EventHandler<KeyEvent>() {
	            public void handle(KeyEvent event) {
	            	//commandHandler(event);
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
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void commandHandler(KeyEvent event)
	{
		//get current char
		inputText+=event.getText();
		
		//detects a space
		if(event.getText().equals(" ")){
			
			//if program detects a command, call a handler
			if(inputText.equals("add"+ " ")){
				
				//this method is for fencing the keyword (will implement later)
				handleMethod(inputText);
				
				//Task task = null;
				//Validator commandValidator = new Validator();
				//Task obj  = (Task) commandValidator.parseCommand(inputText);
				//System.out.println(obj.getTaskName()
			}
		}
		
		
	}
	
	//to be removed once testing are done
 	public static void commandHandlerOld(String command)
 	{
		Task task = null;
 		Validator commandValidator = new Validator();
		commandValidator.parseCommand(command);
		//System.out.println(obj.getTaskName()
		
		
		//commandValidator.parseCommand(command);
 	}

	
	
	//just a proof of concept the thing works
	private static void handleMethod(String input){
		System.out.println(input);
	}
	
}