package gov.onc.xdrtesttool.exception;

public class XDRValidationException extends XDRException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public XDRValidationException(String reason, String resource) {
		super(reason, resource);
	}


	public XDRValidationException(String msg, String resource, Throwable cause) {
		super(msg, resource, cause);
	}

}
