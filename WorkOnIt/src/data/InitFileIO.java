package data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import application.ChooseFolder;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import resource.FileName;

public class InitFileIO {

	private static final Logger LOGGER = Logger.getLogger(InitFileIO.class
			.getName());

	/**
	 *
	 * check and process all the data file.
	 *
	 * @param
	 * @return
	 */
	// @author A0112694E
	public void checkAndProcessFile() {

		LOGGER.fine("Checking required files on init");

		FileName.readCanonicalPathFromFile();

		if (!isFilesExist()) {
			LOGGER.warning("File(s) does not exist. Request save path.");
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
	
	private boolean isFilesExist() {

		boolean isExist = true;

		File fileNormal = new File(FileName.getFilenameNormal());
		File fileFloating = new File(FileName.getFilenameFloating());
		File fileRecur = new File(FileName.getFilenameRecur());
		File fileDeadline = new File(FileName.getFilenameDeadline());

		if (!fileNormal.exists()) {
			LOGGER.warning("normal task data file does not exist");
			isExist = false;
		}

		if (!fileFloating.exists()) {
			LOGGER.warning("floating task data file does not exist");
			isExist = false;
		}

		if (!fileRecur.exists()) {
			LOGGER.warning("recurrence task data file does not exist");
			isExist = false;
		}

		if (!fileDeadline.exists()) {
			LOGGER.warning("deadline task data file does not exist");
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
	
	public static void copyFileUsingFileStreams(String sourceString,
			String destString) throws IOException {

		assert (sourceString != null);
		assert (destString != null);

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
			System.err.println(e.getMessage());
		} finally {
			input.close();
			output.close();
		}
	}

}
