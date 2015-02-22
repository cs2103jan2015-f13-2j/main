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
//			txtF.setOnAction(new EventHandler<ActionEvent>() {
//	            public void handle(ActionEvent event) {
//	               commandHandler(txtF.getText());
//	               txtF.clear();
//	            }
//	        });
			
			//onKeyPressed for each char entered
			txtF.setOnKeyPressed(new EventHandler<KeyEvent>() {
	            public void handle(KeyEvent event) {
	            	commandHandler(event);
	            	txtF.clear();
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
		inputText+=event.getText();
		if(inputText.equals("add")){
			
			openAddWindow("add");
			
			
			
			//Task task = null;
			//Validator commandValidator = new Validator();
			//Task obj  = (Task) commandValidator.parseCommand(command);
			//System.out.println(obj.getTaskName()
		}
		
	}
	
	//just a proof of concept the thing works
	private static void openAddWindow(String windowTitle){
    	
		try {
			Pane root = new Pane();
			Stage stage = new Stage();
			Scene scene = new Scene(root,300,100);
			scene.getStylesheets().add(Main.class.getResource("../css/application.css").toExternalForm());
			scene.setFill(null);
	        
			final TextField txtF = new TextField();
			txtF.setId("textField");
			txtF.setLayoutX(0);
			txtF.setLayoutY(0);
			txtF.setPrefWidth(300);
			txtF.setText("input here");
//			txtF.setOnAction(new EventHandler<ActionEvent>() {
//	            public void handle(ActionEvent event) {
//	               commandHandler(txtF.getText());
//	               txtF.clear();
//	            }
//	        });
			txtF.setOnKeyPressed(new EventHandler<KeyEvent>() {
				 public void handle(KeyEvent event) {
		            	commandHandler(event);
		            	txtF.clear();
			            }
		        });

			root.getChildren().add(txtF);
			
	        //primaryStage.initStyle(StageStyle.TRANSPARENT);		
			stage.setScene(scene);
			stage.show();
		}
		catch(Exception e){
			
		}
      
    }
	
}
