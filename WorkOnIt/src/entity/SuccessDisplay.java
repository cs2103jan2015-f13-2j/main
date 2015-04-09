package entity;

import java.util.Calendar;
import java.util.Date;

public class SuccessDisplay extends Success {

	private String displayType;
	private Calendar calendar;
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public SuccessDisplay(boolean isSuccess, String message) {
		super(isSuccess, message);

	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public SuccessDisplay(String displayType, Object obj, boolean isSuccess,
			String message) {

		super(obj, isSuccess, message);
		setDisplayType(displayType);

	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public String getDisplayType() {
		return displayType;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	private void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Calendar getCalendar() {
		return calendar;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public void setCalendar(Date date) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		this.calendar = calendar;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	@Override
	public String toString() {
		return "SuccessDisplay [displayType=" + displayType + ", calendar="
				+ calendar + ", getDisplayType()=" + getDisplayType()
				+ ", getCalendar()=" + getCalendar() + "]";
	}
}
