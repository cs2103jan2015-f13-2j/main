package resource;

public class Message {

	public static final String UI_INPUT_HERE = "input here";

	public static final String SUCCESS_COMMAND = "Command executed successfully";
	public static final String SUCCESS_RETRIEVE_LIST = "List successfully retrieved.";
	public static final String SUCCESS_DELETE = "Successfully deleted.";
	public static final String SUCCESS_UPDATE = "Successfully updated.";

	public static final String FAIL_RETRIEVE_LIST = "List fail to retrieved.";
	public static final String FAIL_UNDO = "Unable to perform Undo operation.";
	public static final String FAIL_PARSE_COMMAND = "Unrecognized command.";
	public static final String FAIL_PARSE_PRIORITY = "Cannot parse priority.";

	public static final String FILE_CREATED = "New file(s) created.";
	public static final String FILE_NOT_FOUND = "File(s) not found.";

	public static final String ERROR_SAVE_INTO_FILE = "saveIntoFile: IO error. Please check R/W/X access.";
	public static final String ERROR_NO_PRIORITY_FOUND = "No priority found.";
	public static final String ERROR_RETRIEVE = "Retrieval error.";
	public static final String ERROR_UPDATE_INVALID_INDEX = "Invalid index to update. Please enter a valid range.";
	public static final String ERROR_UPDATE_NO_TASK_LIST = "No task to update. Please retrieve task first.";
	public static final String ERROR_UPDATE = "Unknown error occured while parsing update command.";
	public static final String ERROR_DELETE_IS_NAN = "Index is not a number. Please enter a numerical value.";
	public static final String ERROR_DELETE_INVALID_INDEX = "Invalid index to delete. Please enter a valid range.";
	public static final String ERROR_DELETE_NO_TASK_LIST = "No task to delete. Please retrieve task first.";
	public static final String ERROR_DELETE = "Unknown error occured while parsing delete command.";
}
