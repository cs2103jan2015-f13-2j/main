package entity;

public class FloatingTask extends Task {

	public FloatingTask(String taskName, int priority) {

		super(taskName, priority);
	}

	@Override
	public String toString() {
		return "FloatingTask [getTaskId()=" + getTaskId() + ", getTaskName()="
				+ getTaskName() + ", getPriority()=" + getPriority() + "]";
	}
}