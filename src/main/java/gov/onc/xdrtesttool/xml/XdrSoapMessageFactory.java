package gov.onc.xdrtesttool.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.TransportInputStream;

import java.io.IOException;
import java.io.InputStream;

public class XdrSoapMessageFactory implements SoapMessageFactory,
		InitializingBean {
	// This is the Request Context Attribute.
	private static final String REQUEST_CONTEXT_ATTRIBUTE = "XdrSoapMessageFactory";

	private static final Log logger = LogFactory
			.getLog(XdrSoapMessageFactory.class);

	// Two message factories for processing two differnet types of protocols.
	private SaajSoapMessageFactory soap11MessageFactory = new SaajSoapMessageFactory();
	private SaajSoapMessageFactory soap12MessageFactory = new SaajSoapMessageFactory();

	// This Object, will be responsible for choosing the Protocol on Runtime, it
	// can be application/xml or text/xml (SOAP 1.2 & SOAP 1.1)
	private SoapProtocolChooser soapProtocolChooser = new XdrSoapProtocolChooser();

	private void setMessageFactoryForRequestContext(SaajSoapMessageFactory mf) {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		attrs.setAttribute(REQUEST_CONTEXT_ATTRIBUTE, mf,
				RequestAttributes.SCOPE_REQUEST);
	}

	private SaajSoapMessageFactory getMessageFactoryForRequestContext() {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		SaajSoapMessageFactory mf = (SaajSoapMessageFactory) attrs
				.getAttribute(REQUEST_CONTEXT_ATTRIBUTE,
						RequestAttributes.SCOPE_REQUEST);
		return mf;
	}

	// Function called, when we are settign the SOPA Version
	public void setSoapVersion(SoapVersion version) {
		System.out.println("setSoapVersion called with: " + version
				+ " -- ignoring");
	}

	// This Function, will set teh SOAP Proptocl chooser
	public void setSoapProtocolChooser(SoapProtocolChooser soapProtocolChooser) {
		System.out.println("Setting out the SOAP Protocol Chooser");
		this.soapProtocolChooser = soapProtocolChooser;
	}

	// Function will be invoked, when Spring will create the Bean.
	public void afterPropertiesSet() throws Exception {
		soap11MessageFactory.setSoapVersion(SoapVersion.SOAP_11);
		soap11MessageFactory.afterPropertiesSet();
		soap12MessageFactory.setSoapVersion(SoapVersion.SOAP_12);
		soap12MessageFactory.afterPropertiesSet();
		System.out.println("Setting both the SOAP Version to 1.1 and 1.2");
	}

	// Function for creating the Web Service Message.
	public SoapMessage createWebServiceMessage() {
		return getMessageFactoryForRequestContext().createWebServiceMessage();
	}

	// Function for creating the Web Service Message from inputStream.
	public SoapMessage createWebServiceMessage(InputStream inputStream)
			throws IOException {
		setMessageFactoryForRequestContext(soap12MessageFactory);
		if (inputStream instanceof TransportInputStream) {
			TransportInputStream transportInputStream = (TransportInputStream) inputStream;
			if (soapProtocolChooser.useSoap11(transportInputStream)) {
				setMessageFactoryForRequestContext(soap11MessageFactory);
			}
		}
		SaajSoapMessageFactory mf = getMessageFactoryForRequestContext();
		if (mf == soap11MessageFactory) {
			System.out.println("Final soapMessageFactory? "
					+ soap11MessageFactory);
		} else {
			System.out.println("Final soapMessageFactory? "
					+ soap12MessageFactory);
		}
		return mf.createWebServiceMessage(inputStream);
	}

}