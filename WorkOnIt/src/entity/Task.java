package entity;

import java.util.Date;

public abstract class Task {

	private String taskName;
	private long taskId;
	private int priority;
	
	public Task(String taskName, int priority) {
		
		long generatedTaskId = System.currentTimeMillis();
		
		this.setTaskId(generatedTaskId);
		this.setTaskName(taskName);
		this.setPriority(priority);
	}

	public long getTaskId() {
		return taskId;
	}

	private void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public Date getDateCreated() {
		
		Date dateCreated = null;
		
		dateCreated = new Date(this.taskId);
		
		return dateCreated;
	}
	
	public abstract String toString();
}
