package gov.onc.xdrtesttool;

import gov.onc.xdrtesttool.error.XDRMessageRecorder;
import gov.onc.xdrtesttool.validate.SOAPDirectHeaderValidator;
import org.junit.Test;
import org.springframework.ws.soap.SoapMessage;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SOAPDirectHeaderValidatorTest extends XDRBaseTest {
	@Test
	public void validate() {
		SoapMessage message = getSoapMessage();
		SOAPDirectHeaderValidator validator = new SOAPDirectHeaderValidator();
		XDRMessageRecorder errorRecorder = new XDRMessageRecorder();
		validator.validate(message, errorRecorder, null);
		List errors = errorRecorder.getMessageErrors();
		assertEquals(errors.size(), 0);
		List warnings = errorRecorder.getMessageWarnings();
		assertEquals(warnings.size(), 0);
		List infos = errorRecorder.getMessageInfos();
		assertEquals(infos.size(), 0);
	}

}
