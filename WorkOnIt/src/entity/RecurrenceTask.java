package entity;

import java.util.Date;

public class RecurrenceTask extends Task {

	private long tagId;
	private Date startRecurrenceDate, endRecurrenceDate;

	public RecurrenceTask(String taskName, int priority,
			Date startRecurrenceDate, Date endRecurrenceDate) {

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
		super.setSortDate(startRecurrenceDate);
	}

	public Date getEndRecurrenceDate() {
		return endRecurrenceDate;
	}

	public void setEndRecurrenceDate(Date endRecurrenceDate) {
		this.endRecurrenceDate = endRecurrenceDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((endRecurrenceDate == null) ? 0 : endRecurrenceDate
						.hashCode());
		result = prime
				* result
				+ ((startRecurrenceDate == null) ? 0 : startRecurrenceDate
						.hashCode());
		result = prime * result + (int) (tagId ^ (tagId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecurrenceTask other = (RecurrenceTask) obj;
		if (endRecurrenceDate == null) {
			if (other.endRecurrenceDate != null)
				return false;
		} else if (!endRecurrenceDate.equals(other.endRecurrenceDate))
			return false;
		if (startRecurrenceDate == null) {
			if (other.startRecurrenceDate != null)
				return false;
		} else if (!startRecurrenceDate.equals(other.startRecurrenceDate))
			return false;
		if (tagId != other.tagId)
			return false;
		return true;
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
