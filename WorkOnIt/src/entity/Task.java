package entity;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import resource.KeywordConstant;

public abstract class Task {

	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"dd MMM yyyy,  h:mm:ss a");

	private String taskName;
	private long taskId;
	private int priority;
	private Date sortDate;
	private boolean isCompleted;

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public Task(String taskName, int priority) {

		long generatedTaskId = System.currentTimeMillis();

		this.setTaskId(generatedTaskId);
		this.setTaskName(taskName);
		this.setPriority(priority);
		this.setCompleted(false);

		long maxDateOffset = Long.MAX_VALUE;
		this.setSortDate(new Date(maxDateOffset));
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public long getTaskId() {
		return taskId;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	private void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public String getTaskName() {
		return taskName;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public int getPriority() {
		return priority;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public void setPriority(int priority) {

		if (priority > KeywordConstant.PRIORITY_MAX) {
			this.priority = KeywordConstant.PRIORITY_MAX;
		} else if (priority < KeywordConstant.PRIORITY_MIN) {
			this.priority = KeywordConstant.PRIORITY_MIN;
		} else {
			this.priority = priority;
		}
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public boolean isCompleted() {
		return isCompleted;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public Date getSortDate() {
		return sortDate;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public void setSortDate(Date sortDate) {
		this.sortDate = sortDate;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public Date getDateCreated() {

		Date dateCreated = null;

		dateCreated = new Date(this.taskId);

		return dateCreated;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
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

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
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

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
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

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public abstract String toString();

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public abstract String toDisplay();
}