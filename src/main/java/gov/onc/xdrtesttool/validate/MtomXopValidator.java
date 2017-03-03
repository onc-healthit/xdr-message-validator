package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import org.springframework.ws.soap.SoapMessage;

public class MtomXopValidator extends XDRValidator{

	public void validate(SoapMessage soapMsg, MessageRecorder errorRecorder,
			ValidationContext vContext) {
		// TODO Auto-generated method stub
		
	}

	public String getName()
	{
		return "MtomXopValidator";
	}

}
