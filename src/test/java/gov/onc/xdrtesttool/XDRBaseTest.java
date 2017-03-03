package gov.onc.xdrtesttool;

import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;

public abstract class XDRBaseTest {
	protected String fileName = "ProvideAndRegisterDocumentSet-bRequest_SOAP.xml";

	protected SoapMessage getSoapMessage()
	{
    	InputStream input = null;
    	try {
    		MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
    		SaajSoapMessageFactory factory = new SaajSoapMessageFactory(messageFactory);
    		
    		input = this.getClass().getClassLoader().getResourceAsStream(fileName);
    		assertNotNull(input);
    		return factory.createWebServiceMessage(input);
    	} catch (IOException ex) {
    		ex.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		} finally {
        	if(input!=null){
        		try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	}
        }	
    	return null;
	}
	
	public abstract void validate();
}
