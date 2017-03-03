package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import org.springframework.ws.soap.SoapMessage;

public abstract class XDRValidator {
	public abstract void validate(SoapMessage soapMsg, MessageRecorder errorRecorder, ValidationContext vContext);
	public abstract String getName();
}
