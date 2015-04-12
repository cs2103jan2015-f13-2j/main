package entity;

import java.util.Date;

import resource.KeywordConstant;

public class DeadlineTask extends Task {

	private Date deadline;

	/**
	 * This is constructor for deadline task.
	 *
	 * @param String
	 *            The task name that need to be created
	 * @param int The priority of task that need to be created
	 * @param Date
	 *            The date of the deadline of the task that need to be created
	 * @return
	 */
	// @author A0111916M
	public DeadlineTask(String taskName, int priority, Date deadline) {

		super(taskName, priority);
		this.setDeadline(deadline);
	}

	/**
	 * This is the method to get date from the deadline task.
	 *
	 * @return Date The date from the deadline Task.
	 */

	public Date getDeadline() {
		return deadline;
	}

	/**
	 * This is the method to set date for the deadline task.
	 *
	 * @param Date
	 *            The date of the deadline of the task that need to be created
	 *
	 */

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
		super.setSortDate(deadline);
	}

	/**
	 *
	 * This is to generate the hash code from the deadline task
	 *
	 * @return int The hash code generated.
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((deadline == null) ? 0 : deadline.hashCode());
		return result;
	}

	/**
	 *
	 * Compare between 2 deadline task whether they are the same or not.
	 *
	 * @param Object
	 *            The parsed in object that need to be compared
	 * @return boolean return true if both deadline task are the same, else
	 *         false.
	 */

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

	/**
	 *
	 * Generate the deadline task property into String
	 *
	 * @return String the string generated from the deadline Task property
	 */

	@Override
	public String toString() {
		return "DeadlineTask [deadline=" + deadline + ", getTaskId()="
				+ getTaskId() + ", getTaskName()=" + getTaskName()
				+ ", getPriority()=" + getPriority() + "]";
	}

	/**
	 *
	 * Generate the deadline task property into String and it is for display
	 * purpose
	 *
	 * @return String the string generated from the deadline Task property for
	 *         display purpose
	 */

	@Override
	public String toDisplay() {
		String userString = "";

		userString += this.getTaskName();
		userString += " " + KeywordConstant.KEYWORD_BY;
		userString += " " + DATE_FORMAT.format(getDeadline());

		if (this.getPriority() != KeywordConstant.PRIORITY_MEDIUM) {
			userString += " " + KeywordConstant.KEYWORD_PRIORITY;
			userString += " " + this.getPriority();
		}

		return userString;
	}
}