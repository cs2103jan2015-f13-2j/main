package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;

import application.ChooseFolder;
import application.Main;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import resource.FileName;
import resource.Message;
import entity.Success;

public class InitFileIO {

	public void checkAndProcessFile() {

		readCanonicalPathFromFile();

		if (!isFilesExist()) {
			showChooseFolderUi();
		}
	}

	private void showChooseFolderUi() {
		new JFXPanel();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				new ChooseFolder().start(new Stage());
			}
		});
	}

	private boolean isFilesExist() {

		boolean isExist = true;

		File fileNormal = new File(FileName.getFilenameNormal());
		File fileFloating = new File(FileName.getFilenameFloating());
		File fileRecur = new File(FileName.getFilenameRecur());
		File fileDeadline = new File(FileName.getFilenameDeadline());

		if (!fileNormal.exists()) {
			isExist = false;
		}

		if (!fileFloating.exists()) {
			isExist = false;
		}

		if (!fileRecur.exists()) {
			isExist = false;
		}

		if (!fileDeadline.exists()) {
			isExist = false;
		}

		return isExist;
	}

	private void readCanonicalPathFromFile() {

		File file = new File(FileName.FILENAME_PATH);

		if (file.exists()) {

			try {
				BufferedReader reader = new BufferedReader(new FileReader(
						FileName.FILENAME_PATH));

				String retrievedCanonicalPath = reader.readLine();
				FileName.setCanonicalPath(retrievedCanonicalPath);
				System.out.println("READ : " + FileName.getCanonicalPath());

				reader.close();

			} catch (IOException e) {
				System.err.println(Message.ERROR_SAVE_INTO_FILE);
			}

		} else {
			try {
				PrintWriter filewrite = new PrintWriter(new BufferedWriter(
						new FileWriter(FileName.FILENAME_PATH, true)));
				FileName.setDefaultCanonicalPath();
				filewrite.println(FileName.getCanonicalPath());
				filewrite.close();

			} catch (IOException e) {
				System.err.println(Message.ERROR_SAVE_INTO_FILE);
			}
		}
	}
}
