package entity;

import java.util.Date;

import resource.KeywordConstant;

public class DeadlineTask extends Task {

	private Date deadline;
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public DeadlineTask(String taskName, int priority, Date deadline) {

		super(taskName, priority);
		this.setDeadline(deadline);
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Date getDeadline() {
		return deadline;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
		super.setSortDate(deadline);
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
				+ ((deadline == null) ? 0 : deadline.hashCode());
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
	 * @param  	
	 * @return      
	 */
	//@author 
	@Override
	public String toString() {
		return "DeadlineTask [deadline=" + deadline + ", getTaskId()="
				+ getTaskId() + ", getTaskName()=" + getTaskName()
				+ ", getPriority()=" + getPriority() + "]";
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
		userString += " " + KeywordConstant.KEYWORD_BY;
		userString += " " + DATE_FORMAT.format(getDeadline());

		if (this.getPriority() != KeywordConstant.PRIORITY_MEDIUM) {
			userString += " " + KeywordConstant.KEYWORD_PRIORITY;
			userString += " " + this.getPriority();
		}

		return userString;
	}
}