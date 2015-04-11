package entity;

import java.util.List;

public class TaskHistory {

	private Task task, auxTask;
	private List<Task> taskList;
	private String operation;

	/**
	 * This is constructor for Task History .
	 *
	 * @param String
	 *            The last operation for the task
	 * @param task
	 *            The task need to be insert to task history
	 * @return
	 */
	// @author A0111916M
	public TaskHistory(String operation, Task task) {
		this(operation, task, null);
	}

	/**
	 * This is constructor for Task History .
	 *
	 * @param String
	 *            The last operation for the task
	 * @param task
	 *            The task need to be insert to task history
	 * @param task
	 *            The task need to be over ride in task history
	 */
	// @author A0111916M
	public TaskHistory(String operation, Task task, Task auxTask) {
		this.setOperation(operation);
		this.setTask(task);
		this.setAuxTask(auxTask);
	}

	/**
	 * This is constructor for Task History .
	 *
	 * @param String
	 *            The last operation for the task
	 * @param List
	 *            The task list that need to set in the task history
	 */
	// @author A0111916M
	public TaskHistory(String operation, List<Task> taskList) {
		this.setOperation(operation);
		this.setTaskList(taskList);
	}

	/**
	 * This is the method to get Task from the TaskHistory.
	 *
	 * @return Task return the Task from the TaskHistory.
	 */
	// @author A0111916M
	public Task getTask() {
		return task;
	}

	/**
	 * This is the method to set Task for the TaskHistory.
	 *
	 * @return Task set the Task into the TaskHistory.
	 */
	// @author A0111916M
	private void setTask(Task task) {
		this.task = task;
	}

	/**
	 * This is the method to get override Task from the TaskHistory.
	 *
	 * @return Task return the override Task from the TaskHistory.
	 */
	// @author A0111916M
	public Task getAuxTask() {
		return auxTask;
	}

	/**
	 * This is the method to set override Task for the TaskHistory.
	 *
	 * @return Task set the override Task into the TaskHistory.
	 */
	// @author A0111916M
	private void setAuxTask(Task auxTask) {
		this.auxTask = auxTask;
	}

	/**
	 * This is the method to get operation from the TaskHistory.
	 *
	 * @return Task return the operation Task from the TaskHistory.
	 */
	// @author A0111916M
	public String getOperation() {
		return operation;
	}

	/**
	 * This is the method to set operation for the TaskHistory.
	 *
	 * @return Task set the operation Task into the TaskHistory.
	 */
	// @author A0111916M
	private void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * This is the method to get task list from the TaskHistory.
	 *
	 * @return Task return the task list from the TaskHistory.
	 */
	// @author A0111916M
	public List<Task> getTaskList() {
		return taskList;
	}

	/**
	 * This is the method to set task list for the TaskHistory.
	 *
	 * @return Task set the task list Task into the TaskHistory.
	 */
	// @author A0111916M
	private void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	/**
	 *
	 * This is to generate the hash code from the TaskHistory
	 *
	 * @return int The hash code generated.
	 */
	// @author A0111916M
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auxTask == null) ? 0 : auxTask.hashCode());
		result = prime * result
				+ ((operation == null) ? 0 : operation.hashCode());
		result = prime * result + ((task == null) ? 0 : task.hashCode());
		result = prime * result
				+ ((taskList == null) ? 0 : taskList.hashCode());
		return result;
	}

	/**
	 *
	 * Compare between 2 TaskHistory whether they are the same or not.
	 *
	 * @param Object
	 *            The parsed in object that need to be compared
	 * @return boolean return true if both task are the same, else false.
	 */
	// @author A0111916M
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskHistory other = (TaskHistory) obj;
		if (auxTask == null) {
			if (other.auxTask != null)
				return false;
		} else if (!auxTask.equals(other.auxTask))
			return false;
		if (operation == null) {
			if (other.operation != null)
				return false;
		} else if (!operation.equals(other.operation))
			return false;
		if (task == null) {
			if (other.task != null)
				return false;
		} else if (!task.equals(other.task))
			return false;
		if (taskList == null) {
			if (other.taskList != null)
				return false;
		} else if (!taskList.equals(other.taskList))
			return false;
		return true;
	}
}
