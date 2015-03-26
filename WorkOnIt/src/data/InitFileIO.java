package data;

import java.io.File;

import application.ChooseFolder;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import resource.FileName;

public class InitFileIO {

	public void checkAndProcessFile() {
		System.out.println("checkfilepath");
		FileName.readCanonicalPathFromFile();

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

}
