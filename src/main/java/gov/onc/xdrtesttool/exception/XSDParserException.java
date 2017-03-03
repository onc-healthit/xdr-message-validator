package gov.onc.xdrtesttool.exception;

public class XSDParserException extends XDRException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XSDParserException(String msg, String resource) {
		super(msg, resource);
	}

	public XSDParserException(String msg, String resource, Throwable cause) {
		super(msg, resource, cause);
	}

}
