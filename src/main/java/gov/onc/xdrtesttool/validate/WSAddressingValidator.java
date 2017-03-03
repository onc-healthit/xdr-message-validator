package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;
import gov.onc.xdrtesttool.xml.XMLParser;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import javax.ws.rs.core.UriBuilder;
import javax.xml.namespace.QName;
import java.util.Iterator;

public class WSAddressingValidator extends XDRValidator {

	private MessageRecorder errorRecorder;

	public void validate(SoapMessage soapMsg, MessageRecorder errorRecorder,
			ValidationContext vContext) {
		this.errorRecorder = errorRecorder;

		try {
			SoapHeader soapHeader = soapMsg.getSoapHeader();
			if (soapHeader == null) {
				errorRecorder.record("XDR_MSG_412", "Direct XDR Checklist",
						"S:Envelope", MessageType.Error);
				return;
			}
			OMElement header = XMLParser.parseXMLSource(soapHeader.getSource());
			

			// - @role SHOULD NOT be
			// "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver"
			// (this is allowed but not recommended; should generate a warning).
			// - There may be other instances of this element with different
			// @role values, but this checklist does not address them.
			// - To field MUST be populated with the address in the RCPT TO
			// field
			validateTo(header);

			// - wsa:From is of type wsa:EndpointReferenceType.
			// - @role SHOULD NOT be
			// "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver"
			// (this is allowed but not recommended; should generate a warning).
			// - There may be other instances of this element with different
			// @role values, but this checklist does not address them.
			validateFrom(header);

			// -If this element is NOT present then the value of the [address]
			// property of the [reply endpoint] EPR is
			// "http://www.w3.org/2005/08/addressing/anonymous".
			// Description:
			// -This OPTIONAL element (of type wsa:EndpointReferenceType)
			// provides the value for the [reply endpoint] property.
			validateReplyTo(header);

			// This OPTIONAL element (of type wsa:EndpointReferenceType)
			// provides the value for the [fault endpoint] property."
			validateFaultTo(header);

			// - If present, matches the S:Header/wsa:MessageID(s) of the
			// original message(s) to which this is a reply. May contain
			// multiple MessageIDs.
			// - @role SHOULD NOT be
			// "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver"
			// (this is allowed but not recommended; should generate a warning).
			// - There may be other instances of this element with different
			// @role values, but this checklist does not address them.
			validateRelatesTo(header);

			// - Header/MessageID is unique identifier of this message. Reviewer
			// may keep a list of previously used MessageIDs for comparison.
			// While retransmits of identical messages are permitted, messages
			// with distinct application intent MUST have unique MessageIDs.
			// - @role SHOULD NOT be
			// "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver"
			// (this is allowed but not recommended; should generate a warning).
			// - There may be other instances of this element with different
			// @role values, but this checklist does not address them.
			validateMessageID(header);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void validateTo(OMElement header) {
		// - @role SHOULD NOT be
		// "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver" (this
		// is allowed but not recommended; should generate a warning).
		// - There may be other instances of this element with different @role
		// values, but this checklist does not address them.
		// - To field MUST be populated with the address in the RCPT TO field
		Iterator toIter = header.getChildrenWithLocalName("To");
		if (!toIter.hasNext())
			errorRecorder.record("XDR_MSG_413", "Direct XDR Checklist",
					"S:Envelope/S:Header/wsa:To", MessageType.Error);
		else {
			OMElement toElement = (OMElement) toIter.next();
			String toValue = toElement.getText();
			try {
				UriBuilder.fromUri(toValue);
			} catch (IllegalArgumentException e) {
				errorRecorder.record("XDR_MSG_413", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:To", MessageType.Error);
			}
			OMAttribute roleAttr = toElement.getAttribute(new QName("http://www.w3.org/2003/05/soap-envelope","role"));
			if (roleAttr != null)
				errorRecorder.record("XDR_MSG_413_1", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:To", MessageType.Warning);

			// Verify: @mustUnderstand is either "true", "1" "false" or "0".
			// SHOULD NOT be "false" or "0" (this is allowed but not
			// recommended; should generate a warning).
			OMAttribute understandAttr = toElement.getAttribute(new QName("http://www.w3.org/2003/05/soap-envelope","mustUnderstand"));
			if (understandAttr == null)
				errorRecorder.record("XDR_MSG_414", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:To", MessageType.Info);
			else {
				if (!(understandAttr.getAttributeValue().equals("1") || understandAttr
						.getAttributeValue().equals("true")))
					errorRecorder.record("XDR_MSG_414", "Direct XDR Checklist",
							"S:Envelope/S:Header/wsa:To", MessageType.Warning);

			}

		}
	}

	private void validateFrom(OMElement header) {
		Iterator toIter = header.getChildrenWithLocalName("From");
		if (!toIter.hasNext())
			errorRecorder.record("XDR_MSG_417", "Direct XDR Checklist",
					"S:Envelope/S:Header/wsa:From", MessageType.Info);
		else {
			OMElement toElement = (OMElement) toIter.next();
			try {
				UriBuilder.fromUri(toElement.getText());
			} catch (IllegalArgumentException e) {
				errorRecorder.record("XDR_MSG_417", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:From", MessageType.Error);
			}
			OMAttribute roleAttr = toElement.getAttribute(new QName("http://www.w3.org/2003/05/soap-envelope","role"));
			if (roleAttr != null)
				errorRecorder.record("XDR_MSG_417_1", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:From", MessageType.Warning);

			// Verify: @mustUnderstand is either "true", "1" "false" or "0".
			// SHOULD NOT be "false" or "0" (this is allowed but not
			// recommended; should generate a warning).
			OMAttribute understandAttr = toElement.getAttribute(new QName("http://www.w3.org/2003/05/soap-envelope","mustUnderstand"));
			if (understandAttr == null)
				errorRecorder.record("XDR_MSG_418", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:From", MessageType.Info);
			else {
				if (!(understandAttr.getAttributeValue().equals("1") || understandAttr
						.getAttributeValue().equals("true")))
					errorRecorder
							.record("XDR_MSG_418_1", "Direct XDR Checklist",
									"S:Envelope/S:Header/wsa:From",
									MessageType.Warning);

			}

		}
	}

	private void validateReplyTo(OMElement header) {
		Iterator toIter = header.getChildrenWithLocalName("ReplyTo");
		if (!toIter.hasNext())
			errorRecorder.record("XDR_MSG_419", "Direct XDR Checklist",
					"S:Envelope/S:Header/wsa:ReplyTo", MessageType.Info);
		else {
			OMElement toElement = (OMElement) toIter.next();

			OMAttribute roleAttr = toElement.getAttribute(new QName("http://www.w3.org/2003/05/soap-envelope","role"));
			if (roleAttr != null)
				errorRecorder.record("XDR_MSG_419_1", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:ReplyTo", MessageType.Warning);

			Iterator msgIdIter = header.getChildrenWithLocalName("MessageID");
			if (!msgIdIter.hasNext())
				errorRecorder.record("XDR_MSG_419_2", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:ReplyTo", MessageType.Warning);

			// Verify: @mustUnderstand is either "true", "1" "false" or "0".
			// SHOULD NOT be "false" or "0" (this is allowed but not
			// recommended; should generate a warning).
			OMAttribute understandAttr = toElement.getAttribute(new QName("http://www.w3.org/2003/05/soap-envelope","mustUnderstand"));
			if (understandAttr == null)
				errorRecorder.record("XDR_MSG_420", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:ReplyTo", MessageType.Error);
			else {
				if (!(understandAttr.getAttributeValue().equals("1") || understandAttr
						.getAttributeValue().equals("true")))
					errorRecorder.record("XDR_MSG_420_1", "Direct XDR Checklist",
							"S:Envelope/S:Header/wsa:ReplyTo",
							MessageType.Error);
			}

		}
	}

	private void validateFaultTo(OMElement header) {
		Iterator toIter = header.getChildrenWithLocalName("FaultTo");
		if (!toIter.hasNext())
			errorRecorder.record("XDR_MSG_421", "Direct XDR Checklist",
					"S:Envelope/S:Header/wsa:FaultTo", MessageType.Info);
		else {
			OMElement toElement = (OMElement) toIter.next();

			OMAttribute roleAttr = toElement.getAttribute(new QName("http://www.w3.org/2003/05/soap-envelope","role"));
			if (roleAttr != null)
				errorRecorder.record("XDR_MSG_421_1", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:FaultTo", MessageType.Warning);

			Iterator msgIdIter = header.getChildrenWithLocalName("MessageID");
			if (!msgIdIter.hasNext())
				errorRecorder.record("XDR_MSG_421_2", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:FaultTo", MessageType.Warning);

			// Verify: @mustUnderstand is either "true", "1" "false" or "0".
			// SHOULD NOT be "false" or "0" (this is allowed but not
			// recommended; should generate a warning).
			OMAttribute understandAttr = toElement.getAttribute(new QName("http://www.w3.org/2003/05/soap-envelope","mustUnderstand"));
			if (understandAttr == null)
				errorRecorder.record("XDR_MSG_422", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:FaultTo", MessageType.Info);
			else {
				if (!(understandAttr.getAttributeValue().equals("1") || understandAttr
						.getAttributeValue().equals("true")))
					errorRecorder.record("XDR_MSG_422", "Direct XDR Checklist",
							"S:Envelope/S:Header/wsa:FaultTo",
							MessageType.Warning);
			}

		}
	}

	private void validateRelatesTo(OMElement header) {
		Iterator toIter = header.getChildrenWithLocalName("RelatesTo");
		if (!toIter.hasNext())
			errorRecorder.record("XDR_MSG_423", "Direct XDR Checklist",
					"S:Envelope/S:Header/wsa:RelatesTo", MessageType.Info);
		else {
			OMElement toElement = (OMElement) toIter.next();

			OMAttribute roleAttr = toElement.getAttribute(new QName("http://www.w3.org/2003/05/soap-envelope","role"));
			if (roleAttr != null)
				errorRecorder.record("XDR_MSG_423_1", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:RelatesTo",
						MessageType.Warning);

			Iterator msgIdIter = header.getChildrenWithLocalName("MessageID");
			if (!msgIdIter.hasNext())
				errorRecorder.record("XDR_MSG_423_2", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:RelatesTo",
						MessageType.Warning);

			// Verify: @mustUnderstand is either "true", "1" "false" or "0".
			// SHOULD NOT be "false" or "0" (this is allowed but not
			// recommended; should generate a warning).
			OMAttribute understandAttr = toElement.getAttribute(new QName("http://www.w3.org/2003/05/soap-envelope","mustUnderstand"));
			if (understandAttr == null)
				errorRecorder.record("XDR_MSG_424", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:RelatesTo", MessageType.Error);
			else {
				if (!(understandAttr.getAttributeValue().equals("1") || understandAttr
						.getAttributeValue().equals("true")))
					errorRecorder.record("XDR_MSG_424", "Direct XDR Checklist",
							"S:Envelope/S:Header/wsa:RelatesTo",
							MessageType.Warning);
			}

		}
	}

	private void validateMessageID(OMElement header) {
		Iterator toIter = header.getChildrenWithLocalName("MessageID");
		if (!toIter.hasNext())
			errorRecorder.record("XDR_MSG_425", "Direct XDR Checklist",
					"S:Envelope/S:Header/wsa:MessageID", MessageType.Error);
		else {
			OMElement toElement = (OMElement) toIter.next();

			OMAttribute roleAttr = toElement.getAttribute(new QName("http://www.w3.org/2003/05/soap-envelope","role"));
			if (roleAttr != null)
				errorRecorder.record("XDR_MSG_425_1", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:MessageID",
						MessageType.Warning);

			// Verify: @mustUnderstand is either "true", "1" "false" or "0".
			// SHOULD NOT be "false" or "0" (this is allowed but not
			// recommended; should generate a warning).
			QName soapEnvMustQ = new QName("http://www.w3.org/2003/05/soap-envelope", "mustUnderstand", "soapenv");
			OMAttribute understandAttr = toElement.getAttribute(soapEnvMustQ);
			if (understandAttr == null)
				errorRecorder.record("XDR_MSG_426", "Direct XDR Checklist",
						"S:Envelope/S:Header/wsa:MessageID", MessageType.Error);
			else {
				if (!(understandAttr.getAttributeValue().equals("1") || understandAttr
						.getAttributeValue().equals("true")))
					errorRecorder.record("XDR_MSG_426_1", "Direct XDR Checklist",
							"S:Envelope/S:Header/wsa:MessageID",
							MessageType.Warning);
			}

		}
	}

	public String getName() {
		return "WSAddressingValidator";
	}

}
