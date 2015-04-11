package entity;

import java.util.Date;

import resource.KeywordConstant;

public class NormalTask extends Task {

	private Date startDateTime, endDateTime;
	
	/**
	 * This is constructor for normal task.
	 *
	 * @param 	String
	 * 				The task name that need to be created
	 * @param 	int
	 * 				The priority of task that need to be created
	 * @param 	Date
	 * 				The start date of the normal of the task that need to be created
	 * @param 	Date
	 * 				The end date of the normal of the task that need to be created
	 * @return      
	 */
	//@author A0111916M 
	public NormalTask(String taskName, int priority, Date startDateTime,
			Date endDateTime) {

		super(taskName, priority);
		this.setStartDateTime(startDateTime);
		this.setEndDateTime(endDateTime);
	}
	
	/**
	 * This is the method to get start date from the normal task.
	 *
	 * @return  Date
	 * 				The start date from the normal Task.
	 */
	//@author A0111916M
	public Date getStartDateTime() {
		return startDateTime;
	}
	
	/**
	 * This is the method to set start date for the normal task.
	 *
	 * @param 	Date
	 * 				The start date of the task that need to be created
	 *
	 */
	//@author A0111916M
	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
		super.setSortDate(startDateTime);
	}
	
	/**
	 * This is the method to get end date from the normal task.
	 *
	 * @return  Date
	 * 				The end date from the normal Task.
	 */
	//@author A0111916M
	public Date getEndDateTime() {
		return endDateTime;
	}
	
	/**
	 * This is the method to set end date for the normal task.
	 *
	 * @param 	Date
	 * 				The end date of the task that need to be created
	 *
	 */
	//@author A0111916M
	public void setEndDateTime(Date endDateTime) {
		if (endDateTime == null) {
			this.endDateTime = this.getStartDateTime();
		} else {
			this.endDateTime = endDateTime;
		}
	}
	
	/**
	 *
	 *This is to generate the hash code from the normal task
	 *	
	 * @return   int
	 * 				The hash code generated.
	 */
	//@author A0111916M
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
	
	/**
	 *
	 *Compare between 2 normal task whether they are the same or not.
	 *
	 * @param  	Object
	 * 				The parsed in object that need to be compared
	 * @return  boolean
	 * 				return true if both normal task are the same, else false.
	 */
	//@author A0111916M
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
	
	/**
	 *
	 * Generate the normal task property into String
	 *	
	 * @return	String
	 * 				the String generated from the normal Task property
	 */
	//@author A0111916M
	@Override
	public String toString() {
		return "NormalTask [startDateTime=" + startDateTime + ", endDateTime="
				+ endDateTime + ", getTaskId()=" + getTaskId()
				+ ", getTaskName()=" + getTaskName() + ", getPriority()="
				+ getPriority() + "]";
	}
	
	/**
	 *
	 * Generate the normal task property into String and it is for display purpose
	 *	
	 * @return	String
	 * 				the String generated from the normal Task property for display purpose
	 */
	//@author A0111916M
	@Override
	public String toDisplay() {
		String userString = "";

		userString += this.getTaskName();

		if (this.getStartDateTime().equals(this.getEndDateTime())) {
			userString += " " + KeywordConstant.KEYWORD_ON;
			userString += " " + DATE_FORMAT.format(this.getStartDateTime());

		} else {
			userString += " " + KeywordConstant.KEYWORD_FROM;
			userString += " " + DATE_FORMAT.format(this.getStartDateTime());
			userString += " " + KeywordConstant.KEYWORD_TO;
			userString += " " + DATE_FORMAT.format(this.getEndDateTime());
		}

		if (this.getPriority() != KeywordConstant.PRIORITY_MEDIUM) {
			userString += " " + KeywordConstant.KEYWORD_PRIORITY;
			userString += " " + this.getPriority();
		}

		return userString;
	}
}