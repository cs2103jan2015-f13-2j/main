package entity;

import java.util.Date;

public class NormalTask extends Task {

	private Date startDateTime, endDateTime;

	public NormalTask(String taskName, int priority, Date startDateTime,
			Date endDateTime) {

		super(taskName, priority);
		this.setStartDateTime(startDateTime);
		this.setEndDateTime(endDateTime);
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
		super.setSortDate(startDateTime);
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		if (endDateTime == null) {
			this.endDateTime = this.getStartDateTime();
		} else {
			this.endDateTime = endDateTime;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((endDateTime == null) ? 0 : endDateTime.hashCode());
		result = prime * result
				+ ((startDateTime == null) ? 0 : startDateTime.hashCode());
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
		NormalTask other = (NormalTask) obj;
		if (endDateTime == null) {
			if (other.endDateTime != null)
				return false;
		} else if (!endDateTime.equals(other.endDateTime))
			return false;
		if (startDateTime == null) {
			if (other.startDateTime != null)
				return false;
		} else if (!startDateTime.equals(other.startDateTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NormalTask [startDateTime=" + startDateTime + ", endDateTime="
				+ endDateTime + ", getTaskId()=" + getTaskId()
				+ ", getTaskName()=" + getTaskName() + ", getPriority()="
				+ getPriority() + "]";
	}
}