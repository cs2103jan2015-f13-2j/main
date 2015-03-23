package application;

import java.io.File;
import java.io.IOException;

import data.InitFileIO;
import resource.FileName;
import resource.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ChooseFolder extends Application {

	@Override
	public void start(Stage primaryStage) {

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Choose Save Directory");
		File selectedDirectory = directoryChooser.showDialog(primaryStage);

		if (selectedDirectory != null) {
			try {
				String directoryPath = selectedDirectory.getCanonicalPath();
				FileName.setCanonicalPath(directoryPath);
				FileName.writeCanonicalToFile();
				
				FileName.createFileIfNotExist();
				
			} catch (IOException e) {
				System.err.println(Message.FAIL_FILES_EXIST);
				System.exit(1);
			}
		} else {
			System.exit(1);

		}
	}
	
	
}
