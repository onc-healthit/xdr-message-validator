package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import org.springframework.ws.soap.SoapMessage;

public class SchemaValidator extends XDRValidator{

	@Override
	public void validate(SoapMessage soapMsg, MessageRecorder errorRecorder,
			ValidationContext vContext) {
		ValidationUtil.validateSchema(soapMsg, errorRecorder);
	}

	@Override
	public String getName() {
		return "SchemaValidator";
	}

}
