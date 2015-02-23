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
import javafx.scene.text.Text;
import logic.Validator;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Pane root = new Pane();
			
			Scene scene = new Scene(root,400,200);
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
			
			final Text displayText = new Text();
			displayText.setText("1. Due Assignment \t 3 March \n2. Finish Homework");
			displayText.setLayoutX(10);
			displayText.setLayoutY(50);
			
			
			root.getChildren().add(txtF);
			root.getChildren().add(displayText);
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
		Validator commandValidator = new Validator();
		commandValidator.parseCommand(command);
	}
	
}
