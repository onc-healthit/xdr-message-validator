package gov.onc.xdrtesttool.exception;

public class XMLParserException extends XDRException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XMLParserException(String msg, String resource) {
		super(msg, resource);
	}

	public XMLParserException(String msg, String resource, Throwable cause) {
		super(msg, resource, cause);
	}

}
