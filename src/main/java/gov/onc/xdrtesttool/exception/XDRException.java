package gov.onc.xdrtesttool.exception;

public class XDRException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String resource = null;   // pointer back into documentation 

	public XDRException(String msg, String resource) {
		super(msg);
		this.resource = resource;
	}

	public XDRException(String msg, String resource, Throwable cause) {
		super(msg, cause);
		this.resource = resource;
	}
	
	public String getResource() {
		return resource;
	}
	
	public String getDetails() {
		return ExceptionUtil.exception_details(this);
	}
}
