package entity;

import java.util.Calendar;
import java.util.Date;

public class SuccessDisplay extends Success {

	private String displayType;
	private Calendar calendar;
	
	/**
	 * This is constructor for SuccessDisplay class .
	 *
	 * @param 	boolean
	 * 				true if the operation is successfully go through, else false.
	 * @param 	String
	 * 				the message that need to be return
	 * @return      
	 */
	//@author A0111837J
	public SuccessDisplay(boolean isSuccess, String message) {
		super(isSuccess, message);

	}
	
	/**
	 * This is constructor for SuccessDisplay class .
	 *
	 * @param 	String
	 * 				the message that need to be return
	 * @param 	Object
	 * 				object that need to be store inside the Success Display object.
	 * @param 	boolean
	 * 				true if the operation is successfully go through, else false.
	 * @param 	String
	 * 				the message that need to be return
	 * @return      
	 */
	//@author A0111837J
	public SuccessDisplay(String displayType, Object obj, boolean isSuccess,
			String message) {

		super(obj, isSuccess, message);
		setDisplayType(displayType);

	}
	/**
	 * This is the method to get the display type for SuccessDisplay Object .
	 *
	 * @return  String
	 * 				return the display type of the SuccessDisplay Object
	 */
	//@author A0111837J
	public String getDisplayType() {
		return displayType;
	}
	
	/**
	 * This is the method to set the display type for SuccessDisplay Object .
	 *
	 * @param  String
	 * 				set the display type of the SuccessDisplay Object
	 */
	//@author A0111837J
	private void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	
	/**
	 * This is the method to get the calendar for SuccessDisplay Object .
	 *
	 * @return  Calendar
	 * 				return the calendar of the SuccessDisplay Object
	 */
	//@author A0111837J
	public Calendar getCalendar() {
		return calendar;
	}
	
	/**
	 * This is the method to set the calendar for SuccessDisplay Object .
	 *
	 * @param  Calendar
	 * 				set the calendar of the SuccessDisplay Object
	 */
	//@author A0111837J
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	
	/**
	 * This is the method to set the date for SuccessDisplay Object .
	 *
	 * @param  Date
	 * 				set the date of the SuccessDisplay Object
	 */
	//@author A0111837J
	public void setCalendar(Date date) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		this.calendar = calendar;
	}
	
	/**
	 *
	 * Generate the SuccessDisplay property into String and it is for display purpose
	 *	
	 * @return	String
	 * 				the String generated from the SuccessDisplay property for display purpose
	 */
	//@author A0111916M
	@Override
	public String toString() {
		return "SuccessDisplay [displayType=" + displayType + ", calendar="
				+ calendar + ", getDisplayType()=" + getDisplayType()
				+ ", getCalendar()=" + getCalendar() + "]";
	}
}
