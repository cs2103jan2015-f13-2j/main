package application;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import data.InitFileIO;
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

	//@author A0111916M
	@Override
	public void start(final Stage primaryStage) {

		Platform.setImplicitExit(false);

		boolean isChoose = showChooseFolderConfirmation(primaryStage);

		if (isChoose) {
			openFileChooser(primaryStage);
			createWebUiFiles();
			notifySuccess();
		} else {
			System.exit(0);
		}
	}

	/**
	 * This method calls the confirmation dialog
	 * 
	 * @param primaryStage
	 *            UI primary stage
	 * @return boolean value of true for YES and false for NO
	 */

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

	/**
	 * Show up an alert box
	 */

	private void notifySuccess() {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(Message.UI_SUCCESS_ALERT_TITLE);
		alert.setHeaderText(null);
		alert.setContentText(Message.UI_SUCCESS_ALERT_INFO);

		alert.show();
	}

	/**
	 * This opens up a FileChooser dialog
	 * 
	 * @param primaryStage
	 *            UI primary stage
	 */

	private void openFileChooser(Stage primaryStage) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(Message.UI_CHOOSE_FOLDER);
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

	/**
	 * Create the necessary web UI files, for viewing agenda
	 */
	
	private static void createWebUiFiles() {

		try {

			File cssFolder = new File(FileName.getFolderCss());
			File jsFolder = new File(FileName.getFolderJs());

			processFileCreation(cssFolder, jsFolder);
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Perform the necessary process for save path's directories
	 */

	private static void processFileCreation(File cssFolder, File jsFolder)
			throws IOException {
		
		if (!cssFolder.exists()) {
			cssFolder.mkdirs();
		}

		if (!jsFolder.exists()) {
			jsFolder.mkdirs();
		}
		copyFilesToLocal();
	}
	
	/**
	 * Copy out the necessary files to local save directory
	 */

	private static void copyFilesToLocal() throws IOException {
		InitFileIO.copyFileUsingFileStreams(
				FileName.getFileNameMomentProgram(),
				FileName.getFileNameMomentLocal());
		InitFileIO.copyFileUsingFileStreams(
				FileName.getFileNameJqueryProgram(),
				FileName.getFileNameJqueryLocal());
		InitFileIO.copyFileUsingFileStreams(
				FileName.getFileNameFullCalendarJsProgram(),
				FileName.getFileNameFullCalendarJsLocal());
		InitFileIO.copyFileUsingFileStreams(
				FileName.getFileNameCalendarViewProgram(),
				FileName.getFileNameCalendarViewLocal());
		InitFileIO.copyFileUsingFileStreams(
				FileName.getFileNameFullCalendarCssProgram(),
				FileName.getFileNameFullCalendarCssLocal());
	}
}
