package entity;

import logic.Validator;

public class FloatingTask extends Task {

	public FloatingTask(String taskName, int priority) {

		super(taskName, priority);
	}

	@Override
	public String toString() {
		return "FloatingTask [getTaskId()=" + getTaskId() + ", getTaskName()="
				+ getTaskName() + ", getPriority()=" + getPriority() + "]";
	}

	@Override
	public String toDisplay() {
		String userString = "";

		userString += this.getTaskName();

		if (this.getPriority() != Validator.PRIORITY_MEDIUM) {
			userString += " " + Validator.KEYWORD_PRIORITY;
			userString += " " + this.getPriority();
		}

		return userString;
	}
}