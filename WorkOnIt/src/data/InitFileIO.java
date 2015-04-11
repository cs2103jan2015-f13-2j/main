package data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import application.ChooseFolder;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import resource.FileName;

public class InitFileIO {
	/**
	 *
	 * check and process all the data file.
	 *
	 * @param
	 * @return
	 */
	// @author A0111916M
	public void checkAndProcessFile() {

		FileName.readCanonicalPathFromFile();

		if (!isFilesExist()) {
			showChooseFolderUi();
		}
	}

	/**
	 *
	 * prompt user the choose folder UI for user to choose UI.
	 *
	 *
	 * @param
	 * @return
	 */
	// @author A0111916M
	private void showChooseFolderUi() {
		new JFXPanel();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				new ChooseFolder().start(new Stage());
			}
		});
	}

	/**
	 *
	 * Check whether the data file is exist
	 *
	 * @return boolean true if the datafile exist , else return false.
	 */
	// @author A0111916M
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

	/**
	 *
	 * Copy data file to the specific path
	 *
	 * @return String the starting destination of the file that need to be copy
	 * @return String the ending destination of the file that need to be copy
	 * 
	 */
	// @author A0111916M
	public static void copyFileUsingFileStreams(String sourceString,
			String destString) throws IOException {

		assert(destString != null);
		
		File dest = new File(destString);

		if (!dest.exists()) {
			dest.createNewFile();
		}

		InputStream input = null;
		OutputStream output = null;
		try {
			input = ChooseFolder.class.getResourceAsStream(sourceString);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			input.close();
			output.close();
		}
	}

}
