package resource;

public class FileName {

	private static String filenameDeadline = "datafile_deadLine.txt";
	private static String filenameFloating = "datafile_floating.txt";
	private static String filenameNormal = "datafile_normal.txt";
	private static String filenameRecur = "datafile_recur.txt";
	private static String filenameCfg = "command.cfg";

	public static String getFilenameDeadline() {
		return filenameDeadline;
	}

	public static void setFilenameDeadline(String filenameDeadline) {
		FileName.filenameDeadline = filenameDeadline;
	}

	public static String getFilenameFloating() {
		return filenameFloating;
	}

	public static void setFilenameFloating(String filenameFloating) {
		FileName.filenameFloating = filenameFloating;
	}

	public static String getFilenameNormal() {
		return filenameNormal;
	}

	public static void setFilenameNormal(String filenameNormal) {
		FileName.filenameNormal = filenameNormal;
	}

	public static String getFilenameRecur() {
		return filenameRecur;
	}

	public static void setFilenameRecur(String filenameRecur) {
		FileName.filenameRecur = filenameRecur;
	}

	public static String getFilenameCfg() {
		return filenameCfg;
	}

	public static void setFilenameCfg(String filenameCfg) {
		FileName.filenameCfg = filenameCfg;
	}

}
