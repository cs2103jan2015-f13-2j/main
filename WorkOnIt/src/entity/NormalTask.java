package entity;

import java.util.Date;

import resource.KeywordConstant;

public class NormalTask extends Task {

	private Date startDateTime, endDateTime;
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public NormalTask(String taskName, int priority, Date startDateTime,
			Date endDateTime) {

		super(taskName, priority);
		this.setStartDateTime(startDateTime);
		this.setEndDateTime(endDateTime);
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Date getStartDateTime() {
		return startDateTime;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
		super.setSortDate(startDateTime);
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Date getEndDateTime() {
		return endDateTime;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public void setEndDateTime(Date endDateTime) {
		if (endDateTime == null) {
			this.endDateTime = this.getStartDateTime();
		} else {
			this.endDateTime = endDateTime;
		}
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
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
	 * @param  	
	 * @return      
	 */
	//@author 
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
	 * @param  	
	 * @return      
	 */
	//@author 
	@Override
	public String toString() {
		return "NormalTask [startDateTime=" + startDateTime + ", endDateTime="
				+ endDateTime + ", getTaskId()=" + getTaskId()
				+ ", getTaskName()=" + getTaskName() + ", getPriority()="
				+ getPriority() + "]";
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
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