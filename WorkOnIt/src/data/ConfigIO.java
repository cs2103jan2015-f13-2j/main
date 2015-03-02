package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

public class ConfigIO {

	private static final String CFG_FILENAME = "command.cfg";

	public Map<String, String> getFullKeywordMap() {

		String loadedContents = loadFromFile();

		@SuppressWarnings("unchecked")
		Map<String, String> KEYWORD_MAP = (Map<String, String>) deserializeFromJson(
				loadedContents, HashMap.class);

		return KEYWORD_MAP;
	}

	private String loadFromFile() {

		String loadedContents = null;
		File configFile = new File(CFG_FILENAME);

		try {

			if (configFile.exists()) {
				Scanner fileScanner = new Scanner(configFile);

				loadedContents = fileScanner.nextLine();
				fileScanner.close();

			} else {
				createNewFile(configFile);
				System.err.println("loadFromFile: new file created.");
			}
		} catch (FileNotFoundException e) {
			System.err.println("loadFromFile: file not found.");
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

	private static String serializeToJson(Object object) {

		String json;

		Gson gson = new Gson();
		json = gson.toJson(object);

		return json;
	}

	private static <T> Object deserializeFromJson(String json, Class<T> type) {

		Gson gson = new Gson();
		Object object = gson.fromJson(json, type);

		return object;
	}
}
