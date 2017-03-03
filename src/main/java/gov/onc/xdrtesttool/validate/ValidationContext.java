package gov.onc.xdrtesttool.validate;

public class ValidationContext {

	public boolean isXDRLimited = false;  
	public boolean isXDRMinimal = false;  
	private String testFocus = null;
	private String requestId;
	
	
	public ValidationContext() {
	}
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	
}




