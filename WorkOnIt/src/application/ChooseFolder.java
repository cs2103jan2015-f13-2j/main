package application;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import resource.FileName;
import resource.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ChooseFolder extends Application {

	@Override
	public void start(final Stage primaryStage) {

		Platform.setImplicitExit(false);

		boolean isChoose = showChooseFolderConfirmation(primaryStage);

		if (isChoose) {
			openFileChooser(primaryStage);
			notifySuccess();
		} else {
			System.exit(0);
		}
	}

	private boolean showChooseFolderConfirmation(final Stage primaryStage) {

		boolean isYes = false;

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(Message.UI_WELCOME_TITLE);
		alert.setHeaderText(null);
		alert.setContentText(Message.UI_WELCOME_INFO_NO_FILE_FOUND);

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			isYes = true;
		} else {
			isYes = false;
		}

		return isYes;
	}

	private void notifySuccess() {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(Message.UI_SUCCESS_ALERT_TITLE);
		alert.setHeaderText(null);
		alert.setContentText(Message.UI_SUCCESS_ALERT_INFO);

		alert.show();
	}

	private void openFileChooser(Stage primaryStage) {
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
