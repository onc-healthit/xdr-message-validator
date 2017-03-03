
package gov.onc.xdrtesttool.xdrservice.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;

public class XDRServiceClient {
	

	
	private static WebServiceTemplate wsTemplate = null;
	private static InputStream is;
	
	public static void  main(String[] args) throws Exception {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
		wsTemplate = (WebServiceTemplate) appContext.getBean("webServiceTemplate");
		is = new XDRServiceClient().getClass().getResourceAsStream("ProvideAndRegisterDocumentSet-bRequest.xml");		 
		 
		testXDRServiceRequest();
	}

	
	private static final void testXDRServiceRequest() {
			 invokeWS();
	}
	
	private static Result invokeWS() {
        StreamSource source = new StreamSource(is);
        StreamResult result = new StreamResult(System.out);
        wsTemplate.sendSourceAndReceiveToResult(source, new WebServiceMessageCallback(){
        	    /* (non-Javadoc)
        	     * @see org.springframework.ws.client.core.WebServiceMessageCallback#doWithMessage(org.springframework.ws.WebServiceMessage)
        	     */
        	    public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException{
					try {
        	    	SOAPMessage soapMessage = ((org.springframework.ws.soap.saaj.SaajSoapMessage)message).getSaajMessage();

        	    	SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
        	    	SOAPHeader header = soapMessage.getSOAPHeader();
        	    	//<a:Action s:mustUnderstand=\"1\">urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b</a:Action>
        	        QName actionQ = new QName("http://www.w3.org/2005/08/addressing", "Action", "a");
        	        SOAPHeaderElement actionElement = header.addHeaderElement(actionQ);
        	        QName mustUnderstand = new QName("http://www.w3.org/2003/05/soap-envelope", "mustUnderstand", "s");
        	        actionElement.addAttribute(mustUnderstand, "1");
        	        actionElement.setValue("urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b");
        	        
        	        //"<a:MessageID>urn:uuid:6d296e90-e5dc-43d0-b455-7c1f3eb35d83</a:MessageID>"
        	        QName messageQ = new QName("http://www.w3.org/2005/08/addressing", "MessageID", "a");
        	        SOAPHeaderElement messageElement = header.addHeaderElement(messageQ);
        	        messageElement.setValue("urn:uuid:6d296e90-e5dc-43d0-b455-7c1f3eb35d83");
        	        
        	        //"<a:ReplyTo>"
        	        QName replyQ = new QName("http://www.w3.org/2005/08/addressing", "ReplyTo", "a");
        	        SOAPHeaderElement replyElement = header.addHeaderElement(replyQ);
        	        
        	        //"<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>"+
        	        QName addressQ = new QName("http://www.w3.org/2005/08/addressing", "Address", "a");
        	        SOAPElement addressElement = replyElement.addChildElement(addressQ);
        	        addressElement.setValue("http://www.w3.org/2005/08/addressing/anonymous");
        	        
        	        //"<a:To s:mustUnderstand=\"1\">http://localhost:2647/XdsService/IHEXDSRepository.svc</a:To>";
        	        QName toQ = new QName("http://www.w3.org/2005/08/addressing", "Address", "a");
        	        SOAPElement toElement;
						toElement = header.addHeaderElement(toQ);
        	        toElement.addAttribute(mustUnderstand, "1");
        	        addressElement.setValue("http://localhost:2647/XdsService/IHEXDSRepository.svc");

        	        QName directAddr = new QName("urn:direct:addressing", "addressBlock", "direct");
        	        SOAPHeaderElement directAddrElement = header.addHeaderElement(directAddr);
        	        QName roleAttr = new QName("urn:direct:addressing", "role", "direct");
        	        directAddrElement.addAttribute(roleAttr, "urn:direct:addressing:destination");
        	        QName relayAttr = new QName("urn:direct:addressing", "relay", "direct");
        	        directAddrElement.addAttribute(relayAttr, "true");
        	        QName metaLevelQ = new QName("urn:direct:addressing", "metadata-level", "direct");
        	        SOAPElement metaElement = directAddrElement.addChildElement(metaLevelQ);
					metaElement.setValue("minimal");

        	        QName fromQ = new QName("urn:direct:addressing", "from", "direct");
        	        SOAPElement fromElement = directAddrElement.addChildElement(fromQ);
        	        fromElement.setValue("mailto:jane@mroroionline.mrohisp.com");

        	        QName toDirQ = new QName("urn:direct:addressing", "to", "direct");
        	        SOAPElement toDirElement = directAddrElement.addChildElement(toDirQ);
        	        toDirElement.setValue("mailto:edwards@avianhospitals.hispmail.net");
					
					} catch (SOAPException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
        	    }
        	}, result);

        return result;
	}
	
	

	
	
}
