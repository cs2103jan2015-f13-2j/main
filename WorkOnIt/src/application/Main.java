package application;
	
import entity.Task;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import logic.Validator;


public class Main extends Application {
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
	               commandHandler(txtF.getText());
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
	
	public static void commandHandler(String command)
	{
		Task task = null;
		Validator commandValidator = new Validator();
		Task obj  = (Task) commandValidator.parseCommand(command);
		//System.out.println(obj.getTaskName()
		
		
	}
	
}
