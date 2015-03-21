package data;

import java.io.File;

import application.FolderChooser;
import javafx.application.Application;
import javafx.stage.FileChooser;
import resource.FileName;
import resource.Message;
import entity.Success;

public class InitFileIO {
	
	public InitFileIO() {
		processFile();
	}
	
	private void processFile() {
		
		if(isFilesExist()) {
		} else {
			
			Application.launch(FolderChooser.class);
			System.out.println(FileName.getCanonicalPath());
		}
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
