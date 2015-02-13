package entity;

import java.util.Date;

public class DeadlineTask extends Task {
	
	private Date deadline;

	public DeadlineTask(String taskName, int priority, Date deadline) {
		
		super(taskName, priority);
		this.setDeadline(deadline);
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	@Override
	public String toString() {
		return "DeadlineTask [deadline=" + deadline + ", getTaskId()="
				+ getTaskId() + ", getTaskName()=" + getTaskName()
				+ ", getPriority()=" + getPriority() + "]";
	}
}
