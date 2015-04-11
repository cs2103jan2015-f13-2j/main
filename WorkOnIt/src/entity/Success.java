package entity;

import java.util.ArrayList;

public class Success {

	private Object obj;
	private boolean isSuccess;
	private String message;
	
	/**
	 * This is constructor for Success class .
	 *
	 * @param 	boolean
	 * 				true if the operation is successfully go through, else false.
	 * @param 	String
	 * 				the message that need to be return
	 * @return      
	 */
	//@author A0111837J
	public Success(boolean isSuccess, String message) {

		this(null, isSuccess, message);
	}
	
	/**
	 * This is constructor for Success class .
	 *
	 * @param 	Object
	 * 				object that need to be store inside the Success object.
	 * @param 	boolean
	 * 				true if the operation is successfully go through, else false.
	 * @param 	String
	 * 				the message that need to be return
	 * 
	 * @return      
	 */
	//@author A0111837J
	public Success(Object obj, boolean isSuccess, String message) {

		this.setObj(obj);
		this.setSuccess(isSuccess);
		this.setMessage(message);
	}
	
	/**
	 * This is the method to get whether the Success Object is success or not
	 *
	 * @return  boolean
	 * 				return true Success Object is successfully go through.
	 */
	//@author A0111837J
	public boolean isSuccess() {
		return isSuccess;
	}
	
	/**
	 * This is the method to setSuccess Object is success or not
	 *
	 * param  boolean
	 * 				set whether is success object is true or false.
	 */
	//@author A0111837J
	private void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	/**
	 * This is the method to get message from the Success Object 
	 *
	 * @return  String
	 * 				return success message from the success object.
	 */
	//@author A0111837J
	public String getMessage() {
		return message;
	}
	/**
	 * This is the method to set message for the Success Object 
	 *
	 * @return  String
	 * 				set success message for the success object.
	 */
	//@author A0111837J
	private void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * This is the method to get object from the Success Object 
	 *
	 * @return  Object
	 * 				return object from the success object.
	 */
	//@author A0111837J
	public Object getObj() {
		return obj;
	}
	/**
	 * This is the method to set object for the Success Object 
	 *
	 * @return  Object
	 * 				set object for the Success Object .
	 */
	//@author A0111837J
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	/**
	 *
	 * Generate the Success Object property into String
	 *	
	 * @return	String
	 * 				the String generated from the Success Object property
	 */
	//@author A0111837J
	@Override
	public String toString() {
		return "Success [obj=" + obj + ", isSuccess=" + isSuccess
				+ ", message=" + message + "]";
	}
	/**
	 *
	 *Compare between 2 Success Object whether they are the same or not.
	 *
	 * @param  	Object
	 * 				The parsed in object that need to be compared
	 * @return  boolean
	 * 				return true if both Success Object are the same, else false.
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
