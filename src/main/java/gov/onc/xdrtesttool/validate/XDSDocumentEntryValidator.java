package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;
import gov.onc.xdrtesttool.exception.XMLParserException;
import gov.onc.xdrtesttool.resource.MetadataType;
import gov.onc.xdrtesttool.xml.XMLParser;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.tika.mime.MimeType;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.namespace.QName;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

public class XDSDocumentEntryValidator extends XDRValidator {

	MessageRecorder errorRecorder;
	
	String metadataType = MetadataType.instance.getMetadataType();
	public void validate(SoapMessage soapMsg, MessageRecorder errorRecorder,
			ValidationContext vContext) {
		try {
			this.errorRecorder = errorRecorder;
			OMElement element = XMLParser.parseXMLSource(soapMsg
					.getPayloadSource());
			if(element == null)
			{
				errorRecorder.record("XDR_MSG_410", "Direct XDR Checklist",
						"S:Envelope", MessageType.Error);
				return;
			}
			
			Iterator submitObjectsRequestIter = element
					.getChildrenWithLocalName("SubmitObjectsRequest");
			while (submitObjectsRequestIter.hasNext()) {
				OMElement sorElement = (OMElement) submitObjectsRequestIter.next();
				Iterator registryObjectListIter = sorElement
						.getChildrenWithLocalName("RegistryObjectList");
				while (registryObjectListIter.hasNext()) {
					OMElement rolElement = (OMElement) registryObjectListIter.next();
					Iterator extrinsicObjectIter = rolElement
							.getChildrenWithLocalName("ExtrinsicObject");
					// Test ID: 148
					if (!extrinsicObjectIter.hasNext())
						errorRecorder.record("XDS_MSG_148", Constants.XDS_Metadata_Checklist,
								Constants.XDSDocumentEntry, MetadataType.instance.getMessageType(metadataType, "148"));
					else {
						while (extrinsicObjectIter.hasNext()) {
							OMElement extrinsicElement = (OMElement) extrinsicObjectIter.next();
							validateExtrinsicObject(extrinsicElement);
						}
					}
				}
			}
			validateMimePart(soapMsg);
		} catch (XMLParserException e) {
			e.printStackTrace();
			errorRecorder.record("XDR_MSG", Constants.XDS_Metadata_Checklist,
					"SOAP Message", MessageType.Error);
		}
	}

	private void validateExtrinsicObject(OMElement extrinsicElement)
	{
		//rim:ExtrinsicObject WHERE
		//@objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"
		OMAttribute objectTypeAttr = (OMAttribute) extrinsicElement
				.getAttribute(new QName("objectType"));
		if (objectTypeAttr == null)
			errorRecorder.record("XDS_MSG_148",
					Constants.XDS_Metadata_Checklist, Constants.XDSDocumentEntry,
					MetadataType.instance.getMessageType(metadataType, "148"));
		else {
			String attrValue = objectTypeAttr.getAttributeValue();
			if (attrValue == null
					|| !attrValue
							.equals("urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"))
				errorRecorder.record("XDS_MSG_148",
						Constants.XDS_Metadata_Checklist,
						Constants.XDSDocumentEntry, MetadataType.instance.getMessageType(metadataType, "148"));
		}
		// Test ID: 149
		OMAttribute idAttr = (OMAttribute) extrinsicElement
				.getAttribute(new QName("id"));
		if (idAttr == null)
			errorRecorder.record("XDS_MSG_149",
					Constants.XDS_Metadata_Checklist, Constants.XDSDocumentEntry,
					MetadataType.instance.getMessageType(metadataType, "149"));

		// Test ID: 150
		OMAttribute statusAttr = (OMAttribute) extrinsicElement
				.getAttribute(new QName("status"));
		if (statusAttr != null)
			errorRecorder.record("XDS_MSG_150",
					Constants.XDS_Metadata_Checklist, Constants.XDSDocumentEntry,
					MetadataType.instance.getMessageType(metadataType, "50"));

		// Test ID: 151
		OMAttribute homeAttr = (OMAttribute) extrinsicElement
				.getAttribute(new QName("home"));
		if (homeAttr != null)
			errorRecorder.record("XDS_MSG_151",
					Constants.XDS_Metadata_Checklist, Constants.XDSDocumentEntry,
					MetadataType.instance.getMessageType(metadataType, "151"));

		// Test ID: 152
		OMAttribute mimeAttr = (OMAttribute) extrinsicElement
				.getAttribute(new QName("mimeType"));
		if (mimeAttr == null)
			errorRecorder.record("XDS_MSG_152",
					Constants.XDS_Metadata_Checklist, Constants.XDSDocumentEntry,
					MetadataType.instance.getMessageType(metadataType, "152"));
		else {
			if (!MimeType.isValid(mimeAttr.getAttributeValue()))
				errorRecorder.record("XDS_MSG_152",
						Constants.XDS_Metadata_Checklist,
						Constants.XDSDocumentEntry, MetadataType.instance.getMessageType(metadataType, "152"));
		}
		new ExtrinsicObjectSlotValidator().validate(
				extrinsicElement, errorRecorder);
		validateName(extrinsicElement);
		validateDescription(extrinsicElement);
		//new ExtrinsicObjectClassificationValidator().validate(
		//		extrinsicElement, errorRecorder);
		
	}
	private void validateName(OMElement extrinsicElement) {
		Iterator nameIter = extrinsicElement.getChildrenWithLocalName("Name");
		if (nameIter.hasNext()) {
			OMElement nameElement = (OMElement) nameIter.next();
			Iterator localIter = nameElement
					.getChildrenWithLocalName("LocalizedString");
			if (localIter.hasNext()) {
				OMElement local = (OMElement) localIter.next();
				OMAttribute attribute = local.getAttribute(new QName("value"));
				String attrValue = attribute.getAttributeValue();
				if (attrValue == null)
					errorRecorder.record("XDS_MSG_164",
							Constants.XDS_Metadata_Checklist, "XDSDocumentEntry.Name",
							MetadataType.instance.getMessageType(metadataType, "164"));
				else {
					try {
						if (attrValue.getBytes("UTF-8").length > 128)
							errorRecorder.record("XDS_MSG_164",
									Constants.XDS_Metadata_Checklist,
									"XDSDocumentEntry.Name", MetadataType.instance.getMessageType(metadataType, "164"));
					} catch (UnsupportedEncodingException e) {
						errorRecorder.record("XDS_MSG_164",
								Constants.XDS_Metadata_Checklist,
								"XDSDocumentEntry.Name", MetadataType.instance.getMessageType(metadataType, "164"));
					}
				}
			} else {
				errorRecorder.record("XDS_MSG_164", Constants.XDS_Metadata_Checklist,
						"XDSDocumentEntry.Name", MetadataType.instance.getMessageType(metadataType, "164"));
			}
		} else {
			errorRecorder.record("XDS_MSG_164", Constants.XDS_Metadata_Checklist,
					"XDSDocumentEntry.Name", MetadataType.instance.getMessageType(metadataType, "164"));
		}
	}

	private void validateDescription(OMElement extrinsicElement) {
		Iterator nameIter = extrinsicElement
				.getChildrenWithLocalName("Description");
		if (nameIter.hasNext()) {
			OMElement nameElement = (OMElement) nameIter.next();
			Iterator localIter = nameElement
					.getChildrenWithLocalName("LocalizedString");
			if (localIter.hasNext()) {
				OMElement local = (OMElement) localIter.next();
				OMAttribute attribute = local.getAttribute(new QName("value"));
				String attrValue = attribute.getAttributeValue();
				if (attrValue == null)
					errorRecorder.record("XDS_MSG_165",
							Constants.XDS_Metadata_Checklist, "XDSDocumentEntry.Description",
							MetadataType.instance.getMessageType(metadataType, "165"));
			} else {
				errorRecorder.record("XDS_MSG_165", Constants.XDS_Metadata_Checklist,
						"XDSDocumentEntry.Description", MetadataType.instance.getMessageType(metadataType, "165"));
			}
		} else {
			errorRecorder.record("XDS_MSG_165", Constants.XDS_Metadata_Checklist,
					"XDSDocumentEntry.Description", MetadataType.instance.getMessageType(metadataType, "165"));
		}
	}

	private void validateMimePart(SoapMessage soapMsg)
	{
		
	}
	
	public String getName() {
		return "XDSDocumentEntryValidator";
	}

}
