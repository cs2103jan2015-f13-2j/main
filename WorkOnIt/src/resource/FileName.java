package resource;

public class FileName {

	private static String filename_deadline = "datafile_deadLine.txt";
	private static String filename_floating = "datafile_floating.txt";
	private static String filename_normal = "datafile_normal.txt";
	private static String filename_recur = "datafile_recur.txt";

	public static String getFilename_deadline() {
		return filename_deadline;
	}

	public static void setFilename_deadline(String filename_deadline) {
		FileName.filename_deadline = filename_deadline;
	}

	public static String getFilename_floating() {
		return filename_floating;
	}

	public static void setFilename_floating(String filename_floating) {
		FileName.filename_floating = filename_floating;
	}

	public static String getFilename_normal() {
		return filename_normal;
	}

	public static void setFilename_normal(String filename_normal) {
		FileName.filename_normal = filename_normal;
	}

	public static String getFilename_recur() {
		return filename_recur;
	}

	public static void setFilename_recur(String filename_recur) {
		FileName.filename_recur = filename_recur;
	}

}
