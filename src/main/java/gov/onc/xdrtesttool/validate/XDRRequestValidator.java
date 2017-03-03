package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;
import gov.onc.xdrtesttool.exception.XMLParserException;
import gov.onc.xdrtesttool.xml.XMLParser;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.commons.codec.binary.Base64;
import org.springframework.ws.mime.Attachment;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.namespace.QName;
import java.util.Iterator;

public class XDRRequestValidator extends XDRValidator {

	private SoapMessage soapMsg;
	MessageRecorder errorRecorder;
	
	public String getName() {
		return "XDRRequestValidator";
	}

	@SuppressWarnings("rawtypes")
	public void validate(SoapMessage soapMsg, MessageRecorder errorRecorder,
			ValidationContext vContext) {
		this.soapMsg = soapMsg;
		this.errorRecorder = errorRecorder;
		try {
			validateSOAPHeader(soapMsg);

			OMElement element = XMLParser.parseXMLSource(soapMsg
					.getPayloadSource());
			if(element == null)
			{
				errorRecorder.record("XDR_MSG_410", "Direct XDR Checklist",
						"S:Envelope", MessageType.Error);
				return;
			}
			
			// Element Namespace is already verified
			//S:Envelope/S:Body/xdsb:ProvideAndRegisterDocumentSetRequest
			//Verify:
			//- Element is namespace qualified.
			//- Order of subelements is lcm:SubmitObjectsRequest, xdsb:Document."

			validateProvideAndRegisterDocumentSetRequest(element);

			// Test ID: 5
			//lcm:SubmitObjectsRequest
			//Description: The SubmitObjectsRequest allows one to submit a list of RegistryObject elements. Each RegistryEntry element provides metadata for a single submitted object.
			Iterator iterator = element
					.getChildrenWithLocalName("SubmitObjectsRequest");
			if (iterator.hasNext()) {
				while (iterator.hasNext()) {
					OMElement child = (OMElement) iterator.next();
					Iterator iter = child
							.getChildrenWithLocalName("RegistryRequestType");
					if (iter.hasNext()) {
						OMElement rrtElement = (OMElement) iter.next();
						Iterator attrs = rrtElement.getAllAttributes();
						if (attrs.hasNext()) {
							boolean id6 = false;
							boolean comment6 = false;
							while (attrs.hasNext()) {
								OMAttribute attr = (OMAttribute) attrs.next();
								if (attr.getLocalName().equals("id"))
									id6 = true;
								else if (attr.getLocalName().equals("comment"))
									comment6 = true;
							}
							if (!id6 || !comment6)
								errorRecorder
										.record("XDR_MSG_6",
												"XDR Message Checklist",
												"RegistryRequestType",
												MessageType.Info);
						} else
							errorRecorder.record("XDR_MSG_6",
									"XDR Message Checklist",
									"RegistryRequestType", MessageType.Info);

						// Test ID: 8
						//rs:RequestSlotList
						//Description: From rs:RegistryRequestType.  Every request may be extended using Slots. These aren't used by the Exchange specs, but are legal if included.
						iter = rrtElement
								.getChildrenWithLocalName("RequestSlotList");
						if (!iter.hasNext())
							errorRecorder.record("XDR_MSG_8",
									"XDR Message Checklist", "RequestSlotList",
									MessageType.Info);

					} else
						errorRecorder.record("XDR_MSG_6",
								"XDR Message Checklist", "RegistryRequestType",
								MessageType.Info);
					// Test Id: 9
					//rim:RegistryObjectList
					//"Verify: Using the XDS Metadata Checklist, verify all child elements in the RegistryObjectList. If there is no higher context, use context PRDS: Provide and Register Document Set-b.
					//Description: The collection of objects and relationships that are being submitted to the registry."
					iter = child.getChildrenWithLocalName("RegistryObjectList");
					if (!iter.hasNext())
						errorRecorder.record("XDR_MSG_9",
								"XDR Message Checklist", "RegistryObjectList",
								MessageType.Error);
				}
			} else
				errorRecorder.record("XDR_MSG_5", "XDR Message Checklist",
						"RegistryObjectList", MessageType.Error);

			// Test ID: 10
			validateDocument(element);

		} catch (XMLParserException e) {
			e.printStackTrace();
			errorRecorder.record("XDR_MSG", "XDR Message Checklist",
					"SOAP Message", MessageType.Error);
		}

	}

	//- If there is no higher context than XDR for this message, then wsa:Action MUST be urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b.
	//- @mustUnderstand is present and = "true" OR �1�
	//- There may be other instances of this element with different @role values, but this checklist does not address them.
	//- @mustUnderstand is present and = "true" OR �1�
	//- There may be other instances of this element with different @role values, but this checklist does not address them.
	private void validateSOAPHeader(SoapMessage soapMsg) {
		SoapHeader header = soapMsg.getEnvelope().getHeader();
		if(header == null)
		{
			errorRecorder.record("XDR_MSG_412", "Direct XDR Checklist",
					"S:Envelope", MessageType.Error);
			return;
		}
		Iterator<SoapHeaderElement> iter = header.examineAllHeaderElements();
		boolean found = false;
		if (iter.hasNext()) {
			QName soapEnvMustQ = new QName("http://www.w3.org/2003/05/soap-envelope", "mustUnderstand", "soapenv");
			while (iter.hasNext()) {
				SoapHeaderElement headerEle = iter.next();
				if (headerEle.getName().getLocalPart().equals("Action")) {
					found = true;
					String attrValue = headerEle.getAttributeValue(soapEnvMustQ);
					String eleValue = headerEle.getText();
					if (eleValue == null
							|| !eleValue
									.equals("urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"))
						errorRecorder.record("XDR_MSG_2",
								"XDR Message Checklist",
								"S:Envelope/S:Header/wsa:Action",
								MessageType.Error);
					if (attrValue == null
							|| !(attrValue.equals("1") || attrValue
									.equals("true")))
						errorRecorder.record("XDR_MSG_2",
								"XDR Message Checklist",
								"S:Envelope/S:Header/wsa:Action",
								MessageType.Error);
					String roleAttrValue = headerEle.getAttributeValue(new QName("role"));
					if(roleAttrValue != null)
						errorRecorder.record("XDR_MSG_3",
								"XDR Message Checklist",
								"S:Envelope/S:Header/wsa:Action.role",
								MessageType.Warning);
				}
			}
		}

		if (!found)
			errorRecorder.record("XDR_MSG_2", "XDR Message Checklist",
					"S:Envelope/S:Header/wsa:Action", MessageType.Error);
	}

	@SuppressWarnings("rawtypes")
	 //- Value is base64 encoded binary data, OR a child xop:Include element.
	 //- There is exactly one instance of xdsb:Document for each XDSDocumentEntry instance in the same XDSSubmissionSet (see the XDS Metadata Checklist, XDSDocumentEntry Test Focus).
	 //- @id is present AND is of type xs:anyURI AND matches the attribute rim:ExtrinsicObject/@id of the corresponding XDSDocumentEntry instance.
	 //- Hash calculated using the Secure Hash Algorithm (SHA-1) matches the corresponding XDSDocumentEntry.hash metadata attribute.
	 //- Size in bytes matches the corresponding XDSDocumentEntry.size metadata attribute.	
	private void validateDocument(OMElement element) {
		@SuppressWarnings("unchecked")
		Iterator<OMElement> docElements = element.getChildrenWithLocalName("Document");
		if (docElements.hasNext()) {
			while (docElements.hasNext()) {
				OMElement docElement =  docElements.next();
				String docValue = docElement.getText();
				if (docValue != null && docValue.trim().length() != 0) {
					if (!Base64.isBase64(docValue))
						errorRecorder.record("XDR_MSG_10_1",
								"XDR Message Checklist", "Document",
								MessageType.Error);
				} else {
					Iterator xopElements = docElement
							.getChildrenWithLocalName("Include");
					if (!xopElements.hasNext()) {
						errorRecorder.record("XDR_MSG_10_1",
								"XDR Message Checklist", "Document",
								MessageType.Error);
					} else {
						OMElement xopElement = (OMElement) xopElements.next();
						//TODO
						//verifyMimePart(xopElement, errorRecorder);
					}
				}
				OMAttribute attr = docElement.getAttribute(new QName("id"));
				if (attr == null)
					errorRecorder.record("XDR_MSG_10_3",
							"XDR Message Checklist", "Document",
							MessageType.Info);
				else {
					String idString = attr.getAttributeValue();
					if (idString != null && idString.trim().length() > 0) {
						if (!findDocumentId(element, idString))
							errorRecorder.record("XDR_MSG_10_3",
									"XDR Message Checklist", "Document",
									MessageType.Info);
					} else
						errorRecorder.record("XDR_MSG_10_3",
								"XDR Message Checklist", "Document",
								MessageType.Info);
				}
			}
		} else
			errorRecorder.record("XDR_MSG_10", "XDR Message Checklist",
					"Document", MessageType.Info);
	}

	private void verifyMimePart(OMElement xopElement,
			MessageRecorder errorRecorder) {

		String contentId = null;
		OMAttribute attr = xopElement.getAttribute(new QName("href"));
		if (attr != null) {
			contentId = attr.getAttributeValue();
			int index = contentId.indexOf("cid:");
			if (index != -1)
				contentId = contentId.substring(index + 1);
		} else
			errorRecorder.record("XDR_MSG_12", "XDR Message Checklist",
					"Mime Part", MessageType.Warning);

		if (contentId == null)
			errorRecorder.record("XDR_MSG_12", "XDR Message Checklist",
					"Mime Part", MessageType.Warning);
		else {
			Attachment attachment = soapMsg.getAttachment(contentId);
			if (attachment == null)
				errorRecorder.record("XDR_MSG_12", "XDR Message Checklist",
						"Mime Part", MessageType.Warning);
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean findDocumentId(OMElement element, String docId) {
		// Test ID: 10_3
		boolean match = false;
		Iterator sorIter = element
				.getChildrenWithLocalName("SubmitObjectsRequest");
		while (sorIter.hasNext()) {
			OMElement sorElement = (OMElement) sorIter.next();
			Iterator rolIter = sorElement
					.getChildrenWithLocalName("RegistryObjectList");
			if (rolIter.hasNext()) {
				OMElement rolElement = (OMElement) rolIter.next();
				Iterator eoIter = rolElement
						.getChildrenWithLocalName("ExtrinsicObject");
				if (eoIter.hasNext()) {
					OMElement eoElement = (OMElement) eoIter.next();
					OMAttribute eoAttr = eoElement
							.getAttribute(new QName("id"));
					String idVal = eoAttr.getAttributeValue();
					if (idVal != null && idVal.equals(docId)) {
						match = true;
						break;
					}
				}
			}
		}
		return match;
	}

	@SuppressWarnings("rawtypes")
	//S:Envelope/S:Body/xdsb:ProvideAndRegisterDocumentSetRequest
	//- Element is namespace qualified.
	//- Order of subelements is lcm:SubmitObjectsRequest, xdsb:Document.
	private void validateProvideAndRegisterDocumentSetRequest(OMElement element) {
		Iterator iterator = element.getChildElements();
		if (!iterator.hasNext())
			errorRecorder.record("XDR_MSG_4", "XDR Message Checklist",
					"Request Body", MessageType.Error);
		boolean first = false;
		boolean second = false;
		while (iterator.hasNext()) {
			OMElement child = (OMElement) iterator.next();
			if (second && child.getLocalName().equals("SubmitObjectsRequest"))
				errorRecorder.record("XDR_MSG_4", "XDR Message Checklist",
						"SubmitObjectsRequest", MessageType.Error);
			else if (child.getLocalName().equals("SubmitObjectsRequest"))
				first = true;

			if (child.getLocalName().equals("Document") && !first)
				errorRecorder.record("XDR_MSG_4", "XDR Message Checklist",
						"Document", MessageType.Error);
			else if (child.getLocalName().equals("Document"))
				second = true;
		}
	}

}
