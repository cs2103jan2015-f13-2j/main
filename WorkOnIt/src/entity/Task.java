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
	 * This is constructor for Task superclass .
	 *
	 * @param String
	 *            The task name that need to be created
	 * @param int The priority of task that need to be created
	 * @return
	 */
	//@author A0111916M
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
	 * This is the method to get Tag ID from the Task.
	 *
	 * @return Long The Tag ID from the Task.
	 */

	public long getTaskId() {
		return taskId;
	}

	/**
	 * This is the method to set Tag ID for the Task.
	 *
	 * @param Long
	 *            The Tag ID of the task that need to be created
	 *
	 */

	private void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	/**
	 * This is the method to get task name from the Task.
	 *
	 * @return String The task name from the Task.
	 */

	public String getTaskName() {
		return taskName;
	}

	/**
	 * This is the method to set task name for the Task.
	 *
	 * @param String
	 *            The task name of the task that need to be created
	 *
	 */

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * This is the method to get priority from the Task.
	 *
	 * @return int The priority from the Task.
	 */

	public int getPriority() {
		return priority;
	}

	/**
	 * This is the method to set priority for the Task.
	 *
	 * @param int The priority of the task that need to be created
	 *
	 */

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
	 * This is the method to get priority from the Task.
	 *
	 * @return boolean return true is the task is mark as completed, if not,
	 *         false
	 */

	public boolean isCompleted() {
		return isCompleted;
	}

	/**
	 * This is the method to set whether Task to complete or not.
	 *
	 * @param boolean set as true is the task is mark as completed, if not,
	 *        false.
	 *
	 */

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	//@author
	public Date getSortDate() {
		return sortDate;
	}

	/**
	 * This is the method to set sort date for the Task.
	 *
	 * @param String
	 *            The sort date of the task that need to be created
	 *
	 */

	public void setSortDate(Date sortDate) {
		this.sortDate = sortDate;
	}

	/**
	 * This is the method to get sort date from the Task.
	 *
	 * @return String The sort date from the Task.
	 */

	public Date getDateCreated() {

		Date dateCreated = null;

		dateCreated = new Date(this.taskId);

		return dateCreated;
	}

	/**
	 *
	 * This is to generate the hash code from the Task
	 *
	 * @return int The hash code generated.
	 */

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
	 * Compare between 2 task whether they are the same or not.
	 *
	 * @param Object
	 *            The parsed in object that need to be compared
	 * @return boolean return true if both task are the same, else false.
	 */

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
	 * comparator method to sort the task in the Array List base on it date and
	 * follow by priority
	 *
	 */

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
	 * This is an abstract method
	 */
	public abstract String toString();

	/**
	 * This is an abstract method
	 */
	public abstract String toDisplay();
}