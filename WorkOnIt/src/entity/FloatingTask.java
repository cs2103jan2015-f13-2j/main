package entity;

import resource.KeywordConstant;

public class FloatingTask extends Task {
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public FloatingTask(String taskName, int priority) {

		super(taskName, priority);
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	@Override
	public String toString() {
		return "FloatingTask [getTaskId()=" + getTaskId() + ", getTaskName()="
				+ getTaskName() + ", getPriority()=" + getPriority() + "]";
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

		if (this.getPriority() != KeywordConstant.PRIORITY_MEDIUM) {
			userString += " " + KeywordConstant.KEYWORD_PRIORITY;
			userString += " " + this.getPriority();
		}

		return userString;
	}
}