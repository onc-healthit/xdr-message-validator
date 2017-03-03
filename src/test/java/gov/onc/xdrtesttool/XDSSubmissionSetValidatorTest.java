package gov.onc.xdrtesttool;

import gov.onc.xdrtesttool.error.XDRMessageRecorder;
import gov.onc.xdrtesttool.validate.XDSSubmissionSetValidator;
import org.junit.Test;
import org.springframework.ws.soap.SoapMessage;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class XDSSubmissionSetValidatorTest extends XDRBaseTest {
	@Test
	public void validate() {
		SoapMessage message = getSoapMessage();
		XDSSubmissionSetValidator validator = new XDSSubmissionSetValidator();
		XDRMessageRecorder errorRecorder = new XDRMessageRecorder();
		validator.validate(message, errorRecorder, null);
		List errors = errorRecorder.getMessageErrors();
		assertEquals(0, errors.size());
		List warnings = errorRecorder.getMessageWarnings();
		assertEquals(0, warnings.size());
		//List infos = errorRecorder.getMessageInfos();
		//assertEquals(0, infos.size());
	}

}
