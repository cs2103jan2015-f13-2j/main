package entity;

public class FloatingTask extends Task {
	
	private static final int DEFAULT_PRIORITY = 1;

	public FloatingTask(String taskName) {
		
		super(taskName, DEFAULT_PRIORITY);
	}

	@Override
	public String toString() {
		return "FloatingTask [getTaskId()=" + getTaskId() + ", getTaskName()="
				+ getTaskName() + ", getPriority()=" + getPriority() + "]";
	}
}
