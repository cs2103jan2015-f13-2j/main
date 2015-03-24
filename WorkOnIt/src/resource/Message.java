package resource;

public class Message {

	public static final String UI_INPUT_HERE = "input here";
	public static final String UI_WELCOME_TITLE = "Welcome";
	public static final String UI_WELCOME_INFO = "Welcome to Work On It.";
	public static final String UI_WELCOME_INFO_NO_FILE_FOUND = UI_WELCOME_INFO
			+ " We need to create required file(s). Please choose your save directory. Continue with it?";
	public static final String UI_OF_THE_MONTH = "of the month";
	public static final String UI_DUE_BY = "due by";
	
	public static final String BUTTON_YES = "Yes";
	public static final String BUTTON_CANCEL = "Cancel";

	public static final String SUCCESS_GENERAL = "Operation successful.";
	public static final String SUCCESS_COMMAND = "Command executed successfully.";
	public static final String SUCCESS_ADDED = "Task added successfully.";
	public static final String SUCCESS_RETRIEVE_LIST = "List successfully retrieved.";
	public static final String SUCCESS_DELETE = "Successfully deleted.";
	public static final String SUCCESS_UPDATE = "Successfully updated.";
	public static final String SUCCESS_FILES_EXIST = "All required files exist.";
	public static final String SUCCESS_MARK_DONE = "Selected task(s) marked as done.";
	public static final String SUCCESS_MARK_UNDONE = "Selected task(s) marked as un-done.";

	public static final String FAIL_GENERAL = "Operation failed.";
	public static final String FAIL_RETRIEVE_LIST = "List fail to retrieved.";
	public static final String FAIL_UNDO = "Unable to perform Undo operation.";
	public static final String FAIL_REDO = "Unable to perform Redo operation.";
	public static final String FAIL_PARSE_COMMAND = "Unrecognized command.";
	public static final String FAIL_PARSE_PRIORITY = "Cannot parse priority.";
	public static final String FAIL_FILES_EXIST = "One or more file(s) does not exist.";
	public static final String FAIL_MARK_DONE = "Selected task(s) fail to mark as done.";
	public static final String FAIL_MARK_UNDONE = "Selected task(s) fail to mark as un-done.";

	public static final String FILE_CREATED = "New file(s) created.";
	public static final String FILE_NOT_FOUND = "File(s) not found.";

	public static final String ERROR_GENERAL = "Error occured.";
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
	public static final String ERROR_DONE_IS_NAN = "Index is not a number. Please enter a numerical value.";
	public static final String ERROR_DONE_INVALID_INDEX = "Invalid index(es) to mark as done. Please enter a valid range.";
	public static final String ERROR_DONE_NO_TASK_LIST = "No task to mark as done. Please retrieve task first.";
	public static final String ERROR_DONE = "Unknown error occured while parsing done command.";
	public static final String ERROR_UNDONE_IS_NAN = "Index is not a number. Please enter a numerical value.";
	public static final String ERROR_UNDONE_INVALID_INDEX = "Invalid index(es) to mark as undone. Please enter a valid range.";
	public static final String ERROR_UNDONE_NO_TASK_LIST = "No task to mark as undone. Please retrieve task first.";
	public static final String ERROR_UNDONE = "Unknown error occured while parsing undone command.";
}
