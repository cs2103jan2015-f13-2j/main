package entity;

public class RecurrenceTask extends Task {

	//redefined!!
	private long tagId;
	
	public RecurrenceTask(String taskName, int priority) {
		
		super(taskName, priority);
		
		long generatedTagId = System.currentTimeMillis();
		this.setTagId(generatedTagId);
	}

	public long getTagId() {
		return tagId;
	}

	private void setTagId(long tagId) {
		this.tagId = tagId;
	}

	@Override
	public String toString() {
		return "RecurrenceTask [tagId=" + tagId + ", getTaskId()="
				+ getTaskId() + ", getTaskName()=" + getTaskName()
				+ ", getPriority()=" + getPriority() + "]";
	}
}
