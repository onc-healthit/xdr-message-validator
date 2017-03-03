package gov.onc.xdrtesttool.error;


public class MessageRecorderItem {

	private String msg;
	private String extraMessage;
	private String resource;
	private String location;
	private String errorCode;
	private String validationStep;
	private MessageType messageType;
	public static enum ValidatorType {SOAPGeneral, WSAddressing, SOAPDirect, XDRRequest, XDSDocumentEntry, XDSSubmissionSet, MTOMXop };
	public static enum MessageType {Error, Warning, Info};
	

	public MessageRecorderItem(String errorCode, MessageType messageType) 
	{
		this.errorCode = errorCode;
		this.messageType = messageType;
	}

	public MessageRecorderItem(String errorCode, String resource, String location, MessageType messageType) 
	{
		this.resource = resource;
		this.location = location;
		this.errorCode = errorCode;
		this.messageType = messageType;
	}

	public String getMessage() {
		return msg;
	}

	public void setMessage(String msg) {
		this.msg = msg;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String code) {
		this.errorCode = code;
	}

	public String getValidationStep() {
		return validationStep;
	}

	public void setValidationStep(String validationStep) {
		this.validationStep = validationStep;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getExtraMessage() {
		return extraMessage;
	}

	public void setExtraMessage(String extraMessage) {
		this.extraMessage = extraMessage;
	} 
}
