package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;
import gov.onc.xdrtesttool.exception.XMLParserException;
import gov.onc.xdrtesttool.resource.MetadataType;
import gov.onc.xdrtesttool.xml.XMLParser;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.namespace.QName;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class XDSFolderValidator extends XDRValidator{
	MessageRecorder errorRecorder;
	String metadataType = MetadataType.instance.getMetadataType();
	public void validate(SoapMessage soapMsg, MessageRecorder errorRecorder,
			ValidationContext vContext) {
		this.errorRecorder = errorRecorder;
		OMElement element;
		try {
			element = XMLParser.parseXMLSource(soapMsg.getPayloadSource());
			if(element == null)
			{
				errorRecorder.record("XDR_MSG_410", "Direct XDR Checklist",
						"S:Envelope", MessageType.Error);
				return;
			}
			Iterator submitObjectsRequestIter = element
					.getChildrenWithLocalName("SubmitObjectsRequest");
			while (submitObjectsRequestIter.hasNext()) {
				OMElement sorElement = (OMElement) submitObjectsRequestIter
						.next();
				Iterator registryObjectListIter = sorElement
						.getChildrenWithLocalName("RegistryObjectList");
				while (registryObjectListIter.hasNext()) {
					OMElement rolElement = (OMElement) registryObjectListIter
							.next();
					validateRegistryPackage(rolElement);
				}
			}
		} catch (XMLParserException e) {
			errorRecorder.record("XDS_MSG_199", Constants.XDS_Metadata_Checklist,
					"XDSFolder.RegistryPackage", MetadataType.instance.getMessageType(metadataType, "199"));
			e.printStackTrace();
		}
	}

	private void validateRegistryPackage(OMElement registryObjectElement) {
		Iterator registryPackageIter = registryObjectElement
				.getChildrenWithLocalName("RegistryPackage");
		if (!registryPackageIter.hasNext()) {
			errorRecorder.record("XDS_MSG_199", Constants.XDS_Metadata_Checklist,
					"XDSFolder.RegistryPackage", MetadataType.instance.getMessageType(metadataType, "199"));
			return;
		}

		OMElement classificationElement = ValidationUtil
				.findClassificationChildElement(registryObjectElement,
						"urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2");
		while (registryPackageIter.hasNext()) {
			OMElement registryPackageElement = (OMElement) registryPackageIter
					.next();
			if (registryPackageIter.hasNext())
				errorRecorder.record("XDS_MSG_199_1", Constants.XDS_Metadata_Checklist,
						"XDSFolder.RegistryPackage", MessageType.Error);

			if (classificationElement == null) {
				classificationElement = ValidationUtil
						.findClassificationChildElement(registryPackageElement,
								"urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2");
			}

			OMAttribute idAttr = registryPackageElement.getAttribute(new QName(
					"id"));

			if (classificationElement == null)
				errorRecorder.record("XDS_MSG_199", Constants.XDS_Metadata_Checklist,
						"XDSFolder.RegistryPackage", MetadataType.instance.getMessageType(metadataType, "199"));
			else {
				OMAttribute classAttr = classificationElement
						.getAttribute(new QName("classifiedObject"));
				if (classAttr == null)
					errorRecorder.record("XDS_MSG_199",
							Constants.XDS_Metadata_Checklist,
							"XDSFolder.RegistryPackage", MetadataType.instance.getMessageType(metadataType, "199"));
				else {
					if (!classAttr.getAttributeValue().equals(
							idAttr.getAttributeValue()))
						errorRecorder.record("XDS_MSG_199",
								Constants.XDS_Metadata_Checklist,
								"XDSFolder.RegistryPackage", MetadataType.instance.getMessageType(metadataType, "199"));
				}
			}
			if (idAttr == null)
				errorRecorder.record("XDS_MSG_200", Constants.XDS_Metadata_Checklist,
						"XDSFolder.RegistryPackage.id", MetadataType.instance.getMessageType(metadataType, "200"));

			OMAttribute statusAttr = registryPackageElement
					.getAttribute(new QName("status"));
			if (statusAttr != null)
				errorRecorder.record("XDS_MSG_201", Constants.XDS_Metadata_Checklist,
						"XDSFolder.RegistryPackage.status", MetadataType.instance.getMessageType(metadataType, "201"));

			OMAttribute homeAttr = registryPackageElement
					.getAttribute(new QName("home"));
			if (homeAttr != null)
				errorRecorder.record("XDS_MSG_202", Constants.XDS_Metadata_Checklist,
						"XDSFolder.RegistryPackage.home", MetadataType.instance.getMessageType(metadataType, "202"));

			validateSlot(registryPackageElement);
			ValidationUtil.validateRegistryPackageName(registryPackageElement,
					errorRecorder, "XDS_MSG_204",
					"XDSFolder.RegistryPackage.Name");
			ValidationUtil.validateRegistryPackageDescription(
					registryPackageElement, errorRecorder, "XDS_MSG_205",
					"XDSSubmissionSet.RegistryPackage.Description");
			validateRegistryPackageClassification(registryPackageElement);
		}
	}

	private void validateRegistryPackageClassification(
			OMElement registryPackageElement) {
		OMElement classElement1 = ValidationUtil.findClassificationByScheme(
				registryPackageElement,
				"urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5");
		if (classElement1 == null)
			errorRecorder.record("XDR_MSG_206", Constants.XDS_Metadata_Checklist,
					"XDSSubmissionSet.Classification", MetadataType.instance.getMessageType(metadataType, "206"));
		else {
			verifyClassification1(registryPackageElement, classElement1);
		}

		OMElement classElement2 = ValidationUtil.findClassificationByScheme(
				registryPackageElement,
				"urn:uuid:2c144a76-29a9-4b7c-af54-b25409fe7d03");
		if (classElement2 == null)
			errorRecorder.record("XDR_MSG_207", Constants.XDS_Metadata_Checklist,
					"XDSSubmissionSet.Classification", MetadataType.instance.getMessageType(metadataType, "207"));
		else {
			ValidationUtil.validateClassifiedObject(registryPackageElement, classElement2, errorRecorder,
					"XDR_MSG_207", "Classification2");
		}
		ValidateExternalIdentifier1(registryPackageElement);
		ValidateExternalIdentifier2(registryPackageElement);
	}

	/*
	"Verify:
	- @classifiedObject = rim:RegistryPackage/@id of the parent XDSSubmissionSet
	- There MUST be a Slot element WHERE @name=""codingScheme"" AND rim:Slot/rim:ValueList/rim:Value is present
	- rim:Name/rim:LocalizedString/@value MUST be present
	- @nodeRepresentation is present
	- The values above may be constrained by the XDS Affinity Domain.
	
	Description: XDSFolder.codeList metadata attribute. The set of codes specifying the type of clinical activity that resulted in placing XDS Documents in this XDS Folder.
	
	Example:
	<rim:Classification id=""cl02""
	  classificationScheme=�urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5�
	  classifiedObject=""SS01""
	  nodeRepresentation=""some value"">
	  <rim:Slot name=""codingScheme"">
	    <rim:ValueList>
	      <rim:Value>some scheme</rim:Value>
	    </rim:ValueList>
	  </rim:Slot>
	  <rim:Name>
	    <rim:LocalizedString value=""some value description""/>
	  </rim:Name>
	</rim:Classification>"
	 */
	private void verifyClassification1(OMElement extrinsicObject,
			OMElement classElement2) {
		// @classifiedObject = the value of rim:ExtrinsicObject/@id
		ValidationUtil.validateClassifiedObject(extrinsicObject, classElement2, errorRecorder,
				"XDR_MSG_206_1", "Classification");

		//There MUST be a Slot element WHERE @name="codingScheme" AND rim:Slot/rim:ValueList/rim:Value is present
		ValidationUtil.validateCodingScheme(classElement2, "XDR_MSG_206_2", "Classification", errorRecorder);

		
		//rim:Name/rim:LocalizedString/@value MUST be present
		ValidationUtil.validateLocalizedString(classElement2, "XDR_MSG_206_3", "Classification", errorRecorder);

		// @nodeRepresentation is present
		OMAttribute attr = classElement2.getAttribute(new QName(
				"nodeRepresentation"));
		if (attr == null)
			errorRecorder.record("XDR_MSG_206_4", Constants.XDS_Metadata_Checklist,
					"XDSSubmissionSet.nodeRepresentation", MessageType.Error);
	}

/*
	"rim:ExternalIdentifier
	WHERE
	@identificationScheme= ""urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a"""

	 "Verify:
	- @value MUST be formatted in HL7 V2 CX type. This contains two parts formatted as ""ID^^^&amp;OIDofAA&amp;ISO""
	  - OIDofAA: Authority Domain Id
	  - ID: An Id in the above domain
	- The values above are not further verified in this context.
	
	Description: XDSFolder.patientId metadata attribute. Represents the medical record identifier of subject of care at the document recipient.
	
	Example:
	<rim:ExternalIdentifier
	  identificationScheme=
	    ""urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a""
	  value=""6578946^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO""
	  id=�ID_051""
	</rim:ExternalIdentifier>"
		
 */
	private void ValidateExternalIdentifier1(OMElement registryPackageElement)
	{
		OMElement element = ValidationUtil.findExternalIdentifierByScheme(registryPackageElement, "urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a");
		if(element == null)
			errorRecorder.record("XDR_MSG_208", Constants.XDS_Metadata_Checklist,
					"XDSSubmissionSet.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "208"));
		else
		{
			OMAttribute attr = element.getAttribute(new QName("value"));
			if(attr == null)
				errorRecorder.record("XDR_MSG_208", Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "208"));
			else
			{
				String valueStr = attr.getAttributeValue();
				int index1 = valueStr.indexOf("^^^&");
				int index2 = valueStr.indexOf("&ISO");
				if (index1 <= 0 || (index2 <= 0 || index2 <= index1))
					errorRecorder.record("XDR_MSG_208_1",
							Constants.XDS_Metadata_Checklist,
							"XDSSubmissionSet.ExternalIdentifier",
							MessageType.Error);
			}
		}
	}

	/*
	"rim:ExternalIdentifier
	WHERE
	@identificationScheme= ""urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a"""
	
	"Verify:
	- @value is OID with length <= 64
	Description: XDSFolder.uniqueId metadata attribute. The globally unique identifier assigned by the document creator to this document.
	Example: 1.3.6.1.4.1.21367.2005.3.7"
	 */
	private void ValidateExternalIdentifier2(OMElement registryPackageElement)
	{
		OMElement element = ValidationUtil.findExternalIdentifierByScheme(registryPackageElement, "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a");
		if(element == null)
			errorRecorder.record("XDR_MSG_209", Constants.XDS_Metadata_Checklist,
					"XDSDocumentEntry.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "209"));
		else
		{
			OMAttribute attr = element.getAttribute(new QName("value"));
			if(attr == null)
				errorRecorder.record("XDR_MSG_209", Constants.XDS_Metadata_Checklist,
						"XDSDocumentEntry.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "209"));
			else
			{
				if(attr.getAttributeValue().length() > 64)
					errorRecorder.record("XDR_MSG_209_1",
							Constants.XDS_Metadata_Checklist,
							"XDSDocumentEntry.ExternalIdentifier",
							MessageType.Error);
			}
		}
	}	
	
	private void validateSlot(OMElement registryPackageElement) {
		// Verify: rim:ValueList/rim:Value:
		// - MUST be a single value.
		// - MUST be in DTM format.
		// - MUST be a time in the past.
		OMElement submissionSlot = ValidationUtil.findSlot(
				registryPackageElement, "lastUpdateTime");
		if (!ValidationUtil.isSlotUnique(registryPackageElement,
				"lastUpdateTime"))
			errorRecorder.record("XDS_MSG_203_1", Constants.XDS_Metadata_Checklist,
					"XDSFolder.RegistryPackage.Slot.lastUpdateTime",
					MessageType.Error);
		else {
			if (submissionSlot == null) {
				errorRecorder.record("XDR_MSG_203", Constants.XDS_Metadata_Checklist,
						"XDSFolder.RegistryPackage.Slot.lastUpdateTime",
						MetadataType.instance.getMessageType(metadataType, "203"));
				return;
			}

			List<String> valueList = ValidationUtil
					.getSlotValueList(submissionSlot);
			if (valueList.size() == 0)
				errorRecorder.record("XDR_MSG_203_2", Constants.XDS_Metadata_Checklist,
						"XDSFolder.RegistryPackage.Slot.lastUpdateTime",
						MessageType.Error);

			String attrValue = valueList.get(0);
			Calendar currDtCal = Calendar.getInstance();
			Date currDt = currDtCal.getTime();

			if (!ValidationUtil.validateAndCompareUTCFormat(attrValue, currDt))
				errorRecorder.record("XDR_MSG_203_3", Constants.XDS_Metadata_Checklist,
						"XDSFolder.RegistryPackage.Slot.lastUpdateTime",
						MessageType.Error);
		}

	}

	@Override
	public String getName() {
		return "XDSFolderValidator";
	}
}
