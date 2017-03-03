package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;
import gov.onc.xdrtesttool.resource.MetadataType;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;
import java.util.Iterator;
import java.util.List;

public class XDSSubmissionSetClassificationValidator {
	MessageRecorder errorRecorder;
	String metadataType = MetadataType.instance.getMetadataType();
	public String getName() {
		return "EXtrinsicObjectClassificationValidator";
	}

	public void validate(OMElement registryPackageElement,
			MessageRecorder errorRecorder) {
		this.errorRecorder = errorRecorder;
		validateRegistryPackageClassification(registryPackageElement);
	}

	private void validateRegistryPackageClassification(OMElement registryPackageElement)
	{
		 validateClassification1(registryPackageElement);
			OMElement classElement2 = ValidationUtil.findClassificationByScheme(registryPackageElement, "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500");
			if(classElement2 ==  null)
				errorRecorder.record("XDR_MSG_194", Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.Classification2", MetadataType.instance.getMessageType(metadataType, "194"));
			else
			{
				verifyClassification2(registryPackageElement, classElement2);
			}
			OMElement classElement3 = ValidationUtil.findClassificationByNode(registryPackageElement, "urn:uuid:5003a9db-8d8d-49e6-bf0c-990e34ac7707");
			if(classElement3 ==  null)
				errorRecorder.record("XDR_MSG_195", Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.Classification3", MetadataType.instance.getMessageType(metadataType, "195"));
			else
			{
				ValidationUtil.validateClassifiedObject(registryPackageElement, classElement3, errorRecorder, "XDR_MSG_195_1", "Classification3");
			}
			ValidateExternalIdentifier1(registryPackageElement);
			ValidateExternalIdentifier2(registryPackageElement);
	}
	
	private void ValidateExternalIdentifier1(OMElement registryPackageElement)
	{
		OMElement element = ValidationUtil.findExternalIdentifierByScheme(registryPackageElement, "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446");
		if(element == null)
			errorRecorder.record("XDR_MSG_196", Constants.XDS_Metadata_Checklist,
					"XDSSubmissionSet.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "196"));
		else
		{
			OMAttribute attr = element.getAttribute(new QName("value"));
			if(attr == null)
				errorRecorder.record("XDR_MSG_196", Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "196"));
			else
			{
				String valueStr = attr.getAttributeValue();
				int index1 = valueStr.indexOf("^^^&");
				int index2 = valueStr.indexOf("&ISO");
				if (index1 <= 0 || (index2 <= 0 || index2 <= index1))
					errorRecorder.record("XDR_MSG_196_1",
							Constants.XDS_Metadata_Checklist,
							"XDSSubmissionSet.ExternalIdentifier",
							MessageType.Error);
			}
		}
	}

	private void ValidateExternalIdentifier2(OMElement registryPackageElement)
	{
		OMElement element = ValidationUtil.findExternalIdentifierByScheme(registryPackageElement, "urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832");
		if(element == null)
			errorRecorder.record("XDR_MSG_197", Constants.XDS_Metadata_Checklist,
					"XDSDocumentEntry.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "197"));
		else
		{
			OMAttribute attr = element.getAttribute(new QName("value"));
			if(attr == null)
				errorRecorder.record("XDR_MSG_197", Constants.XDS_Metadata_Checklist,
						"XDSDocumentEntry.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "197"));
			else
			{
				if(attr.getAttributeValue().length() > 64)
					errorRecorder.record("XDR_MSG_197_1",
							Constants.XDS_Metadata_Checklist,
							"XDSDocumentEntry.ExternalIdentifier",
							MessageType.Error);
			}
		}
	}
	
	private void ValidateExternalIdentifier3(OMElement registryPackageElement)
	{
		OMElement element = ValidationUtil.findExternalIdentifierByScheme(registryPackageElement, "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8");
		if(element == null)
			errorRecorder.record("XDR_MSG_198", Constants.XDS_Metadata_Checklist,
					"XDSDocumentEntry.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "198"));
		else
		{
			OMAttribute attr = element.getAttribute(new QName("value"));
			if(attr == null)
				errorRecorder.record("XDR_MSG_198", Constants.XDS_Metadata_Checklist,
						"XDSDocumentEntry.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "198"));
			else
			{
				if(attr.getAttributeValue().length() > 64)
					errorRecorder.record("XDR_MSG_198_1",
							Constants.XDS_Metadata_Checklist,
							"XDSDocumentEntry.ExternalIdentifier",
							MessageType.Error);
			}
		}
	}
	
	private void validateClassification1(OMElement registryPackageElement)
	{
		OMElement classElement = ValidationUtil.findClassificationByScheme(registryPackageElement, "urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d");
		if(classElement ==  null)
			errorRecorder.record("XDR_MSG_189_1", Constants.XDS_Metadata_Checklist,
					"XDSSubmissionSet.Classification", MessageType.Warning);
		else
		{
			verifyAuthor(registryPackageElement);
		}
		
	}
	
	private void verifyClassification2(OMElement extrinsicObject, OMElement classElement2)
	{
		//@classifiedObject = the value of rim:ExtrinsicObject/@id
		ValidationUtil.validateClassifiedObject(extrinsicObject, classElement2, errorRecorder, "XDR_MSG_194_1", "Classification2");

		//There MUST be a Slot element WHERE @name="codingScheme" AND rim:Slot/rim:ValueList/rim:Value is present
		ValidationUtil.validateCodingScheme(classElement2, "XDR_MSG_194_2", "Classification2", errorRecorder);

		
		//rim:Name/rim:LocalizedString/@value MUST be present
		ValidationUtil.validateLocalizedString(classElement2, "XDR_MSG_194_3", "Classification2", errorRecorder);
		
		//@nodeRepresentation is present
		//@nodeRepresentation value SHOULD come from the value set in HITSP C80 2.0: Table 2-144, column "Concept Code"
		//- rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept Code in HITSP C80 2.0: Table 2-144, columns "Concept Name" and "Description"
		OMAttribute attr = classElement2.getAttribute(new QName("nodeRepresentation"));
		if(attr == null)
			errorRecorder.record("XDR_MSG_194_4", Constants.XDS_Metadata_Checklist,
					"XDSSubmissionSet.nodeRepresentation", MessageType.Error);
		else
		{
			if(!ValidationUtil.isValidConceptCode(attr.getAttributeValue()))
				errorRecorder.record("XDR_MSG_194_4", Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.Classification2.nodeRepresentation", MessageType.Warning);				
		}
	}	
	
	
	
	
	private void verifyAuthor(OMElement classElement)
	{
		OMElement authorInstSlot = ValidationUtil.findSlot(classElement, "authorInstitution");
		if(authorInstSlot == null)
			errorRecorder.record("XDR_MSG_190", Constants.XDS_Metadata_Checklist,
					"XDSSubmissionSet.Classification.authorInstitution", MetadataType.instance.getMessageType(metadataType, "190"));
		else
		{
			List<String> values = ValidationUtil.getSlotValueList(authorInstSlot);
			if(values == null || values.size() == 0)
				errorRecorder.record("XDR_MSG_190_1", Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.Classification.authorInstitution", MessageType.Error);
		}

		OMElement authorPersonSlot = ValidationUtil.findSlot(classElement, "authorPerson");
		if(authorPersonSlot == null)
			errorRecorder.record("XDR_MSG_191", Constants.XDS_Metadata_Checklist,
					"XDSSubmissionSet.Classification.authorPerson", MetadataType.instance.getMessageType(metadataType, "191"));
		else
		{
			List<String> values = ValidationUtil.getSlotValueList(authorPersonSlot);
			if(values == null || values.size() == 0)
				errorRecorder.record("XDR_MSG_191_1", Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.Classification.authorPerson", MessageType.Error);
		}
		
		OMElement authorRoleSlot = ValidationUtil.findSlot(classElement, "authorRole");
		if(authorRoleSlot == null)
			errorRecorder.record("XDR_MSG_192", Constants.XDS_Metadata_Checklist,
					"XDSSubmissionSet.Classification.authorRole", MetadataType.instance.getMessageType(metadataType, "192"));
		else
		{
			List<String> values = ValidationUtil.getSlotValueList(authorRoleSlot);
			if(values == null || values.size() == 0)
				errorRecorder.record("XDR_MSG_192_1", Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.Classification.authorRole", MessageType.Error);
		}

		OMElement authorSpecialtySlot = ValidationUtil.findSlot(classElement, "authorSpecialty");
		if(authorSpecialtySlot == null)
			errorRecorder.record("XDR_MSG_193", Constants.XDS_Metadata_Checklist,
					"XDSSubmissionSet.Classification.authorSpecialty", MetadataType.instance.getMessageType(metadataType, "193"));
		else
		{
			List<String> values = ValidationUtil.getSlotValueList(authorSpecialtySlot);
			if(values == null || values.size() == 0)
				errorRecorder.record("XDR_MSG_193_1", Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.Classification.authorSpecialty", MessageType.Error);
		}
	}
	
	private void validateXTN(OMElement classElement)
	{
		//rim:Slot WHERE @name="authorTelecommunication"
		//"Verify:
		//- Value is in the HL7 V2 XTN format: ""^^Internet^"" direct-address
		//- As with any HL7 datatype, the XTN value may have trailing delimiters (""^"" characters).
		//- The value of the email address MUST = the value in the RFC 5322 ""from"" header.
		//Description: XDSSubmissionSet.authorTelecommunication metadata attribute.
		//Example: ^^Internet^drwel@direct.example.org"
		OMElement slot = ValidationUtil.findSlot(classElement, "authorTelecommunication");
		if(slot == null)
			errorRecorder.record("XDR_MSG_433", "XDSSubmissionSet",
					"XDSSubmissionSet", MessageType.Error);			
		else
		{
			List valueList = ValidationUtil.getSlotValueList(slot);
			if(valueList == null || valueList.size() == 0)
				errorRecorder.record("XDR_MSG_433", "XDSSubmissionSet",
						"XDSSubmissionSet", MessageType.Error);			
			else
			{
				Iterator valueIter = valueList.iterator();
				while(valueIter.hasNext())
				{
					String value = (String) valueIter.next();
					if(!ValidationUtil.isValidXTN(value))
						errorRecorder.record("XDR_MSG_433", "XDSSubmissionSet",
								"XDSSubmissionSet", MessageType.Error);			
						
				}
			}
				
		}
	}
	
}
