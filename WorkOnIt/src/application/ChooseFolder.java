package application;

import java.io.File;
import java.io.IOException;

import data.InitFileIO;
import resource.FileName;
import resource.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Dialogs.DialogResponse;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChooseFolder extends Application {

	@Override
	public void start(final Stage primaryStage) {

		DialogResponse response = Dialogs.showConfirmDialog(primaryStage,
				Message.UI_WELCOME_INFO_NO_FILE_FOUND, null,
				Message.UI_WELCOME_TITLE);
		
		if(response.equals(response.YES)) {
			openFileChooser(primaryStage);
		} else {
			System.exit(0);
		}
	}

	protected void openFileChooser(Stage primaryStage) {
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
