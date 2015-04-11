package logic;

import java.util.List;
import java.util.Stack;

import resource.KeywordConstant;
import resource.Message;
import data.FileIO;
import entity.Success;
import entity.Task;
import entity.TaskHistory;

public class Utility {

	public static Stack<TaskHistory> undoStack = null;
	public static Stack<TaskHistory> redoStack = null;
	private static Utility obj = null;

	/**
	 * instantiate both of the undo and redo stack.
	 *
	 * @return
	 */
	// @author A0112694E
	public Utility() {
		undoStack = new Stack<TaskHistory>();
		redoStack = new Stack<TaskHistory>();
	}

	/**
	 * instantiate the utility object.
	 *
	 * @return Utility the only Utility object which belong to the class
	 */
	// @author A0112694E
	public static Utility getInstance() {
		if (obj == null) {
			obj = new Utility();
			return obj;
		} else {
			return obj;
		}
	}

	/**
	 * re-instantiate both of the undo and redo stack.
	 *
	 * @return
	 */
	// @author A0112694E
	public static void reset() {
		undoStack = new Stack<TaskHistory>();
		redoStack = new Stack<TaskHistory>();
	}

	/**
	 * add Task into the undo stack
	 *
	 * @return
	 */
	// @author A0112694E
	public static void addUndoStack(TaskHistory task) {
		undoStack.push(task);

	}

	/**
	 * add Task into the redo stack
	 *
	 * @return
	 */
	// @author A0112694E
	public static void addRedoStack(TaskHistory task) {
		redoStack.push(task);
	}

	/**
	 * undo the last command from the undo stack
	 *
	 * @return Success Success object return by the fileIO contain the success
	 *         Message.
	 */
	// @author A0112694E
	public static Success undoTaskFunction() {

		Success status = null;
		FileIO dataStorage = new FileIO();

		if (undoStack.size() > 0) {
			TaskHistory undoTask = undoStack.pop();
			String undoOperation = undoTask.getOperation();

			// delete the task obj.
			if (undoOperation.equalsIgnoreCase(KeywordConstant.KEYWORD_ADD)) {
				Task taskObj = undoTask.getTask();
				status = dataStorage.deleteFromFile(taskObj);
			}
			// add the taskObj.
			else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DELETE)) {
				List<Task> taskListToRevert = undoTask.getTaskList();
				boolean isMarkAllDeleted = true;

				for (int i = 0; i < taskListToRevert.size(); i++) {

					Task currentTask = taskListToRevert.get(i);
					Success successObj = dataStorage.saveIntoFile(currentTask);

					if (successObj == null || !successObj.isSuccess()) {
						isMarkAllDeleted = false;
					}
				}

				if (isMarkAllDeleted) {
					status = new Success(true, Message.SUCCESS_DELETE);
				} else {
					status = new Success(false, Message.ERROR_DELETE);
				}
			}
			// revert the taskObj to previous version.
			else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UPDATE)) {
				Task taskObj = undoTask.getTask();
				Task taskToRevert = undoTask.getAuxTask();
				status = dataStorage.updateFromFile(taskToRevert, taskObj);

			} else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DONE)) {

				boolean isMarkAllUndone = true;
				List<Task> taskListToRevert = undoTask.getTaskList();

				for (int i = 0; i < taskListToRevert.size(); i++) {

					Task currentTask = taskListToRevert.get(i);

					Success successObj = dataStorage
							.deleteFromFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllUndone = false;
					}

					currentTask.setCompleted(false);
					successObj = dataStorage.saveIntoFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllUndone = false;
					}
				}

				if (isMarkAllUndone) {
					status = new Success(true, Message.SUCCESS_MARK_UNDONE);
				} else {
					status = new Success(false, Message.FAIL_MARK_UNDONE);
				}

			} else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UNDONE)) {

				boolean isMarkAllDone = true;
				List<Task> taskListToRevert = undoTask.getTaskList();

				for (int i = 0; i < taskListToRevert.size(); i++) {

					Task currentTask = taskListToRevert.get(i);

					Success successObj = dataStorage
							.deleteFromFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllDone = false;
					}

					currentTask.setCompleted(true);
					successObj = dataStorage.saveIntoFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllDone = false;
					}
				}

				if (isMarkAllDone) {
					status = new Success(true, Message.SUCCESS_MARK_DONE);
				} else {
					status = new Success(false, Message.FAIL_MARK_DONE);
				}
			}

			redoStack.push(undoTask);

		} else {
			status = new Success(false, Message.FAIL_UNDO);
		}
		return status;
	}

	/**
	 * redo the last command from the redo stack
	 *
	 * @return Success Success object return by the fileIO contain the success
	 *         Message.
	 */
	// @author A0112694E
	public static Success redoTaskFunction() {

		Success status = null;
		FileIO dataStorage = new FileIO();

		if (redoStack.size() > 0) {
			TaskHistory redoTask = redoStack.pop();
			String undoOperation = redoTask.getOperation();
			Task taskObj = redoTask.getTask();

			// add the task obj.
			if (undoOperation.equalsIgnoreCase(KeywordConstant.KEYWORD_ADD)) {
				status = dataStorage.saveIntoFile(taskObj);
			}
			// delete the taskObj.
			else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DELETE)) {
				List<Task> taskListToRevert = redoTask.getTaskList();
				boolean isMarkAllDeleted = true;

				for (int i = 0; i < taskListToRevert.size(); i++) {

					Task currentTask = taskListToRevert.get(i);
					Success successObj = status = dataStorage
							.deleteFromFile(currentTask);

					if (successObj == null || !successObj.isSuccess()) {
						isMarkAllDeleted = false;
					}
				}

				if (isMarkAllDeleted) {
					status = new Success(true, Message.SUCCESS_DELETE);
				} else {
					status = new Success(false, Message.ERROR_DELETE);
				}
			}
			// revert the taskObj to previous version.
			else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UPDATE)) {
				Task taskToRevert = redoTask.getAuxTask();
				status = dataStorage.updateFromFile(taskObj, taskToRevert);
			} else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UNDONE)) {

				boolean isMarkAllUndone = true;
				List<Task> taskListToRevert = redoTask.getTaskList();

				for (int i = 0; i < taskListToRevert.size(); i++) {

					Task currentTask = taskListToRevert.get(i);

					Success successObj = dataStorage
							.deleteFromFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllUndone = false;
					}

					currentTask.setCompleted(false);
					successObj = dataStorage.saveIntoFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllUndone = false;
					}
				}

				if (isMarkAllUndone) {
					status = new Success(true, Message.SUCCESS_MARK_UNDONE);
				} else {
					status = new Success(false, Message.FAIL_MARK_UNDONE);
				}

			} else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DONE)) {

				boolean isMarkAllDone = true;
				List<Task> taskListToRevert = redoTask.getTaskList();

				for (int i = 0; i < taskListToRevert.size(); i++) {

					Task currentTask = taskListToRevert.get(i);

					Success successObj = dataStorage
							.deleteFromFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllDone = false;
					}

					currentTask.setCompleted(true);
					successObj = dataStorage.saveIntoFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllDone = false;
					}
				}

				if (isMarkAllDone) {
					status = new Success(true, Message.SUCCESS_MARK_DONE);
				} else {
					status = new Success(false, Message.FAIL_MARK_DONE);
				}
			}

			undoStack.push(redoTask);

		} else {
			status = new Success(false, Message.FAIL_REDO);
		}

		return status;
	}

}
