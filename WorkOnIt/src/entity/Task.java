package entity;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public abstract class Task {

	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"dd MMM yyyy,  h:mm:ss a");

	private String taskName;
	private long taskId;
	private int priority;
	private Date sortDate;
	private boolean isCompleted;

	public Task(String taskName, int priority) {

		long generatedTaskId = System.currentTimeMillis();

		this.setTaskId(generatedTaskId);
		this.setTaskName(taskName);
		this.setPriority(priority);
		this.setCompleted(false);

		long maxDateOffset = Long.MAX_VALUE;
		this.setSortDate(new Date(maxDateOffset));
	}

	public long getTaskId() {
		return taskId;
	}

	private void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public Date getSortDate() {
		return sortDate;
	}

	public void setSortDate(Date sortDate) {
		this.sortDate = sortDate;
	}

	public Date getDateCreated() {

		Date dateCreated = null;

		dateCreated = new Date(this.taskId);

		return dateCreated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isCompleted ? 1231 : 1237);
		result = prime * result + priority;
		result = prime * result
				+ ((sortDate == null) ? 0 : sortDate.hashCode());
		result = prime * result
				+ ((taskName == null) ? 0 : taskName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (isCompleted != other.isCompleted)
			return false;
		if (priority != other.priority)
			return false;
		if (sortDate == null) {
			if (other.sortDate != null)
				return false;
		} else if (!sortDate.equals(other.sortDate))
			return false;
		if (taskName == null) {
			if (other.taskName != null)
				return false;
		} else if (!taskName.equals(other.taskName))
			return false;
		return true;
	}

	public static Comparator<Task> taskComparator = new Comparator<Task>() {

		@Override
		public int compare(Task task1, Task task2) {
			int returnVal;

			// sort task date in ascending order
			Date task1Date = task1.getSortDate();
			Date task2Date = task2.getSortDate();

			if (task1.isCompleted == task2.isCompleted) {
				if (task1Date.equals(task2Date)) {
					// sort priority in descending order
					// (high > medium > low)
					int task1Priority = task1.getPriority();
					int task2Priority = task2.getPriority();
					if (task1Priority == task2Priority) {
						// sort task by name
						String task1Desc = task1.getTaskName();
						String task2Desc = task2.getTaskName();
						returnVal = task1Desc.compareTo(task2Desc);
					} else {
						returnVal = task2Priority - task1Priority;
					}
				} else {
					returnVal = task1Date.compareTo(task2Date);
				}

			} else {
				if (task1.isCompleted) {
					returnVal = 1;
				} else {
					returnVal = -1;
				}

			}
			return returnVal;
		}
	};

	public abstract String toString();

	public abstract String toDisplay();
}