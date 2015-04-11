package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import resource.KeywordConstant;
import resource.Message;
import data.FileIO;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
import entity.SuccessDisplay;
import entity.Task;
import entity.Success;
import entity.TaskHistory;

public class Engine {

	
	/**
	 * This method will call the saveIntoFile method in 
	 * fileIO to save the Task 
	 *
	 * @param	Task
	 * 				Task which need to be save into the data file
	 * @return	Success      
	 * 				Success object return by the fileIO contain the success Message.
	 */
	//@author 	A0112694E
	public Success addTask(Task task) {

		Success status = null;

		FileIO dataStorage = new FileIO();
		status = dataStorage.saveIntoFile(task);

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_ADD, task);
		
		Utility.getInstance();
		Utility.addUndoStack(taskHistoryObj);

		return status;
	}

	/**
	 * This method will call the loadFromFileTask method in 
	 * fileIO to load the task based on the keyword entered
	 *
	 * @param	String
	 * 				Keyword which specific the task type
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A0112694E
	@SuppressWarnings("unchecked")
	public Success retrieveTask(String keyword) {

		Success status;

		try {
			List<Task> taskList = new ArrayList<Task>();

			FileIO dataStorage = new FileIO();

			taskList.addAll((ArrayList<Task>) dataStorage.loadFromFileTask(
					keyword).getObj());

			status = new Success(taskList, true, Message.SUCCESS_RETRIEVE_LIST);

		} catch (Exception e) {
			status = new Success(false, e.getMessage());
		}

		return status;

	}
	
	/**
	 * This method will call the loadFromFileTask method in
	 *  fileIO to load all Task.
	 *
	 * @param	
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A0112694E
	@SuppressWarnings("unchecked")
	public Success retrieveTask() {

		Success status;

		try {
			List<Task> taskList = new ArrayList<Task>();

			FileIO dataStorage = new FileIO();

			taskList.addAll((ArrayList<Task>) dataStorage.loadFromFileTask(
					KeywordConstant.KEYWORD_FLOATING_TASK).getObj());
			taskList.addAll((ArrayList<Task>) dataStorage.loadFromFileTask(
					KeywordConstant.KEYWORD_NORMAL_TASK).getObj());
			taskList.addAll((ArrayList<Task>) dataStorage.loadFromFileTask(
					KeywordConstant.KEYWORD_DEADLINE_TASK).getObj());
			taskList.addAll((ArrayList<Task>) dataStorage.loadFromFileTask(
					KeywordConstant.KEYWORD_RECUR_TASK).getObj());

			status = new Success(taskList, true, Message.SUCCESS_RETRIEVE_LIST);

		} catch (Exception e) {
			status = new Success(false, e.getMessage());
		}

		return status;

	}
	
	
	/**
	 * This method will call the loadFromStartDate method in 
	 * fileIO to load the task based on the date entered
	 *
	 * @param	Date
	 * 				date which specific the task date to be retrieved
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A0112694E
	public Success retrieveTask(Date date) throws IOException {

		Success status = null;
		FileIO dataStorage = new FileIO();
		status = dataStorage.loadFromStartDate(date);

		return status;
	}
	
	/**
	 * This method will call the loadFromBetweenDate method in 
	 * fileIO to load the task between start date and end date entered
	 *
	 * @param	Date
	 * 				start date which specific the task start date.
	 * @param	Date
	 * 				end date which specific the task end date.
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A0112694E
	public Success retrieveTask(Date startDate, Date endDate)
			throws IOException {

		Success status = null;
		FileIO dataStorage = new FileIO();
		status = dataStorage.loadFromBetweenDate(startDate, endDate);

		return status;
	}
	
	/**
	 * This method will call the loadFromPriority method in 
	 * fileIO to load the task base on the priority
	 *
	 * @param	int
	 * 				priority which specific the task priority to be retrieved
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A0112694E
	public Success retrieveTask(int priority) throws IOException {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.loadFromPriority(priority);

		return status;
	}
	/**
	 * This method will call the loadFromPriorityAndDate method in 
	 * fileIO to load the task based on the date and priority entered
	 *
	 * @param	Date
	 * 				date which specific the task date to be retrieved
	 * @param	int
	 * 				priority which specific the task priority to be retrieved
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A0112694E
	public Success retrieveTask(int priority, Date date) throws IOException {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.loadFromPriorityAndDate(priority, date);

		return status;
	}
	
	/**
	 * This method will call the loadFromPriorityBetweenDate method in 
	 * fileIO to load the task between start date and end date and priority entered
	 *
	 * @param	Date
	 * 				start date which specific the task start date.
	 * @param	Date
	 * 				end date which specific the task end date.
	 * @param	int
	 * 				priority which specific the task priority to be retrieved
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A0112694E
	public Success retrieveTask(int priority, Date startDate, Date endDate)
			throws IOException {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.loadFromPriorityBetweenDate(priority, startDate,
				endDate);

		return status;
	}
	
	/**
	 * This method will call the loadCompletedTaskWithDate method in 
	 * fileIO to load the task which has mark as completed with date
	 *
	 * @param	boolean
	 * 				boolean which specific the wether the task has been completed or not
	 * @param	Date
	 * 				date which specific the task date.
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A0112694E
	public Success getCompleteTaskWithDate(boolean isComplete ,Date date) {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.loadCompletedTaskWithDate(isComplete, date);

		return status;
	}
	
	/**
	 * This method will call the loadCompletedTaskBetweenDate method in 
	 * fileIO to load the task between start date and end date entered and which mark as completed
	 *
	 * @param	boolean
	 * 				boolean which specific the wether the task has been completed or not
	 * @param	Date
	 * 				start date which specific the task start date.
	 * @param	Date
	 * 				end date which specific the task end date.
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A0112694E
	public Success getCompleteTaskBetweenDate(boolean isComplete, Date startDate, Date endDate) {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.loadCompletedTaskBetweenDate(isComplete, startDate, endDate);

		return status;
	}

	
	
	@SuppressWarnings("unchecked")
	public SuccessDisplay retrieveDisplay(Date startDate, Date endDate,
			String displayType) throws IOException {

		SuccessDisplay statusDisp = null;

		Success status = retrieveTask(startDate, endDate);

		if (status.isSuccess()) {

			List<Task> taskList = (ArrayList<Task>) status.getObj();

			for (int i = 0; i < taskList.size(); i++) {
				String name = taskList.get(i).getTaskName();
				System.out.println(name);
			}

			status = retrieveTask(KeywordConstant.KEYWORD_FLOATING_TASK);

			if (status.isSuccess()) {
				List<Task> floatingTaskList = (ArrayList<Task>) status.getObj();
				taskList.addAll(floatingTaskList);

				statusDisp = new SuccessDisplay(displayType, taskList, true,
						Message.SUCCESS_RETRIEVE_LIST);
			} else {
				statusDisp = new SuccessDisplay(false, Message.ERROR_RETRIEVE);
			}

		} else {
			statusDisp = new SuccessDisplay(false, Message.ERROR_RETRIEVE);
		}

		return statusDisp;
	}
	
	
	/**
	 * This method will call the searchFromFile method in 
	 * fileIO to load the task base on the keyword entered.
	 *
	 * @param	String
	 * 				keyowrd which specific inside the task description  
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A0112694E
	public Success searchTask(String keyword) {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.searchFromFile(keyword);

		return status;
	}
	/**
	 * This method will call the searchFromFileWithDate method in 
	 * fileIO to load the task base on the keyword and date entered.
	 *
	 * @param	String
	 * 				keyowrd which specific inside the task description  
	 * @param	Date
	 * 				date which specific the task date.
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A0112694E
	public Success searchTask(String keyword, Date date) {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.searchFromFileWithDate(keyword, date);

		return status;
	}
	
	/**
	 * This method will call the searchFromFileBetweenDate method in 
	 * fileIO to load the task base on the keyword and between the start date and end date entered.
	 *
	 * @param	String
	 * 				keyowrd which specific inside the task description  
	 * @param	Date
	 * 				start date which specific the task start date.
	 * @param	Date
	 * 				end date which specific the task end date.
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 
	public Success searchTask(String keyword, Date startDate, Date endDate) {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.searchFromFileBetweenDate(keyword, startDate,
				endDate);

		return status;
	}
	
	/**
	 * This method will call the deleteFromFile method in 
	 * fileIO to delete the specific task parse in.
	 *
	 * @param	List
	 * 				List of task that need to be deleted from the data file  
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author A0112694E
	public Success deleteTask(List<Task> deleteList) {

		Success status = null;
		FileIO dataStorage = new FileIO();
		boolean isMarkAllDeleted = true;

		for (int i = 0; i < deleteList.size(); i++) {

			Task currentTask = deleteList.get(i);
			Success successObj = dataStorage.deleteFromFile(currentTask);

			if (!successObj.isSuccess()) {
				isMarkAllDeleted = false;
			}
		}

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_DELETE, deleteList);
		
		Utility.getInstance();
		Utility.addUndoStack(taskHistoryObj);

		if (isMarkAllDeleted) {
			status = new Success(true, Message.SUCCESS_DELETE);
		} else {
			status = new Success(false, Message.ERROR_DELETE);
		}

		return status;
	}
	
	/**
	 * This method will call the updateFromFile method in 
	 * fileIO to update the specific task parse in
	 *
	 * @param	Task
	 * 				the new task that need to be updated
	 * @param	Task
	 * 				the old task that need to be overwrite.
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author A0112694E
	public Success updateTask(Task taskUpdate, Task taskOld) {

		Success status = null;
		FileIO dataStorage = new FileIO();

		if (taskOld.isCompleted()) {
			taskUpdate.setCompleted(true);
		}

		status = dataStorage.updateFromFile(taskUpdate, taskOld);

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_UPDATE, taskUpdate, taskOld);
		Utility.getInstance();
		Utility.addUndoStack(taskHistoryObj);

		return status;
	}
	
	/**
	 * This method will call the getCompletedTask method in 
	 * fileIO to load the task which mark as completed
	 *
	 * @param	boolean
	 * 				boolean which specific the wether the task has been completed or not
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A0112694E
	public Success getCompleteTask(boolean isComplete) {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.getCompletedTask(isComplete);

		return status;
	}
	
	/**
	 * This method will mark the list of task as done
	 * 
	 * @param	List
	 * 				the list of task that need to be mark as done
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A01111916M
	public Success markAsDone(List<Task> doneList) {

		Success status = null;
		FileIO dataStorage = new FileIO();
		boolean isMarkAllDone = true;

		for (int i = 0; i < doneList.size(); i++) {

			Task currentTask = doneList.get(i);

			Success successObj = dataStorage.deleteFromFile(currentTask);

			if (!successObj.isSuccess()) {
				isMarkAllDone = false;
			}

			currentTask.setCompleted(true);
			successObj = dataStorage.saveIntoFile(currentTask);

			if (!successObj.isSuccess()) {
				isMarkAllDone = false;
			}
		}

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_DONE, doneList);
		Utility.getInstance();
		Utility.addUndoStack(taskHistoryObj);

		if (isMarkAllDone) {
			status = new Success(true, Message.SUCCESS_MARK_DONE);
		} else {
			status = new Success(false, Message.FAIL_MARK_DONE);
		}

		return status;
	}
	
	/**
	 * This method will mark the list of task as undone
	 * 
	 * @param	List
	 * 				the list of task that need to be mark as undone
	 * @return	Success      
	 * 				Success object return by the fileIO contain 
	 * 				the success Message and Array List of task retrieve from data file.
	 */
	//@author 	A01111916M
	public Success markAsUndone(List<Task> undoneList) {

		Success status = null;
		FileIO dataStorage = new FileIO();
		boolean isMarkAllUndone = true;

		for (int i = 0; i < undoneList.size(); i++) {

			Task currentTask = undoneList.get(i);

			Success successObj = dataStorage.deleteFromFile(currentTask);

			if (!successObj.isSuccess()) {
				isMarkAllUndone = false;
			}

			currentTask.setCompleted(false);
			successObj = dataStorage.saveIntoFile(currentTask);

			if (!successObj.isSuccess()) {
				isMarkAllUndone = false;
			}
		}

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_UNDONE, undoneList);
		Utility.getInstance();
		Utility.addUndoStack(taskHistoryObj);

		if (isMarkAllUndone) {
			status = new Success(true, Message.SUCCESS_MARK_UNDONE);
		} else {
			status = new Success(false, Message.FAIL_MARK_UNDONE);
		}

		return status;
	}
	/**
	 * call the undoTaskFunction method in Utility class to undo the last task
	 *
	 * @param  	
	 * @return  Success
	 * 				Success object which contain the message from utility  
	 */
	//@author A0112694E
	public Success undoTask() {
		
		Utility.getInstance();
		Success successObj = Utility.undoTaskFunction();
		
		return successObj;
		
	}
	/**
	 * call the redoTaskFunction method in Utility class to redo the last task
	 *
	 * @param  	
	 * @return  Success
	 * 				Success object which contain the message from utility  
	 */
	//@author A0112694E
	public Success redoTask() {
		
		Utility.getInstance();
		Success successObj = Utility.redoTaskFunction();
		
		return successObj;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Success getHistory() {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.getHistory();

		return status;
	}

}