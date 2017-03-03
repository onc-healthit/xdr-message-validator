package gov.onc.xdrtesttool.exception;

public class XDRFormatException extends XDRException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XDRFormatException(String msg, String resource) {
		super(msg, resource);
	}

	public XDRFormatException(String msg, String resource, Throwable cause) {
		super(msg, resource, cause);
	}


}
