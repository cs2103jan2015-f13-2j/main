package application;

import java.io.File;

import resource.FileName;
import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class FolderChooser extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Choose Save Directory");
		File selectedDirectory = directoryChooser.showDialog(primaryStage);
		
		if(selectedDirectory != null) {
			FileName.setCanonicalPath(selectedDirectory.getCanonicalPath());
			System.out.println("new path : " + FileName.getCanonicalPath());
		}
		
	}
}
