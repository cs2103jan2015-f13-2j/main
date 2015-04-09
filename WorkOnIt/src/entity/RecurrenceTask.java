package entity;

import java.util.Date;

import resource.KeywordConstant;

public class RecurrenceTask extends Task {

	private long tagId;
	private Date startRecurrenceDate, endRecurrenceDate;
	private String occurenceType;
	
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
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
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
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
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public long getTagId() {
		return tagId;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	private void setTagId(long tagId) {
		this.tagId = tagId;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Date getStartRecurrenceDate() {
		return startRecurrenceDate;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public void setStartRecurrenceDate(Date startRecurrenceDate) {
		this.startRecurrenceDate = startRecurrenceDate;
		super.setSortDate(startRecurrenceDate);
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Date getEndRecurrenceDate() {
		return endRecurrenceDate;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public void setEndRecurrenceDate(Date endRecurrenceDate) {
		if (endRecurrenceDate == null) {
			this.endRecurrenceDate = this.getStartRecurrenceDate();
		} else {
			this.endRecurrenceDate = endRecurrenceDate;
		}
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public String getOccurenceType() {
		return occurenceType;
	}	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 

	public void setOccurenceType(String occurenceType) {
		this.occurenceType = occurenceType;
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
	 * @param  	
	 * @return      
	 */
	//@author 
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
	 * @param  	
	 * @return      
	 */
	//@author 
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