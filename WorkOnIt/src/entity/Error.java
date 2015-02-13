package entity;

public class Error {
	
	private boolean isSuccess;
	private String message;

	public Error(boolean isSuccess, String message) {
		
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

	@Override
	public String toString() {
		return "Error [isSuccess=" + isSuccess + ", message=" + message + "]";
	}

}
