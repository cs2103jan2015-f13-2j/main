package entity;

public class SuccessDisplay extends Success {

	private String displayType;

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

	@Override
	public String toString() {
		return "SuccessDisplay [getDisplayType()=" + getDisplayType()
				+ ", isSuccess()=" + isSuccess() + ", getMessage()="
				+ getMessage() + ", getObj()=" + getObj() + "]";
	}
}
