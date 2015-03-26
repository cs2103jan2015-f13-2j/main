package resource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.google.gson.Gson;

public class FileName {

	public static final String FILENAME_PATH = "saved_path.cfg";

	private static String canonicalPath = "";
	private static String filenameDeadline = "datafile_deadLine.txt";
	private static String filenameFloating = "datafile_floating.txt";
	private static String filenameNormal = "datafile_normal.txt";
	private static String filenameRecur = "datafile_recur.txt";
	private static String filenameCfg = "command.cfg";

	public static String getFilenameDeadline() {
		return getCanonicalPath() + File.separator + filenameDeadline;
	}

	public static void setFilenameDeadline(String filenameDeadline) {
		FileName.filenameDeadline = filenameDeadline;
	}

	public static String getFilenameFloating() {
		return getCanonicalPath() + File.separator + filenameFloating;
	}

	public static void setFilenameFloating(String filenameFloating) {
		FileName.filenameFloating = filenameFloating;
	}

	public static String getFilenameNormal() {
		return getCanonicalPath() + File.separator + filenameNormal;
	}

	public static void setFilenameNormal(String filenameNormal) {
		FileName.filenameNormal = filenameNormal;
	}

	public static String getFilenameRecur() {
		return getCanonicalPath() + File.separator + filenameRecur;
	}

	public static void setFilenameRecur(String filenameRecur) {
		FileName.filenameRecur = filenameRecur;
	}

	public static String getFilenameCfg() {
		return getCanonicalPath() + File.separator + filenameCfg;
	}

	public static void setFilenameCfg(String filenameCfg) {
		FileName.filenameCfg = filenameCfg;
	}

	public static String getCanonicalPath() {
		return canonicalPath;
	}

	public static void setCanonicalPath(String canonicalPath) {
		FileName.canonicalPath = canonicalPath;
	}

	public static void setDefaultCanonicalPath() {
		try {
			canonicalPath = new File(".").getCanonicalPath();
		} catch (IOException e) {
			System.err.println(Message.ERROR_GENERAL);
		}
	}

	public static void writeCanonicalToFile() {
		try {
			File file = new File(FILENAME_PATH);
			file.delete();
			file.createNewFile();

			PrintWriter filewrite = new PrintWriter(new BufferedWriter(
					new FileWriter(FILENAME_PATH, true)));
			filewrite.println(getCanonicalPath());
			filewrite.close();

		} catch (IOException e) {
			System.err.println(Message.ERROR_SAVE_INTO_FILE);
		}
	}

	public static String readCanonicalPathFromFile() {
		
		File file = new File(FileName.FILENAME_PATH);
		String retrievedCanonicalPath = null;

		if (file.exists()) {

			try {
				BufferedReader reader = new BufferedReader(new FileReader(
						FileName.FILENAME_PATH));

				retrievedCanonicalPath = reader.readLine();
				FileName.setCanonicalPath(retrievedCanonicalPath);
				System.out.println("READ : " + getCanonicalPath());

				reader.close();

			} catch (IOException e) {
				System.err.println(Message.ERROR_SAVE_INTO_FILE);
			}

		} else {
			setDefaultCanonicalPath();
			writeCanonicalToFile();
		}

		return retrievedCanonicalPath;
	}

	public static void createFileIfNotExist() {

		try {
			createFile(FileName.getFilenameDeadline());
			createFile(FileName.getFilenameFloating());
			createFile(FileName.getFilenameNormal());
			createFile(FileName.getFilenameRecur());
			createFile(FileName.getFilenameCfg());
		} catch (IOException e) {
			System.err.println(Message.ERROR_GENERAL);
			System.exit(1);
		}
	}

	private static void createFile(String canonicalFileName) throws IOException {
		File file = new File(canonicalFileName);

		if (!file.exists()) {
			if (canonicalFileName.equals(FileName.getFilenameCfg())) {
				file.delete();
				file.createNewFile();

				Map<String, String> basicCommandMap = KeywordConstant
						.createBasicMap();
				String serializedBasicCommand = serializeToJson(basicCommandMap);

				PrintWriter filewrite = new PrintWriter(new BufferedWriter(
						new FileWriter(canonicalFileName, true)));
				filewrite.println(serializedBasicCommand);
				filewrite.close();
			} else {
				file.createNewFile();
			}
		}
	}

	private static String serializeToJson(Object object) {

		String json;

		Gson gson = new Gson();
		json = gson.toJson(object);

		return json;
	}
}
