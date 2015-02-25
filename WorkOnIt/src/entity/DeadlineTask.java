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
		super.setSortDate(deadline);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((deadline == null) ? 0 : deadline.hashCode());
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
		DeadlineTask other = (DeadlineTask) obj;
		if (deadline == null) {
			if (other.deadline != null)
				return false;
		} else if (!deadline.equals(other.deadline))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeadlineTask [deadline=" + deadline + ", getTaskId()="
				+ getTaskId() + ", getTaskName()=" + getTaskName()
				+ ", getPriority()=" + getPriority() + "]";
	}
}