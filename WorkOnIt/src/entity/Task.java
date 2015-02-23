package entity;

import java.util.Comparator;
import java.util.Date;

public abstract class Task {

	private String taskName;
	private long taskId;
	private int priority;
	private Date sortDate;

	public Task(String taskName, int priority) {

		long generatedTaskId = System.currentTimeMillis();

		this.setTaskId(generatedTaskId);
		this.setTaskName(taskName);
		this.setPriority(priority);

		long epochOffset = 0L;
		this.setSortDate(new Date(epochOffset));
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
		result = prime * result + priority;
		result = prime * result
				+ ((sortDate == null) ? 0 : sortDate.hashCode());
		result = prime * result + (int) (taskId ^ (taskId >>> 32));
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
		if (priority != other.priority)
			return false;
		if (sortDate == null) {
			if (other.sortDate != null)
				return false;
		} else if (!sortDate.equals(other.sortDate))
			return false;
		if (taskId != other.taskId)
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

			return returnVal;
		}
	};

	public abstract String toString();
}
