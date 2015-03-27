package entity;

import java.util.Calendar;
import java.util.Date;

public class SuccessDisplay extends Success {

	private String displayType;
	private Calendar calendar;

	public SuccessDisplay(boolean isSuccess, String message) {
		super(isSuccess, message);

	}

	public SuccessDisplay(String displayType, Object obj, boolean isSuccess,
			String message) {

		super(obj, isSuccess, message);
		setDisplayType(displayType);

	}

	public String getDisplayType() {
		return displayType;
	}

	private void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	
	public void setCalendar(Date date) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		this.calendar = calendar;
	}

	@Override
	public String toString() {
		return "SuccessDisplay [displayType=" + displayType + ", calendar="
				+ calendar + ", getDisplayType()=" + getDisplayType()
				+ ", getCalendar()=" + getCalendar() + "]";
	}
}
