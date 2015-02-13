package entity;

import java.util.Date;

public class NormalTask extends Task {

	private Date startDateTime, endDateTime;
	
	public NormalTask(String taskName, int priority, Date startDateTime, Date endDateTime) {
		
		super(taskName, priority);
		this.setStartDateTime(startDateTime);
		this.setEndDateTime(endDateTime);
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	@Override
	public String toString() {
		return "NormalTask [startDateTime=" + startDateTime + ", endDateTime="
				+ endDateTime + ", getTaskId()=" + getTaskId()
				+ ", getTaskName()=" + getTaskName() + ", getPriority()="
				+ getPriority() + "]";
	}
}
