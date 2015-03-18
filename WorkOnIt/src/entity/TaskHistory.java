package entity;

public class TaskHistory {

	private Task task, auxTask;
	private String operation;

	public TaskHistory(String operation, Task task) {
		this(operation, task, null);
	}

	public TaskHistory(String operation, Task task, Task auxTask) {
		this.setOperation(operation);
		this.setTask(task);
		this.setAuxTask(auxTask);
	}

	public Task getTask() {
		return task;
	}

	private void setTask(Task task) {
		this.task = task;
	}

	public Task getAuxTask() {
		return auxTask;
	}

	private void setAuxTask(Task auxTask) {
		this.auxTask = auxTask;
	}

	public String getOperation() {
		return operation;
	}

	private void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auxTask == null) ? 0 : auxTask.hashCode());
		result = prime * result
				+ ((operation == null) ? 0 : operation.hashCode());
		result = prime * result + ((task == null) ? 0 : task.hashCode());
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
		TaskHistory other = (TaskHistory) obj;
		if (auxTask == null) {
			if (other.auxTask != null)
				return false;
		} else if (!auxTask.equals(other.auxTask))
			return false;
		if (operation == null) {
			if (other.operation != null)
				return false;
		} else if (!operation.equals(other.operation))
			return false;
		if (task == null) {
			if (other.task != null)
				return false;
		} else if (!task.equals(other.task))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaskHistory [task=" + task.toString() + ", auxTask="
				+ auxTask.toString() + ", operation=" + operation + "]";
	}

}
