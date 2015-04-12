package entity;

import java.util.Date;

import resource.KeywordConstant;

public class RecurrenceTask extends Task {

	private long tagId;
	private Date startRecurrenceDate, endRecurrenceDate;
	private String occurenceType;

	/**
	 * This is constructor for recurrence task.
	 *
	 * @param String
	 *            The task name that need to be created
	 * @param int The priority of task that need to be created
	 * @param Date
	 *            The starting recurrence date of the recurrence of the task
	 *            that need to be created
	 * @param Date
	 *            The ending recurrence date of the recurrence of the task that
	 *            need to be created
	 * @return
	 */
	//@author A0111916M - unused
	//dropped support for recurrence task
	public RecurrenceTask(String taskName, int priority,
			Date startRecurrenceDate, Date endRecurrenceDate) {

		super(taskName, priority);

		long generatedTagId = System.currentTimeMillis();
		this.setTagId(generatedTagId);
		this.setStartRecurrenceDate(startRecurrenceDate);
		this.setEndRecurrenceDate(endRecurrenceDate);
		this.setOccurenceType(KeywordConstant.KEYWORD_DEFAULT_OCCURENCE);

	}

	/**
	 * This is constructor for recurrence task.
	 *
	 * @param String
	 *            The task name that need to be created
	 * @param int The priority of task that need to be created
	 * @param Date
	 *            The starting recurrence date of the recurrence of the task
	 *            that need to be created
	 * @param Date
	 *            The ending recurrence date of the recurrence of the task that
	 *            need to be created
	 * @param String
	 *            The occurrence type of the task.
	 * @return
	 */

	public RecurrenceTask(String taskName, int priority,
			Date startRecurrenceDate, Date endRecurrenceDate,
			String occurenceType) {

		super(taskName, priority);

		long generatedTagId = System.currentTimeMillis();
		this.setTagId(generatedTagId);
		this.setStartRecurrenceDate(startRecurrenceDate);
		this.setEndRecurrenceDate(endRecurrenceDate);
		this.setOccurenceType(occurenceType);

	}

	/**
	 * This is the method to get Tag ID from the recurrence task.
	 *
	 * @return Long The Tag ID from the recurrence Task.
	 */

	public long getTagId() {
		return tagId;
	}

	/**
	 * This is the method to set Tag ID for the recurrence task.
	 *
	 * @param Long
	 *            The Tag ID of the task that need to be created
	 *
	 */

	private void setTagId(long tagId) {
		this.tagId = tagId;
	}

	/**
	 * This is the method to get start recurrence date from the recurrence task.
	 *
	 * @return Date The start recurrence date from the recurrence Task.
	 */

	public Date getStartRecurrenceDate() {
		return startRecurrenceDate;
	}

	/**
	 * This is the method to set start recurrence date for the recurrence task.
	 *
	 * @param Date
	 *            The start recurrence date of the task that need to be created
	 *
	 */

	public void setStartRecurrenceDate(Date startRecurrenceDate) {
		this.startRecurrenceDate = startRecurrenceDate;
		super.setSortDate(startRecurrenceDate);
	}

	/**
	 * This is the method to get end recurrence date from the recurrence task.
	 *
	 * @return Date The end recurrence date from the recurrence Task.
	 */

	public Date getEndRecurrenceDate() {
		return endRecurrenceDate;
	}

	/**
	 * This is the method to set end recurrence date for the recurrence task.
	 *
	 * @param Date
	 *            The end recurrence date of the task that need to be created
	 *
	 */

	public void setEndRecurrenceDate(Date endRecurrenceDate) {
		if (endRecurrenceDate == null) {
			this.endRecurrenceDate = this.getStartRecurrenceDate();
		} else {
			this.endRecurrenceDate = endRecurrenceDate;
		}
	}

	/**
	 * This is the method to get occurrence type from the recurrence task.
	 *
	 * @return String The occurrence type from the recurrence Task.
	 */

	public String getOccurenceType() {
		return occurenceType;
	}

	/**
	 * This is the method to set occurrence type for the recurrence task.
	 *
	 * @param String
	 *            Set occurrence type for the recurrence Task.
	 */

	public void setOccurenceType(String occurenceType) {
		this.occurenceType = occurenceType;
	}

	/**
	 *
	 * This is to generate the hash code from the recurrence task
	 *
	 * @return int The hash code generated.
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((endRecurrenceDate == null) ? 0 : endRecurrenceDate
						.hashCode());
		result = prime * result
				+ ((occurenceType == null) ? 0 : occurenceType.hashCode());
		result = prime
				* result
				+ ((startRecurrenceDate == null) ? 0 : startRecurrenceDate
						.hashCode());
		result = prime * result + (int) (tagId ^ (tagId >>> 32));
		return result;
	}

	/**
	 *
	 * Compare between 2 recurrence task whether they are the same or not.
	 *
	 * @param Object
	 *            The parsed in object that need to be compared
	 * @return boolean return true if both recurrence task are the same, else
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
		RecurrenceTask other = (RecurrenceTask) obj;
		if (endRecurrenceDate == null) {
			if (other.endRecurrenceDate != null)
				return false;
		} else if (!endRecurrenceDate.equals(other.endRecurrenceDate))
			return false;
		if (occurenceType == null) {
			if (other.occurenceType != null)
				return false;
		} else if (!occurenceType.equals(other.occurenceType))
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

	/**
	 *
	 * Generate the recurrence task property into String
	 *
	 * @return String the String generated from the recurrence Task property
	 */

	@Override
	public String toString() {
		return "RecurrenceTask [tagId=" + tagId + ", startRecurrenceDate="
				+ startRecurrenceDate + ", endRecurrenceDate="
				+ endRecurrenceDate + ", occurenceType=" + occurenceType
				+ ", getTaskId()=" + getTaskId() + ", getTaskName()="
				+ getTaskName() + ", getPriority()=" + getPriority()
				+ ", getSortDate()=" + getSortDate() + ", getDateCreated()="
				+ getDateCreated() + "]";
	}

	/**
	 *
	 * Generate the recurrence task property into String and it is for display
	 * purpose
	 *
	 * @return String the String generated from the recurrence Task property for
	 *         display purpose
	 */

	@Override
	public String toDisplay() {
		String userString = "";

		userString += this.getTaskName();

		if (this.getStartRecurrenceDate().equals(this.getEndRecurrenceDate())) {
			userString += " " + KeywordConstant.KEYWORD_EVERY;
			userString += " "
					+ DATE_FORMAT.format(this.getStartRecurrenceDate());

		} else {
			userString += " " + KeywordConstant.KEYWORD_EVERY;
			userString += " "
					+ DATE_FORMAT.format(this.getStartRecurrenceDate());
			userString += " " + KeywordConstant.KEYWORD_TO;
			userString += " " + DATE_FORMAT.format(this.getEndRecurrenceDate());
		}

		userString += " " + this.getOccurenceType();

		if (this.getPriority() != KeywordConstant.PRIORITY_MEDIUM) {
			userString += " " + KeywordConstant.KEYWORD_PRIORITY;
			userString += " " + this.getPriority();
		}

		return userString;
	}
}