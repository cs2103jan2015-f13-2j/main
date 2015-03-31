package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import resource.FileName;
import resource.Message;

public class ConfigIO {

	public Map<String, String> getFullKeywordMap() {

		String loadedContents = loadFromFile();

		@SuppressWarnings("unchecked")
		Map<String, String> KEYWORD_MAP = (Map<String, String>) Serializer
				.deserializeFromJson(loadedContents, HashMap.class);

		return KEYWORD_MAP;
	}

	private String loadFromFile() {

		String loadedContents = null;
		String filenameCfg = FileName.getFilenameCfg();

		File configFile = new File(filenameCfg);

		try {

			if (configFile.exists()) {

				Scanner fileScanner = new Scanner(configFile);

				loadedContents = fileScanner.nextLine();
				fileScanner.close();

			} else {
				createNewFile(configFile);
				System.err.println(Message.FILE_CREATED);
			}
		} catch (FileNotFoundException e) {
			System.err.println(Message.FILE_NOT_FOUND);
		}

		return loadedContents;
	}

	private void createNewFile(File file) {

		try {
			file.createNewFile();

		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
