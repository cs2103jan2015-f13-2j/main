package parser;

import java.util.ArrayList;

import entity.Task;

public class DataParser {

	private ArrayList<Task> retrievedTaskList = null;
	private String lastRetrieve = null;
	private Task taskToRemove = null;

	public ArrayList<Task> getRetrievedTaskList() {
		return retrievedTaskList;
	}

	public void setRetrievedTaskList(ArrayList<Task> retrievedTaskList) {
		this.retrievedTaskList = retrievedTaskList;
	}

	public String getLastRetrieve() {
		return lastRetrieve;
	}

	public void setLastRetrieve(String lastRetrieve) {
		this.lastRetrieve = lastRetrieve;
	}
	
	public void appendLastRetrieve(String append) {
		this.lastRetrieve += " " + append;
	}

	public Task getTaskToRemove() {
		return taskToRemove;
	}

	public void setTaskToRemove(Task taskToRemove) {
		this.taskToRemove = taskToRemove;
	}
}
