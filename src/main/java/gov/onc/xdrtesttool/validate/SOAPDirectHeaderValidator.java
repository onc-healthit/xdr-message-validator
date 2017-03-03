package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;
import gov.onc.xdrtesttool.xml.XMLParser;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import javax.ws.rs.core.UriBuilder;
import java.util.Iterator;

public class SOAPDirectHeaderValidator extends XDRValidator{

	MessageRecorder errorRecorder;
	public void validate(SoapMessage soapMsg, MessageRecorder errorRecorder,
			ValidationContext vContext) {
		this.errorRecorder = errorRecorder;

		try {
			SoapHeader soapHeader = soapMsg.getSoapHeader();
			if (soapHeader == null) {
				errorRecorder.record("XDR_MSG_427", "Direct XDR Checklist",
						"S:Envelope", MessageType.Error);
				return;
			}
			OMElement header = XMLParser.parseXMLSource(soapHeader.getSource());

			
			if(header == null)
				errorRecorder.record("XDR_MSG_427", "Direct XDR Checklist",
						"S:Envelope", MessageType.Error);
			else
			{
				Iterator addressIter = header.getChildrenWithLocalName("addressBlock");
				if(!addressIter.hasNext())
					errorRecorder.record("XDR_MSG_427", "Direct XDR Checklist",
							"S:Envelope", MessageType.Info);
				else
				{
					//S:Envelope/S:Header/direct:AddressBlock - Optional 
					OMElement addressElement = (OMElement)addressIter.next();
					if(addressIter.hasNext())
						errorRecorder.record("XDR_MSG_427", "Direct XDR Checklist",
								"S:Envelope", MessageType.Error);
					//Verify:
					//	-The namespace ="urn:direct:addressing"
					OMNamespace ns = addressElement.getNamespace();
					if(ns == null || ns.getNamespaceURI() == null || !ns.getNamespaceURI().equals("urn:direct:addressing"))
						errorRecorder.record("XDR_MSG_427_1", "Direct XDR Checklist",
								"S:Envelope", MessageType.Error);
					
					//S:Envelope/S:Header/direct:AddressBlock/direct:to - Optional
					//To field is specified in the SOAP Header using the direct:to element and contains a value conformant to the anyURI type. 
					validateDirectTo(addressElement);

					//S:Envelope/S:Header/direct:AddressBlock/direct:from - Optional
					//To field is specified in the SOAP Header using the direct:from element and contains a value conformant to the anyURI type. 
					validateDirectFrom(addressElement);
					
					//S:Envelope/S:Header/direct:metadata-level - Optional
					// - If the context is Minimal Metadata, then value MUST be "minimal" or not present.
					// - If the context is Full Metadata, then value MUST be "XDS".
					validateMetadata(addressElement);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			errorRecorder.record("XDR_MSG_427", "Direct XDR Checklist",
					"SOAP: Direct Header", MessageType.Error);
		}
	}

	private void validateDirectTo(OMElement addressElement)
	{
		Iterator toIter = addressElement.getChildrenWithLocalName("to");
		if(!toIter.hasNext())
			errorRecorder.record("XDR_MSG_428", "Direct XDR Checklist",
					"SOAP: Direct Header", MessageType.Info);
		else
		{
			OMElement toElement = (OMElement) toIter.next();
			try
			{
				UriBuilder.fromUri(toElement.getText());
			}
			catch(IllegalArgumentException e)
			{
				errorRecorder.record("XDR_MSG_428_1", "Direct XDR Checklist",
						"SOAP: Direct Header", MessageType.Error);
			}
		}
	}
	
	private void validateDirectFrom(OMElement addressElement)
	{
		Iterator toIter = addressElement.getChildrenWithLocalName("from");
		if(!toIter.hasNext())
			errorRecorder.record("XDR_MSG_429", "Direct XDR Checklist",
					"SOAP: Direct Header", MessageType.Info);
		else
		{
			OMElement toElement = (OMElement) toIter.next();
			try
			{
				UriBuilder.fromUri(toElement.getText());
			}
			catch(IllegalArgumentException e)
			{
				errorRecorder.record("XDR_MSG_429_1", "Direct XDR Checklist",
						"SOAP: Direct Header", MessageType.Error);
			}
		}
	}

	private void validateMetadata(OMElement addressElement)
	{
		Iterator iter = addressElement.getChildrenWithLocalName("metadata-level");
		if(!iter.hasNext())
			errorRecorder.record("XDR_MSG_435", "Direct XDR Checklist",
					"SOAP: Direct Header.metadata-level", MessageType.Info);
		else
		{
			OMElement element = (OMElement) iter.next();
			String metaValue = element.getText();
			if(metaValue == null || !(metaValue.equals("minimal") || metaValue.equals("XDS")))
				errorRecorder.record("XDR_MSG_435_1", "Direct XDR Checklist",
						"SOAP: Direct Header.metadata-level", MessageType.Error);
		}
	}

	public String getName()
	{
		return "SOAPDirectHeaderValidator";
	}

}
