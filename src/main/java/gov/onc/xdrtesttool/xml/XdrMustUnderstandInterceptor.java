package gov.onc.xdrtesttool.xml;

import org.apache.log4j.Logger;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.server.SoapEndpointInterceptor;

import javax.xml.namespace.QName;

public class XdrMustUnderstandInterceptor implements SoapEndpointInterceptor {
	private final Logger log = Logger.getLogger(this.getClass().toString());

	@Override
    public boolean understands(SoapHeaderElement header) {
        return true;
    }

	@Override
	public boolean handleRequest(MessageContext messageContext, Object endpoint)
			throws Exception {
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext, Object endpoint)
			throws Exception {
		SaajSoapMessage message = (SaajSoapMessage)messageContext.getResponse();
		message.convertToXopPackage();
		SoapHeader soapHeader = message.getSoapHeader();

		QName wsaActionQName = new QName("http://www.w3.org/2005/08/addressing", "Action", "wsa");
		SoapHeaderElement wsaAction =  soapHeader.addHeaderElement(wsaActionQName);
		wsaAction.setText("urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse");
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext, Object endpoint)
			throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
	}

}