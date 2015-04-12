package entity;

import resource.KeywordConstant;

public class FloatingTask extends Task {

	/**
	 * This is constructor for floating task.
	 *
	 * @param String
	 *            The task name that need to be created
	 * @param int The priority of task that need to be created
	 * @return
	 */
	//@author A0111916M
	public FloatingTask(String taskName, int priority) {

		super(taskName, priority);
	}

	/**
	 *
	 * Generate the floating task property into String
	 *
	 * @return String the string generated from the floating Task property
	 */

	@Override
	public String toString() {
		return "FloatingTask [getTaskId()=" + getTaskId() + ", getTaskName()="
				+ getTaskName() + ", getPriority()=" + getPriority() + "]";
	}

	/**
	 *
	 * Generate the floating task property into String and it is for display
	 * purpose
	 *
	 * @return String the string generated from the floating Task property for
	 *         display purpose
	 */

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