package entity;

import java.util.ArrayList;

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
	
	@Override
	public boolean equals(Object other){
		boolean isSame = false;
	
		Success otherSuccess = (Success) other;
		System.out.println(this);
		System.out.println(otherSuccess);
		if(otherSuccess.getMessage().equals(this.getMessage())){
			if(otherSuccess.getObj()!=null && this.getObj()!=null){
				Object returnObj = otherSuccess.getObj();
				Object thisObj = this.getObj();
				
				if (returnObj instanceof ArrayList<?>){
					ArrayList<?> otherList = (ArrayList<?>)returnObj;
					ArrayList<?> thisList = (ArrayList<?>)thisObj;
					//System.out.println(otherList.size() + " " + thisList.size());
					if(otherList.size() == thisList.size()){
						isSame = true;
					}
				}
			} else {
				isSame = true;
			}
		}
		
		return isSame;
		
	}
}
