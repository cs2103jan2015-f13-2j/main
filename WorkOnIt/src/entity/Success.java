package entity;

public class Success {
	
	private Object obj;
	private boolean isSuccess;
	private String message;

	public Success(boolean isSuccess, String message) {

		this(null, isSuccess, message);
	}
	
	public Success(Object obj, boolean isSuccess, String message) {
		
		this.setObj(obj);
		this.setSuccess(isSuccess);
		this.setMessage(message);
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	private void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		return "Success [obj=" + obj + ", isSuccess=" + isSuccess
				+ ", message=" + message + "]";
	}
}
