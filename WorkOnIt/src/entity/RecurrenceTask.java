package entity;

import java.util.Date;

public class RecurrenceTask extends Task {

	private long tagId;
	private Date startRecurrenceDate, endRecurrenceDate;
	
	public RecurrenceTask(String taskName, int priority, Date startRecurrenceDate, Date endRecurrenceDate) {
		
		super(taskName, priority);
		
		long generatedTagId = System.currentTimeMillis();
		this.setTagId(generatedTagId);
		this.setStartRecurrenceDate(startRecurrenceDate);
		this.setEndRecurrenceDate(endRecurrenceDate);
		
	}

	public long getTagId() {
		return tagId;
	}

	private void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public Date getStartRecurrenceDate() {
		return startRecurrenceDate;
	}

	public void setStartRecurrenceDate(Date startRecurrenceDate) {
		this.startRecurrenceDate = startRecurrenceDate;
	}

	public Date getEndRecurrenceDate() {
		return endRecurrenceDate;
	}

	public void setEndRecurrenceDate(Date endRecurrenceDate) {
		this.endRecurrenceDate = endRecurrenceDate;
	}

	@Override
	public String toString() {
		return "RecurrenceTask [tagId=" + tagId + ", startRecurrenceDate="
				+ startRecurrenceDate + ", endRecurrenceDate="
				+ endRecurrenceDate + ", getTaskId()=" + getTaskId()
				+ ", getTaskName()=" + getTaskName() + ", getPriority()="
				+ getPriority() + ", getDateCreated()=" + getDateCreated()
				+ "]";
	}
}
