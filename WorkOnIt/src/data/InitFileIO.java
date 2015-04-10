package data;

import java.io.File;
import java.io.FileInputStream;
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
	 * @param
	 * @return
	 */
	// @author
	public void checkAndProcessFile() {

		FileName.readCanonicalPathFromFile();

		if (!isFilesExist()) {
			showChooseFolderUi();
		}
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
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
	 * @param
	 * @return
	 */
	// @author
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
	
	 
	public static void copyFileUsingFileStreams(String sourceString, String destString)
			throws IOException {
		
		File source = new File(sourceString);
		File dest = new File(destString);
		
		System.out.println("initfileIO : " + source.getPath());
		
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(source);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			input.close();
			output.close();
		}
	}

}
