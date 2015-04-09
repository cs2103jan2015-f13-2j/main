package entity;

import java.util.ArrayList;

public class Success {

	private Object obj;
	private boolean isSuccess;
	private String message;
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Success(boolean isSuccess, String message) {

		this(null, isSuccess, message);
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Success(Object obj, boolean isSuccess, String message) {

		this.setObj(obj);
		this.setSuccess(isSuccess);
		this.setMessage(message);
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public boolean isSuccess() {
		return isSuccess;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	private void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public String getMessage() {
		return message;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	private void setMessage(String message) {
		this.message = message;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Object getObj() {
		return obj;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public void setObj(Object obj) {
		this.obj = obj;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	@Override
	public String toString() {
		return "Success [obj=" + obj + ", isSuccess=" + isSuccess
				+ ", message=" + message + "]";
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author A0111837J
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
