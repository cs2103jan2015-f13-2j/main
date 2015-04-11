package parser;

import java.util.ArrayList;

import entity.Task;

public class DataParser {

	private ArrayList<Task> retrievedTaskList = null;
	private String lastRetrieve = null;
	private Task taskToRemove = null;

	/**
	 * Returns the retrieved task list, based on previous retrieve or display
	 * command, if any.
	 * 
	 * @return retrievedTaskList previous Task(s) that were retrieved or
	 *         displayed, if any
	 */
	// @author A0111837J
	public ArrayList<Task> getRetrievedTaskList() {
		return retrievedTaskList;
	}

	/**
	 * Set the retrieved list that is filled with Task(s), if any.
	 * 
	 * @param retrievedTaskList
	 *            set the list of Task(s)
	 */
	// @author A0111837J
	public void setRetrievedTaskList(ArrayList<Task> retrievedTaskList) {
		this.retrievedTaskList = retrievedTaskList;
	}

	/**
	 * Returns the last retrieved or displayed command, if any retrieve or
	 * display is performed.
	 * 
	 * @return lastRetrieve The command that were used previously
	 */
	// @author A0111837J
	public String getLastRetrieve() {
		return lastRetrieve;
	}

	/**
	 * Set the last retrieved or displayed command, if any retrieve or display
	 * is performed.
	 * 
	 * @param lastRetrieve
	 *            Set the command that were used previously
	 */
	// @author A0111837J
	public void setLastRetrieve(String lastRetrieve) {
		this.lastRetrieve = lastRetrieve;
	}

	/**
	 * Append more commands to previous retrieve or display command.
	 * 
	 * @param append
	 *            Append more command to existing last retrieve command
	 */
	// @author A0111837J
	public void appendLastRetrieve(String append) {
		this.lastRetrieve += " " + append;
	}

	/**
	 * Returns the Task object that is pending for update, if any update command
	 * is executed.
	 * 
	 * @return taskToRemove The Task object that will be used for update, if any
	 *         update command is executed.
	 */
	// @author A0111837J
	public Task getTaskToRemove() {
		return taskToRemove;
	}

	/**
	 * Set the Task object that is pending for update, if any update command is
	 * performed.
	 * 
	 * @param taskToRemove
	 *            Set the Task object to be updated, if any update command is
	 *            performed.
	 */
	// @author A0111837J
	public void setTaskToRemove(Task taskToRemove) {
		this.taskToRemove = taskToRemove;
	}
}
