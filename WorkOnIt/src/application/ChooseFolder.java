package application;

import java.io.File;
import java.io.IOException;

import resource.FileName;
import resource.Message;
import javafx.application.Application;
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
				FileName.setCanonicalPath(selectedDirectory.getCanonicalPath());
			} catch (IOException e) {
				System.err.println(Message.FAIL_FILES_EXIST);
				System.exit(1);
			}
		} else {
			System.exit(1);

		}

	}
}
