package entity;

import java.util.List;

public class TaskHistory {

	private Task task, auxTask;
	private List<Task> taskList;
	private String operation;
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public TaskHistory(String operation, Task task) {
		this(operation, task, null);
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public TaskHistory(String operation, Task task, Task auxTask) {
		this.setOperation(operation);
		this.setTask(task);
		this.setAuxTask(auxTask);
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public TaskHistory(String operation, List<Task> taskList) {
		this.setOperation(operation);
		this.setTaskList(taskList);
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Task getTask() {
		return task;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	private void setTask(Task task) {
		this.task = task;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Task getAuxTask() {
		return auxTask;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	private void setAuxTask(Task auxTask) {
		this.auxTask = auxTask;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public String getOperation() {
		return operation;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	private void setOperation(String operation) {
		this.operation = operation;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public List<Task> getTaskList() {
		return taskList;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	private void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
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
	 * @param  	
	 * @return      
	 */
	//@author 
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
