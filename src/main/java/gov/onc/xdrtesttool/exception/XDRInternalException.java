package gov.onc.xdrtesttool.exception;

public class XDRInternalException extends XDRException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public XDRInternalException(String reason) {
		super(reason, "Internal Error");
	}


	public XDRInternalException(String msg, Throwable cause) {
		super(msg,  "Internal Error", cause);
	}
}
