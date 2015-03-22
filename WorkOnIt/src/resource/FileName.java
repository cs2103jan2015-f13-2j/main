package resource;

import java.io.File;
import java.io.IOException;

public class FileName {

	private static String canonicalPath = "";
	private static String filenameDeadline = "datafile_deadLine.txt";
	private static String filenameFloating = "datafile_floating.txt";
	private static String filenameNormal = "datafile_normal.txt";
	private static String filenameRecur = "datafile_recur.txt";
	private static String filenameCfg = "command.cfg";
	
	public static String getFilenameDeadline() {
		return getCanonicalPath() + "\\" + filenameDeadline;
	}

	public static void setFilenameDeadline(String filenameDeadline) {
		FileName.filenameDeadline = filenameDeadline;
	}

	public static String getFilenameFloating() {
		return getCanonicalPath() + "\\" + filenameFloating;
	}

	public static void setFilenameFloating(String filenameFloating) {
		FileName.filenameFloating = filenameFloating;
	}

	public static String getFilenameNormal() {
		return getCanonicalPath() + "\\" + filenameNormal;
	}

	public static void setFilenameNormal(String filenameNormal) {
		FileName.filenameNormal = filenameNormal;
	}

	public static String getFilenameRecur() {
		return getCanonicalPath() + "\\" + filenameRecur;
	}

	public static void setFilenameRecur(String filenameRecur) {
		FileName.filenameRecur = filenameRecur;
	}

	public static String getFilenameCfg() {
		return getCanonicalPath() + "\\" + filenameCfg;
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
			canonicalPath = new File( "." ).getCanonicalPath();
		} catch(IOException e) {
			System.err.println(Message.ERROR_GENERAL);
		}
	}

}
