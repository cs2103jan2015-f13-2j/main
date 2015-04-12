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

import data.InitFileIO;

public class FileName {

	public static final String FILENAME_PATH = "saved_path.cfg";

	private static String canonicalPath = "";
	private static String filenameDeadline = "datafile_deadLine.txt";
	private static String filenameFloating = "datafile_floating.txt";
	private static String filenameNormal = "datafile_normal.txt";
	private static String filenameRecur = "datafile_recur.txt";
	private static String filenameCfg = "command.cfg";
	private static String filenameHistory = "history.txt";

	private static String localUrl = "file:\\\\\\";
	private static String relativeWebUiPath = "webUI";
	private static String filenameCalendarUi = "calendarView.html";

	private static String webUiPath = "/webUI/";
	private static String folderCss = "css";
	private static String folderJs = "js";
	private static String fileNameFullCalendarCss = "fullcalendar.css";
	private static String fileNameFullCalendarJs = "fullcalendar.js";
	private static String fileNameJquery = "jquery-2.1.3.min.js";
	private static String fileNameMoment = "moment.js";

	/**
	 * Returns the full path of deadline task(s) file, according to user save
	 * path.
	 * 
	 * @return Deadline full path of file
	 */
	// @author A0111916M
	public static String getFilenameDeadline() {
		return getCanonicalPath() + File.separator + filenameDeadline;
	}

	/**
	 * Set the full path of deadline task(s) file.
	 */

	public static void setFilenameDeadline(String filenameDeadline) {
		FileName.filenameDeadline = filenameDeadline;
	}

	/**
	 * Returns the full path of floating task(s) file, according to user save
	 * path.
	 * 
	 * @return Floating full path of file
	 */

	public static String getFilenameFloating() {
		return getCanonicalPath() + File.separator + filenameFloating;
	}

	/**
	 * Set the full path of floating task(s) file.
	 */

	public static void setFilenameFloating(String filenameFloating) {
		FileName.filenameFloating = filenameFloating;
	}

	/**
	 * Returns the full path of normal task(s) file, according to user save
	 * path.
	 * 
	 * @return Normal full path of file
	 */

	public static String getFilenameNormal() {
		return getCanonicalPath() + File.separator + filenameNormal;
	}

	/**
	 * Set the full path of normal task(s) file.
	 */

	public static void setFilenameNormal(String filenameNormal) {
		FileName.filenameNormal = filenameNormal;
	}

	/**
	 * Returns the full path of recurrence task(s) file, according to user save
	 * path.
	 * 
	 * @return Recurrence full path of file
	 */

	public static String getFilenameRecur() {
		return getCanonicalPath() + File.separator + filenameRecur;
	}

	/**
	 * Set the full path of recurrence task(s) file.
	 */

	public static void setFilenameRecur(String filenameRecur) {
		FileName.filenameRecur = filenameRecur;
	}

	/**
	 * Returns the full path of configuration file, according to user save path.
	 * 
	 * @return Configuration full path of file
	 */

	public static String getFilenameCfg() {
		return getCanonicalPath() + File.separator + filenameCfg;
	}

	/**
	 * Set the full path of configuration file.
	 */

	public static void setFilenameCfg(String filenameCfg) {
		FileName.filenameCfg = filenameCfg;
	}

	/**
	 * Returns the full path of history file, according to user save path.
	 * 
	 * @return History full path of file
	 */

	public static String getFilenameHistory() {
		return getCanonicalPath() + File.separator + filenameHistory;
	}

	/**
	 * Set the full path of History file.
	 */

	public static void setFilenameHistory(String filenameHistory) {
		FileName.filenameHistory = filenameHistory;
	}

	/**
	 * Returns the full path of the canonical saved path, according to user save
	 * path.
	 * 
	 * @return Canonical full path
	 */

	public static String getCanonicalPath() {
		return canonicalPath;
	}

	/**
	 * Set the canonical path.
	 */

	public static void setCanonicalPath(String canonicalPath) {
		FileName.canonicalPath = canonicalPath;
	}

	/**
	 * Set the default canonical path to be the current working directory.
	 */

	public static void setDefaultCanonicalPath() {
		try {
			canonicalPath = new File(".").getCanonicalPath();
		} catch (IOException e) {
			System.err.println(Message.ERROR_GENERAL);
		}
	}

	/**
	 * Save the user specified saved path for data files.
	 */

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

	/**
	 * Read the user specified saved path for data files.
	 */

	public static String readCanonicalPathFromFile() {

		File file = new File(FileName.FILENAME_PATH);
		String retrievedCanonicalPath = null;

		if (file.exists()) {

			try {
				BufferedReader reader = new BufferedReader(new FileReader(
						FileName.FILENAME_PATH));

				retrievedCanonicalPath = reader.readLine();
				FileName.setCanonicalPath(retrievedCanonicalPath);

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

	/**
	 * Returns the URL of the generated HTML file, according to user save path.
	 * 
	 * @return URL full path of HTML file
	 */

	public static String getLocalUrl() {
		return localUrl;
	}

	/**
	 * Set the URL of the generated HTML file.
	 * 
	 */

	public static void setLocalUrl(String localUrl) {
		FileName.localUrl = localUrl;
	}

	/**
	 * Returns the relative path of the generated HTML file, according to user
	 * save path.
	 * 
	 * @return Path of the generated HTML file
	 */

	public static String getRelativeWebUiPath() {
		return relativeWebUiPath;
	}

	/**
	 * Set the relative path of the generated HTML file.
	 * 
	 * @return Path of the generated HTML file
	 */

	public static void setRelativeWebUiPath(String relativeWebUiPath) {
		FileName.relativeWebUiPath = relativeWebUiPath;
	}

	/**
	 * Returns the full path of the generated HTML file, according to user save
	 * path.
	 * 
	 * @return Full path of the generated HTML file
	 */

	public static String getFilenameCalendarUi() {
		return getCanonicalPath() + File.separator + getRelativeWebUiPath()
				+ File.separator + FileName.filenameCalendarUi;
	}

	/**
	 * Returns the full URL of the generated HTML file, according to user save
	 * path.
	 * 
	 * @return Full URL of the generated HTML file
	 */

	public static String getFilenameCalendarUiUrl() {
		return getLocalUrl() + getCanonicalPath() + File.separator
				+ getRelativeWebUiPath() + File.separator
				+ FileName.filenameCalendarUi;
	}

	/**
	 * Set the full path of the generated HTML file.
	 * 
	 */

	public static void setFilenameMonthUi(String filenameMonthUi) {
		FileName.filenameCalendarUi = filenameMonthUi;
	}

	/**
	 * Returns the Web UI path.
	 * 
	 * @return Path of WebUI
	 */

	public static String getWebUiPath() {
		return webUiPath;
	}

	/**
	 * Set the Web UI path.
	 */

	public static void setWebUiPath(String webUiPath) {
		FileName.webUiPath = webUiPath;
	}

	/**
	 * Returns the path of the generated HTML file, within the program.
	 * 
	 * @return path of generated HTML file, within the program.
	 */

	public static String getFileNameCalendarViewProgram() {
		return getWebUiPath() + filenameCalendarUi;
	}

	/**
	 * Returns the path of the full calendar css, within the program.
	 * 
	 * @return path of full calendar css file, within the program.
	 */

	public static String getFileNameFullCalendarCssProgram() {
		return getWebUiPath() + fileNameFullCalendarCss;
	}

	/**
	 * Set the path of the full calendar css file, within the program.
	 * 
	 */

	public static void setFileNameFullCalendarCss(String fileNameFullCalendarCss) {
		FileName.fileNameFullCalendarCss = fileNameFullCalendarCss;
	}

	/**
	 * Returns the path of the full calendar js file, within the program.
	 * 
	 * @return path of full calendar js file, within the program.
	 */

	public static String getFileNameFullCalendarJsProgram() {
		return getWebUiPath() + fileNameFullCalendarJs;
	}

	/**
	 * Set the path of the full calendar js file, within the program.
	 * 
	 */

	public static void setFileNameFullCalendarJs(String fileNameFullCalendarJs) {
		FileName.fileNameFullCalendarJs = fileNameFullCalendarJs;
	}

	/**
	 * Returns the path of the jquery js file, within the program.
	 * 
	 * @return path of jquery js file, within the program.
	 */

	public static String getFileNameJqueryProgram() {
		return getWebUiPath() + fileNameJquery;
	}

	/**
	 * Set the path of the jquery js file, within the program.
	 * 
	 */

	public static void setFileNameJquery(String fileNameJquery) {
		FileName.fileNameJquery = fileNameJquery;
	}

	/**
	 * Returns the path of the moment js file, within the program.
	 * 
	 * @return path of moment js file, within the program.
	 */

	public static String getFileNameMomentProgram() {
		return getWebUiPath() + fileNameMoment;
	}

	/**
	 * Set the path of the moment js file, within the program.
	 * 
	 */

	public static void setFileNameMoment(String fileNameMoment) {
		FileName.fileNameMoment = fileNameMoment;
	}

	/**
	 * Set the filename of the generated HTML file, within the program.
	 *
	 */

	public static void setFilenameCalendarUi(String filenameCalendarUi) {
		FileName.filenameCalendarUi = filenameCalendarUi;
	}

	/**
	 * Returns the path of the css folder, according to user saved path.
	 * 
	 * @return path of the css folder, according to user saved path.
	 */

	public static String getFolderCss() {
		return getCanonicalPath() + File.separator + getWebUiPath()
				+ File.separator + folderCss;
	}

	/**
	 * Set the path of the css folder, according to user saved path.
	 * 
	 */

	public static void setFolderCss(String folderCss) {
		FileName.folderCss = folderCss;
	}

	/**
	 * Returns the path of the js folder, according to user saved path.
	 * 
	 * @return path of the js folder, according to user saved path.
	 */

	public static String getFolderJs() {
		return getCanonicalPath() + File.separator + getWebUiPath()
				+ File.separator + folderJs;
	}

	/**
	 * Set the path of the js folder, according to user saved path.
	 * 
	 */

	public static void setFolderJs(String folderJs) {
		FileName.folderJs = folderJs;
	}

	/**
	 * Returns the path of the moment js file, according to user saved path.
	 * 
	 * @return path of the moment js file, according to user saved path.
	 */

	public static String getFileNameMomentLocal() {
		return getCanonicalPath() + File.separator + getRelativeWebUiPath()
				+ File.separator + folderJs + File.separator + fileNameMoment;
	}

	/**
	 * Returns the path of the jquery js file, according to user saved path.
	 * 
	 * @return path of the jquery js file, according to user saved path.
	 */

	public static String getFileNameJqueryLocal() {
		return getCanonicalPath() + File.separator + getRelativeWebUiPath()
				+ File.separator + folderJs + File.separator + fileNameJquery;
	}

	/**
	 * Returns the path of the full calendar js file, according to user saved
	 * path.
	 * 
	 * @return path of the full calendar js file, according to user saved path.
	 */

	public static String getFileNameFullCalendarJsLocal() {
		return getCanonicalPath() + File.separator + getRelativeWebUiPath()
				+ File.separator + folderJs + File.separator
				+ fileNameFullCalendarJs;
	}

	/**
	 * Returns the path of the generated HTML file, according to user saved
	 * path.
	 * 
	 * @return path of generated HTML file, according to user saved path.
	 */

	public static String getFileNameCalendarViewLocal() {
		return getCanonicalPath() + File.separator + getRelativeWebUiPath()
				+ File.separator + filenameCalendarUi;
	}

	/**
	 * Returns the path of the full calendar css file, according to user saved
	 * path.
	 * 
	 * @return path of the full calendar css file, according to user saved path.
	 */

	public static String getFileNameFullCalendarCssLocal() {
		return getCanonicalPath() + File.separator + getRelativeWebUiPath()
				+ File.separator + folderCss + File.separator
				+ fileNameFullCalendarCss;
	}

	/**
	 * This method will create the required physical file(s), if they did not
	 * exist in the defined user saved path.
	 */

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

	/**
	 * This method will create the file, according to the full path and file
	 * name in the input parameter.
	 * 
	 * @param canonicalFileName
	 *            The full path of the file, including the file name
	 * @throws IOException
	 *             Will throw this exception if unable to read or write on the
	 *             file
	 */

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

	/**
	 * This method will serialize any given Object into its JSON form, in String
	 * format.
	 * 
	 * @param object
	 *            Object to be serialized to JSON
	 * @return the object that is serialized to JSON, in String format
	 */

	private static String serializeToJson(Object object) {

		String json;

		Gson gson = new Gson();
		json = gson.toJson(object);

		return json;
	}
}
