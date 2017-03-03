package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;
import gov.onc.xdrtesttool.xml.XMLParser;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.namespace.QName;
import java.util.Iterator;

public class SOAPGeneralValidator extends XDRValidator {

	private SoapMessage soapMsg;

	public void validate(SoapMessage soapMsg, MessageRecorder errorRecorder,
			ValidationContext vContext) {
		this.soapMsg = soapMsg;

		try {

			SoapHeader soapHeader = soapMsg.getSoapHeader();
			if (soapHeader == null) {
				errorRecorder.record("XDR_MSG_411", "Direct XDR Checklist",
						"S:Envelope", MessageType.Error);
				return;
			}
			OMElement header = XMLParser.parseXMLSource(soapHeader.getSource());
			
			SoapBody soapBody = soapMsg.getSoapBody();
			if (soapBody == null) {
				errorRecorder.record("XDR_MSG_412", "Direct XDR Checklist",
						"S:Envelope", MessageType.Error);
				return;
			}
			OMElement body = XMLParser.parseXMLSource(soapBody.getSource());


			//- @role SHOULD NOT be "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver" (this is allowed but not recommended; should generate a warning).
			//- There may be other instances of this element with different @role values, but this checklist does not address them.
			//- To field MUST be populated with the address in the RCPT TO field 			
			Iterator toIter = header.getChildrenWithLocalName("To");
			if(!toIter.hasNext())
				errorRecorder
				.record("XDR_MSG_413",
						"Direct XDR Checklist",
						"S:Envelope",
						MessageType.Error);
			else
			{
				OMElement toElement = (OMElement)toIter.next();
				OMAttribute roleAttr = toElement.getAttribute(new QName("http://www.w3.org/2003/05/soap-envelope","role"));
				if(roleAttr != null)
					errorRecorder
					.record("XDR_MSG_413_1",
							"Direct XDR Checklist",
							"S:Envelope",
							MessageType.Warning);

				//Verify: @mustUnderstand is either "true", "1" "false" or "0".  
				//SHOULD NOT be "false" or "0" (this is allowed but not recommended; should generate a warning).
				OMAttribute understandAttr = toElement.getAttribute(new QName("http://www.w3.org/2003/05/soap-envelope","mustUnderstand"));
				if(understandAttr == null)
					errorRecorder
					.record("XDR_MSG_414",
							"Direct XDR Checklist",
							"S:Envelope",
							MessageType.Info);
				else
				{
					//TODO
					/*
					if(!(understandAttr.getAttributeValue().equals("1") || understandAttr.getAttributeValue().equals("true")))
						errorRecorder
						.record("XDR_MSG_414_1",
								"Direct XDR Checklist",
								"S:Envelope",
								MessageType.Warning);
						*/
				}
				
			}
			//Test ID: 1
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getName()
	{
		return "SOAPGeneralValidator";
	}
	
}
