package entity;

import java.util.Date;

public class RecurrenceTask extends Task {

	private long tagId;
	private Date recurrenceDate;
	
	public RecurrenceTask(String taskName, int priority, Date recurrenceDate) {
		
		super(taskName, priority);
		
		long generatedTagId = System.currentTimeMillis();
		this.setTagId(generatedTagId);
		this.setRecurrenceDate(recurrenceDate);
	}

	public long getTagId() {
		return tagId;
	}

	private void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public Date getRecurrenceDate() {
		return recurrenceDate;
	}

	public void setRecurrenceDate(Date recurrenceDate) {
		this.recurrenceDate = recurrenceDate;
	}

	@Override
	public String toString() {
		return "RecurrenceTask [tagId=" + tagId + ", recurrenceDate="
				+ recurrenceDate + ", getTaskId()=" + getTaskId()
				+ ", getTaskName()=" + getTaskName() + ", getPriority()="
				+ getPriority() + ", getDateCreated()=" + getDateCreated()
				+ "]";
	}
}
