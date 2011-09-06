package org.gov.sunayana.app;

public class ButtonBean {
	protected String buttonName;
	protected int attemptedNumber;
	protected int failedRandomNumber;
	public int getFailedRandomNumber() {
		return failedRandomNumber;
	}
	public void setFailedRandomNumber(int failedRandomNumber) {
		this.failedRandomNumber = failedRandomNumber;
	}
	public String getButtonName() {
		return buttonName;
	}
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	public int getAttemptedNumber() {
		return attemptedNumber;
	}
	public void setAttemptedNumber(int attemptedNumber) {
		this.attemptedNumber = attemptedNumber;
	}
	
	

}
