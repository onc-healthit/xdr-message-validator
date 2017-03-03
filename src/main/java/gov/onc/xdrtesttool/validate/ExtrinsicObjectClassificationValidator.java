package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;
import gov.onc.xdrtesttool.resource.MetadataType;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;
import java.util.Iterator;
import java.util.List;

public class ExtrinsicObjectClassificationValidator {
	MessageRecorder errorRecorder;
	String metadataType = MetadataType.instance.getMetadataType();
	public String getName() {
		return "EXtrinsicObjectClassificationValidator";
	}

	public void validate(OMElement extrinsicObject,
			MessageRecorder errorRecorder) {
		this.errorRecorder = errorRecorder;
		validateExtrinsicObjectClassification(extrinsicObject);
	}

	private void validateExtrinsicObjectClassification(OMElement extrinsicObject)
	{
		OMElement classElement1 = ValidationUtil.findClassificationByScheme(extrinsicObject, "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d");
		if(classElement1 ==  null)
			errorRecorder.record("XDR_MSG_166", "XDS Metadata Checklist",
					"XDSDocumentEntry.Classification1", MetadataType.instance.getMessageType(metadataType, "166"));
		else
		{
			verifyAuthor(classElement1);
		}

		OMElement classElement2 = ValidationUtil.findClassificationByScheme(extrinsicObject, "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a");
		if(classElement2 ==  null)
			errorRecorder.record("XDR_MSG_171", "XDS Metadata Checklist",
					"XDSDocumentEntry.Classification2", MetadataType.instance.getMessageType(metadataType, "171"));
		else
		{
			verifyClassification2(extrinsicObject, classElement2);
		}

		OMElement classElement3 = ValidationUtil.findClassificationByScheme(extrinsicObject, "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f");
		if(classElement3 ==  null)
			errorRecorder.record("XDR_MSG_172", "XDS Metadata Checklist",
					"XDSDocumentEntry.Classification3", MetadataType.instance.getMessageType(metadataType, "172"));
		else
		{
			verifyClassification3(extrinsicObject, classElement3);
		}

		OMElement classElement4 = ValidationUtil.findClassificationByScheme(extrinsicObject, "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f");
		if(classElement4 ==  null)
			errorRecorder.record("XDR_MSG_173", "XDS Metadata Checklist",
					"XDSDocumentEntry.Classification4", MetadataType.instance.getMessageType(metadataType, "173"));
		else
		{
			verifyClassification4(extrinsicObject, classElement4);
		}
		
		OMElement classElement5 = ValidationUtil.findClassificationByScheme(extrinsicObject, "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d");
		if(classElement5 ==  null)
			errorRecorder.record("XDR_MSG_174", "XDS Metadata Checklist",
					"XDSDocumentEntry.Classification5", MetadataType.instance.getMessageType(metadataType, "174"));
		else
		{
			verifyClassification5(extrinsicObject, classElement5);
		}

		OMElement classElement6 = ValidationUtil.findClassificationByScheme(extrinsicObject, "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1");
		if(classElement6 ==  null)
			errorRecorder.record("XDR_MSG_175", "XDS Metadata Checklist",
					"XDSDocumentEntry.Classification6", MetadataType.instance.getMessageType(metadataType, "175"));
		else
		{
			verifyClassification6(extrinsicObject, classElement6);
		}

		OMElement classElement7 = ValidationUtil.findClassificationByScheme(extrinsicObject, "urn:uuid:ab9b591b-83ab-4d03-8f5d-f93b1fb92e85");
		if(classElement7 ==  null)
			errorRecorder.record("XDR_MSG_176", "XDS Metadata Checklist",
					"XDSDocumentEntry.Classification7", MetadataType.instance.getMessageType(metadataType, "176"));
		else
		{
			validateClassifiedObject(extrinsicObject, classElement7, "XDR_MSG_176", "Classification7");
		}

		OMElement classElement8 = ValidationUtil.findClassificationByScheme(extrinsicObject, "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead");
		if(classElement8 ==  null)
			errorRecorder.record("XDR_MSG_177", "XDS Metadata Checklist",
					"XDSDocumentEntry.Classification8", MetadataType.instance.getMessageType(metadataType, "177"));
		else
		{
			verifyClassification8(extrinsicObject, classElement8);
		}

		OMElement classElement9 = ValidationUtil.findClassificationByScheme(extrinsicObject, "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983");
		if(classElement9 ==  null)
			errorRecorder.record("XDR_MSG_178", "XDS Metadata Checklist",
					"XDSDocumentEntry.Classification9", MetadataType.instance.getMessageType(metadataType, "178"));
		else
		{
			verifyClassification9(extrinsicObject, classElement9);
		}
		
		ValidateExternalIdentifier1(extrinsicObject);
		ValidateExternalIdentifier2(extrinsicObject);
	}
	
	private void ValidateExternalIdentifier1(OMElement extrinsicObject)
	{
		OMElement element = ValidationUtil.findExternalIdentifierByScheme(extrinsicObject, "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427");
		if(element == null)
			errorRecorder.record("XDR_MSG_179", "XDS Metadata Checklist",
					"XDSDocumentEntry.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "179"));
		else
		{
			OMAttribute attr = element.getAttribute(new QName("value"));
			if(attr == null)
				errorRecorder.record("XDR_MSG_179", "XDS Metadata Checklist",
						"XDSDocumentEntry.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "179"));
			else
			{
				String valueStr = attr.getAttributeValue();
				int index1 = valueStr.indexOf("^^^&");
				int index2 = valueStr.indexOf("&ISO");
				if (index1 <= 0 || (index2 <= 0 || index2 <= index1))
					errorRecorder.record("XDR_MSG_179_1",
							"XDS Metadata Checklist",
							"XDSDocumentEntry.ExternalIdentifier",
							MessageType.Error);
			}
		}
	}

	private void ValidateExternalIdentifier2(OMElement extrinsicObject)
	{
		OMElement element = ValidationUtil.findExternalIdentifierByScheme(extrinsicObject, "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");
		if(element == null)
			errorRecorder.record("XDR_MSG_180", "XDS Metadata Checklist",
					"XDSDocumentEntry.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "180"));
		else
		{
			OMAttribute attr = element.getAttribute(new QName("value"));
			if(attr == null)
				errorRecorder.record("XDR_MSG_180", "XDS Metadata Checklist",
						"XDSDocumentEntry.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "180"));
			else
			{
				if(attr.getAttributeValue().length() > 128)
					errorRecorder.record("XDR_MSG_180_1",
							"XDS Metadata Checklist",
							"XDSDocumentEntry.ExternalIdentifier",
							MessageType.Error);
			}
		}
	}
	
/*
 "rim:Classification
WHERE
@classificationScheme= ""urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a"""

"Verify:
- @classifiedObject = the value of rim:ExtrinsicObject/@id
- There MUST be a Slot element WHERE @name=""codingScheme"" AND rim:Slot/rim:ValueList/rim:Value is present
- rim:Name/rim:LocalizedString/@value MUST be present
- @nodeRepresentation is present
- In the contexts of PRDS and PRDSML - Provide and Register Document Set, and DDSM - Distribute Document Set on Media:
  - @nodeRepresentation value SHOULD come from the value set in HITSP C80 2.0: Table 2-144, column ""Concept Code""
  - rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept Code in HITSP C80 2.0: Table 2-144, columns ""Concept Name"" and ""Description""
  - The ""codingScheme"" Slot rim:Slot/rim:ValueList/rim:Value SHOULD be 2.16.840.1.113883.6.1

Description: XDSDocumentEntry.classCode metadata attribute.

Example:
<rim:Classification id=""cl02""
  classificationScheme=""urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a""
  classifiedObject=""Document01""
  nodeRepresentation=""34133-9"">
  <rim:Slot name=""codingScheme"">
    <rim:ValueList>
      <rim:Value>2.16.840.1.113883.6.1</rim:Value>
    </rim:ValueList>
  </rim:Slot>
  <rim:Name>
    <rim:LocalizedString value=""Summarization of episode note""/>
  </rim:Name>
</rim:Classification>"
	
 */
	private void verifyClassification2(OMElement extrinsicObject, OMElement classElement2)
	{
		//@classifiedObject = the value of rim:ExtrinsicObject/@id
		validateClassifiedObject(extrinsicObject, classElement2, "XDR_MSG_171_1", "Classification2");

		//There MUST be a Slot element WHERE @name="codingScheme" AND rim:Slot/rim:ValueList/rim:Value is present
		validateCodingScheme(classElement2, "XDR_MSG_171_2", "Classification2");

		
		//rim:Name/rim:LocalizedString/@value MUST be present
		validateLocalizedString(classElement2, "XDR_MSG_171_3", "Classification2");
		
		//@nodeRepresentation is present
		//@nodeRepresentation value SHOULD come from the value set in HITSP C80 2.0: Table 2-144, column "Concept Code"
		//- rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept Code in HITSP C80 2.0: Table 2-144, columns "Concept Name" and "Description"
		OMAttribute attr = classElement2.getAttribute(new QName("nodeRepresentation"));
		if(attr == null)
			errorRecorder.record("XDR_MSG_171_4", "XDS Metadata Checklist",
					"XDSDocumentEntry.Classification2.nodeRepresentation", MessageType.Error);
		else
		{
			if(!ValidationUtil.isValidHITSP_C80_20_Table2_144conceptCode(attr.getAttributeValue()))
				errorRecorder.record("XDR_MSG_171_4", "XDS Metadata Checklist",
						"XDSDocumentEntry.Classification2.nodeRepresentation", MessageType.Warning);				
		}
		//The "codingScheme" Slot rim:Slot/rim:ValueList/rim:Value SHOULD be 2.16.840.1.113883.6.1
		validateCodingSchemeValue(classElement2, "XDR_MSG_171_5", "Classification1", "2.16.840.1.113883.6.1");
	}

	
	private void verifyClassification3(OMElement extrinsicObject, OMElement classElement3)
	{
		//@classifiedObject = the value of rim:ExtrinsicObject/@id
		validateClassifiedObject(extrinsicObject, classElement3, "XDR_MSG_172_1", "Classification3");

		//There MUST be a Slot element WHERE @name="codingScheme" AND rim:Slot/rim:ValueList/rim:Value is present
		validateCodingScheme(classElement3, "XDR_MSG_172_2", "Classification3");
		
		//rim:Name/rim:LocalizedString/@value MUST be present
		validateLocalizedString(classElement3, "XDR_MSG_172_3", "Classification3");
		
		//@nodeRepresentation is present
		//@nodeRepresentation value SHOULD come from the value set in HITSP C80 2.0: Table 2-144, column "Concept Code"
		//- rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept Code in HITSP C80 2.0: Table 2-144, columns "Concept Name" and "Description"
		validateNodeRepresentationWithTable151(classElement3, "XDR_MSG_172_4", "Classification3");

		//The "codingScheme" Slot rim:Slot/rim:ValueList/rim:Value SHOULD be 2.16.840.1.113883.5.25
		validateCodingSchemeValue(classElement3, "XDR_MSG_172_5", "Classification3", "2.16.840.1.113883.5.25");
	}

	/*
	"rim:Classification
	WHERE
	@classificationScheme= ""urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4"""

	"Verify:
	- @classifiedObject = the value of rim:ExtrinsicObject/@id
	- There MUST be a Slot element WHERE @name=""codingScheme"" AND rim:Slot/rim:ValueList/rim:Value is present
	- rim:Name/rim:LocalizedString/@value MUST be present
	- @nodeRepresentation is present
	- The values above are not constrained, even though Table 4.2-5 states it ""shall not conflict with the corresponding values inherent in the classCode, practiceSettingCode or typeCode of this XDSDocumentEntry, as such a conflict would create an ambiguous situation."", but the Spec Factory has decided not to constrain it at this level.  See linked implementation guidance.
	
	Description: XDSDocumentEntry.eventCodeList metadata attribute.
	
	Example:
	<rim:Classification id=""cl02""
	  classificationScheme=""urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4""
	  classifiedObject=""Document01""
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
	private void verifyClassification4(OMElement extrinsicObject, OMElement classElement4)
	{
		//@classifiedObject = the value of rim:ExtrinsicObject/@id
		validateClassifiedObject(extrinsicObject, classElement4, "XDR_MSG_173_1", "Classification4");

		//There MUST be a Slot element WHERE @name="codingScheme" AND rim:Slot/rim:ValueList/rim:Value is present
		validateCodingScheme(classElement4, "XDR_MSG_173_2", "Classification4");
		
		//rim:Name/rim:LocalizedString/@value MUST be present
		validateLocalizedString(classElement4, "XDR_MSG_173_3", "Classification4");

		//@nodeRepresentation is present
		//The values above are not constrained, even though Table 4.2-5 states it "shall not conflict with the corresponding values inherent in the classCode, practiceSettingCode or typeCode of this XDSDocumentEntry, as such a conflict would create an ambiguous situation.", but the Spec Factory has decided not to constrain it at this level.  See linked implementation guidance.		
		OMAttribute attr = classElement4.getAttribute(new QName("nodeRepresentation"));
		if(attr == null)
			errorRecorder.record("XDR_MSG_173_4", "XDS Metadata Checklist",
					"XDSDocumentEntry.Classification3.nodeRepresentation", MessageType.Error);
	}
	/*
	"rim:Classification
	WHERE
	@classificationScheme= ""urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d"""
	 
	"Verify: 
	- @classifiedObject = the value of rim:ExtrinsicObject/@id
	- There MUST be a Slot element WHERE @name=""codingScheme"" AND rim:Slot/rim:ValueList/rim:Value is present
	- rim:Name/rim:LocalizedString/@value MUST be present
	- @nodeRepresentation is present
	- In the contexts of PRDS and PRDSML - Provide and Register Document Set, and DDSM - Distribute Document Set on Media:
	  - @nodeRepresentation value SHOULD come from the value set in HITSP C80 2.0: Table 2-153, column ""Concept Code""
	  - rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept Code in HITSP C80 2.0: Table 2-153, column ""Concept Name""
	  - The ""codingScheme"" Slot rim:Slot/rim:ValueList/rim:Value SHOULD be 1.3.6.1.4.1.19376.1.2.3 (except for concept code urn:nhin:names:acp:XACML)
	
	Description: XDSDocumentEntry.formatCode metadata attribute.
	
	Example: 
	<rim:Classification id=""cl02""
	  classificationScheme=""urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d""
	  classifiedObject=""Document01""
	  nodeRepresentation=""urn:ihe:pcc:xphr:2007"">
	  <rim:Slot name=""codingScheme"">
	    <rim:ValueList>
	      <rim:Value>1.3.6.1.4.1.19376.1.2.3</rim:Value>
	    </rim:ValueList>
	  </rim:Slot>
	  <rim:Name>
	    <rim:LocalizedString value=""HL7 CCD Document""/>
	  </rim:Name>
	</rim:Classification>"
	 
	 */
	
	private void verifyClassification5(OMElement extrinsicObject, OMElement classElement5)
	{
		//@classifiedObject = the value of rim:ExtrinsicObject/@id
		validateClassifiedObject(extrinsicObject, classElement5, "XDR_MSG_174_1", "Classification5");
		
		//There MUST be a Slot element WHERE @name="codingScheme" AND rim:Slot/rim:ValueList/rim:Value is present
		validateCodingScheme(classElement5, "XDR_MSG_174_2", "Classification5");
		
		//rim:Name/rim:LocalizedString/@value MUST be present
		validateLocalizedString(classElement5, "XDR_MSG_174_3", "Classification5");
		
		
		//@nodeRepresentation is present
		//@nodeRepresentation value SHOULD come from the value set in HITSP C80 2.0: Table 2-144, column "Concept Code"
		//- rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept Code in HITSP C80 2.0: Table 2-144, columns "Concept Name" and "Description"
		validateNodeRepresentationWithTable153(classElement5, "XDR_MSG_174_4", "Classification5");

		//The "codingScheme" Slot rim:Slot/rim:ValueList/rim:Value SHOULD be 1.3.6.1.4.1.19376.1.2.3 
		//(except for concept code urn:nhin:names:acp:XACML)
		validateCodingSchemeValue(classElement5, "XDR_MSG_174_5", "Classification5", "1.3.6.1.4.1.19376.1.2.3");
	}

	/*
	"rim:Classification
	WHERE
	@classificationScheme= ""urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1"""

	"Verify: 
	- @classifiedObject = the value of rim:ExtrinsicObject/@id
	- There MUST be a Slot element WHERE @name=""codingScheme"" AND rim:Slot/rim:ValueList/rim:Value is present
	- rim:Name/rim:LocalizedString/@value MUST be present
	- @nodeRepresentation is present
	- @nodeRepresentation MUST NOT conflict with the value inherent in the typeCode, as such a conflict would create an ambiguous situation (may not be verifiable)
	- In the contexts of PRDS and PRDSML - Provide and Register Document Set, and DDSM - Distribute Document Set on Media:
	  - @nodeRepresentation value SHOULD come from the value set in HITSP C80 2.0: Table 2-147, column ""Concept Code""
	  - rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept Code in HITSP C80 2.0: Table 2-147, column ""Concept Name""
	  - The ""codingScheme"" Slot rim:Slot/rim:ValueList/rim:Value SHOULD be 2.16.840.1.113883.6.96

	Description: XDSDocumentEntry.healthcareFacilityTypeCode metadata attribute.

	Example:
	<rim:Classification id=""cl02""
	  classificationScheme=""urn:uuid:f33fb8ac-18af-42cc-ae0eed0b0bdb91e1""
	  classifiedObject=""Document01""
	  nodeRepresentation=""73770003"">
	  <rim:Slot name=""codingScheme"">
	    <rim:ValueList>
	      <rim:Value>2.16.840.1.113883.6.96</rim:Value>
	    </rim:ValueList>
	  </rim:Slot>
	  <rim:Name>
	    <rim:LocalizedString value=""Emergency department--hospital""/>
	  </rim:Name>
	</rim:Classification>"
	*/
	private void verifyClassification6(OMElement extrinsicObject, OMElement classElement6)
	{
		//@classifiedObject = the value of rim:ExtrinsicObject/@id
		validateClassifiedObject(extrinsicObject, classElement6, "XDR_MSG_175_1", "Classification6");
		
		//There MUST be a Slot element WHERE @name="codingScheme" AND rim:Slot/rim:ValueList/rim:Value is present
		validateCodingScheme(classElement6, "XDR_MSG_175_2", "Classification6");
		
		//rim:Name/rim:LocalizedString/@value MUST be present
		//- rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept Code in HITSP C80 2.0: Table 2-144, columns "Concept Name" and "Description"
		validateLocalizedString(classElement6, "XDR_MSG_175_3", "Classification6");
		
		
		//@nodeRepresentation is present
		//nodeRepresentation value SHOULD come from the value set in HITSP C80 2.0: Table 2-147, column "Concept Code"
		validateNodeRepresentationWithTable147(classElement6, "XDR_MSG_175_4", "Classification6");

		//rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept Code in HITSP C80 2.0: Table 2-147, column "Concept Name"
		validateLocalizedStringHITSPC80_20_Table147(classElement6, "XDR_MSG_175_5", "Classification6");

		//The "codingScheme" Slot rim:Slot/rim:ValueList/rim:Value SHOULD be 2.16.840.1.113883.6.96 
		//(except for concept code urn:nhin:names:acp:XACML)
		validateCodingSchemeValue(classElement6, "XDR_MSG_175_6", "Classification6", "2.16.840.1.113883.6.96");
	}
	
/*
  	"rim:Classification
	WHERE
	@classificationScheme= ""urn:uuid:cccf5598-8b07-4b77-a05eae952c785ead"""

	"Verify: 
	- @classifiedObject = the value of rim:ExtrinsicObject/@id
	- There MUST be a Slot element WHERE @name=""codingScheme"" AND rim:Slot/rim:ValueList/rim:Value is present
	- rim:Name/rim:LocalizedString/@value MUST be present
	- @nodeRepresentation is present
	- In the contexts of PRDS and PRDSML - Provide and Register Document Set, and DDSM - Distribute Document Set on Media:
	  - @nodeRepresentation value SHOULD come from the value set in HITSP C80 2.0: Table 2-149, column ""SNOMED CT � Concept ID""
	  - rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept ID in HITSP C80 2.0: Table 2-149, column ""Concept Name Specialty Area""
	  - The ""codingScheme"" Slot rim:Slot/rim:ValueList/rim:Value SHOULD be 2.16.840.1.113883.6.96

	Description: XDSDocumentEntry.practiceSettingCode metadata attribute.

	Example: 
	<rim:Classification id=""cl02""
	  classificationScheme=""urn:uuid:cccf5598-8b07-4b77-a05eae952c785ead""
	  classifiedObject=""Document01""
	  nodeRepresentation=""394577000"">
	  <rim:Slot name=""codingScheme"">
	    <rim:ValueList>
	      <rim:Value>2.16.840.1.113883.6.96</rim:Value>
	    </rim:ValueList>
	  </rim:Slot>
	  <rim:Name>
	    <rim:LocalizedString value=""Anesthetics""/>
	  </rim:Name>
	</rim:Classification>"
*/
	private void verifyClassification8(OMElement extrinsicObject, OMElement classElement)
	{
		//@classifiedObject = the value of rim:ExtrinsicObject/@id
		validateClassifiedObject(extrinsicObject, classElement, "XDR_MSG_177_1", "Classification8");
		
		//There MUST be a Slot element WHERE @name="codingScheme" AND rim:Slot/rim:ValueList/rim:Value is present
		validateCodingScheme(classElement, "XDR_MSG_177_2", "Classification6");
		
		//rim:Name/rim:LocalizedString/@value MUST be present
		//rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept ID in HITSP C80 2.0: Table 2-149, column "Concept Name Specialty Area"
		validateLocalizedStringHITSPC80_20_Table149(classElement, "XDR_MSG_177_3", "Classification8");
		
		
		//@nodeRepresentation is present
		//@nodeRepresentation value SHOULD come from the value set in HITSP C80 2.0: Table 2-144, column "Concept Code"
		//- rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept Code in HITSP C80 2.0: Table 2-144, columns "Concept Name" and "Description"
		validateNodeRepresentation(classElement, "XDR_MSG_177_4", "Classification8");

		//The "codingScheme" Slot rim:Slot/rim:ValueList/rim:Value SHOULD be 2.16.840.1.113883.6.96 
		validateCodingSchemeValue(classElement, "XDR_MSG_177_5", "Classification8", "2.16.840.1.113883.6.96");
	}	
	
	private void verifyClassification9(OMElement extrinsicObject, OMElement classElement)
	{
		//@classifiedObject = the value of rim:ExtrinsicObject/@id
		validateClassifiedObject(extrinsicObject, classElement, "XDR_MSG_178_1", "Classification9");
		
		//There MUST be a Slot element WHERE @name="codingScheme" AND rim:Slot/rim:ValueList/rim:Value is present
		validateCodingScheme(classElement, "XDR_MSG_178_2", "Classification9");
		
		//rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions
		//for the Concept Code in HITSP C80 2.0: Table 2-144, columns "Concept Name" and "Description"		
		//validateLocalizedStringHITSPC80_20(classElement, "XDR_MSG_178_3", "Classification9");
		
		
		//@nodeRepresentation is present
		//@nodeRepresentation value SHOULD come from the value set in HITSP C80 2.0: Table 2-144, column "Concept Code"
		//- rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept Code in HITSP C80 2.0: Table 2-144, columns "Concept Name" and "Description"
		validateNodeRepresentation(classElement, "XDR_MSG_178_4", "Classification9");

		//The "codingScheme" Slot rim:Slot/rim:ValueList/rim:Value SHOULD be 2.16.840.1.113883.6.1 
		//(except for concept code urn:nhin:names:acp:XACML)
		validateCodingSchemeValue(classElement, "XDR_MSG_178_5", "Classification9", "2.16.840.1.113883.6.1");
	}	
	

	private void validateNodeRepresentationHITSPC80_20(OMElement classElement, String errorCode, String elementName)
	{
		OMAttribute attr = classElement.getAttribute(new QName("nodeRepresentation"));
		if(attr == null)
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSDocumentEntry."+elementName, MessageType.Error);
		else
		{
			if(!ValidationUtil.isValidConceptCode(attr.getAttributeValue()))
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSDocumentEntry."+elementName, MessageType.Error);
		}
		
	}

	private void validateNodeRepresentation(OMElement classElement, String errorCode, String elementName)
	{
		OMAttribute attr = classElement.getAttribute(new QName("nodeRepresentation"));
		if(attr == null)
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSDocumentEntry."+elementName, MessageType.Error);
		else
		{
			if(!ValidationUtil.isValidHITSP_C80_20_Table2_147conceptCode(attr.getAttributeValue()))
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSDocumentEntry."+elementName, MessageType.Error);
		}
		
	}

	private void validateNodeRepresentationWithTable151(OMElement classElement, String errorCode, String elementName)
	{
		OMAttribute attr = classElement.getAttribute(new QName("nodeRepresentation"));
		if(attr == null)
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSDocumentEntry."+elementName, MessageType.Error);
		else
		{
			if(!ValidationUtil.isValidHITSP_C80_20_Table2_151conceptCode(attr.getAttributeValue()))
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSDocumentEntry."+elementName, MessageType.Error);
		}
		
	}

	private void validateNodeRepresentationWithTable147(OMElement classElement, String errorCode, String elementName)
	{
		OMAttribute attr = classElement.getAttribute(new QName("nodeRepresentation"));
		if(attr == null)
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSDocumentEntry."+elementName, MessageType.Error);
		else
		{
			if(!ValidationUtil.isValidHITSP_C80_20_Table2_147conceptCode(attr.getAttributeValue()))
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSDocumentEntry."+elementName, MessageType.Error);
		}
		
	}

	private void validateNodeRepresentationWithTable153(OMElement classElement, String errorCode, String elementName)
	{
		OMAttribute attr = classElement.getAttribute(new QName("nodeRepresentation"));
		if(attr == null)
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSDocumentEntry."+elementName, MessageType.Error);
		else
		{
			if(!ValidationUtil.isValidHITSP_C80_20_Table2_153conceptCode(attr.getAttributeValue()))
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSDocumentEntry."+elementName, MessageType.Error);
		}
		
	}

	//@classifiedObject = the value of rim:ExtrinsicObject/@id
	private void validateClassifiedObject(OMElement extrinsicObject, OMElement classElement, String errorCode, String elementName)
	{
		OMAttribute attr1 = classElement.getAttribute(new QName("classifiedObject"));
		if(attr1 == null)
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSDocumentEntry."+elementName, MessageType.Error);
		else
		{
			OMAttribute eAttr = extrinsicObject.getAttribute(new QName("id"));
			if(eAttr == null || !eAttr.getAttributeValue().equals(attr1.getAttributeValue()))
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSDocumentEntry."+elementName, MessageType.Error);
		}
		
	}

	//There MUST be a Slot element WHERE @name="codingScheme" AND rim:Slot/rim:ValueList/rim:Value is present
	private void validateCodingScheme(OMElement classElement, String errorCode, String elementName)
	{
		OMElement slot1 = ValidationUtil.findSlot(classElement, "codingScheme");
		if(slot1 == null)
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSDocumentEntry."+elementName, MessageType.Error);
		else
		{
			List<String> values = ValidationUtil.getSlotValueList(slot1);
			if(values == null || values.size() == 0)
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSDocumentEntry."+elementName, MessageType.Error);
		}
	}

	//rim:Name/rim:LocalizedString/@value SHOULD be consistent with the corresponding descriptions for the Concept Code in HITSP C80 2.0: Table 2-144, columns "Concept Name" and "Description"	
	private void validateLocalizedStringHITSPC80_20(OMElement classElement, String errorCode, String elementName)
	{
		Iterator nameElementIter = classElement.getChildrenWithLocalName("Name");
		if(!nameElementIter.hasNext())
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSDocumentEntry."+elementName, MessageType.Error);
		else
		{
			OMElement nameElement = (OMElement) nameElementIter.next();
			Iterator localIter = nameElement.getChildrenWithLocalName("LocalizedString");
			if(!localIter.hasNext())
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSDocumentEntry."+elementName, MessageType.Error);
			else
			{
				OMElement valueElement = (OMElement)localIter.next();
				OMAttribute attr = valueElement.getAttribute(new QName("value"));
				if(attr == null || attr.getAttributeValue() == null)
					errorRecorder.record(errorCode, "XDS Metadata Checklist",
							"XDSDocumentEntry."+elementName, MessageType.Error);
				else
				{
					if(!ValidationUtil.isValidHITSP_C80_20_Table2_144conceptCode(attr.getAttributeValue()))
					{
						errorRecorder.record(errorCode, "XDS Metadata Checklist",
								"XDSDocumentEntry."+elementName, MessageType.Error);
					}
				}
			}
			
		}
	}

	private void validateLocalizedStringHITSPC80_20_Table147(OMElement classElement, String errorCode, String elementName)
	{
		Iterator nameElementIter = classElement.getChildrenWithLocalName("Name");
		if(!nameElementIter.hasNext())
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSDocumentEntry."+elementName, MessageType.Error);
		else
		{
			OMElement nameElement = (OMElement) nameElementIter.next();
			Iterator localIter = nameElement.getChildrenWithLocalName("LocalizedString");
			if(!localIter.hasNext())
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSDocumentEntry."+elementName, MessageType.Error);
			else
			{
				OMElement valueElement = (OMElement)localIter.next();
				OMAttribute attr = valueElement.getAttribute(new QName("value"));
				if(attr == null || attr.getAttributeValue() == null)
					errorRecorder.record(errorCode, "XDS Metadata Checklist",
							"XDSDocumentEntry."+elementName, MessageType.Error);
				else
				{
					if(!ValidationUtil.isValidHITSP_C80_20_Table2_147conceptName(attr.getAttributeValue()))
					{
						errorRecorder.record(errorCode, "XDS Metadata Checklist",
								"XDSDocumentEntry."+elementName, MessageType.Error);
					}
				}
			}
			
		}
	}

	private void validateLocalizedStringHITSPC80_20_Table149(OMElement classElement, String errorCode, String elementName)
	{
		Iterator nameElementIter = classElement.getChildrenWithLocalName("Name");
		if(!nameElementIter.hasNext())
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSDocumentEntry."+elementName, MessageType.Error);
		else
		{
			OMElement nameElement = (OMElement) nameElementIter.next();
			Iterator localIter = nameElement.getChildrenWithLocalName("LocalizedString");
			if(!localIter.hasNext())
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSDocumentEntry."+elementName, MessageType.Error);
			else
			{
				OMElement valueElement = (OMElement)localIter.next();
				OMAttribute attr = valueElement.getAttribute(new QName("value"));
				if(attr == null || attr.getAttributeValue() == null)
					errorRecorder.record(errorCode, "XDS Metadata Checklist",
							"XDSDocumentEntry."+elementName, MessageType.Error);
				else
				{
					if(!ValidationUtil.isValidHITSP_C80_20_Table2_149conceptName(attr.getAttributeValue()))
					{
						errorRecorder.record(errorCode, "XDS Metadata Checklist",
								"XDSDocumentEntry."+elementName, MessageType.Error);
					}
				}
			}
			
		}
	}
	
	//rim:Name/rim:LocalizedString/@value MUST be present
	private void validateLocalizedString(OMElement classElement, String errorCode, String elementName)
	{
		Iterator nameElementIter = classElement.getChildrenWithLocalName("Name");
		if(!nameElementIter.hasNext())
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSDocumentEntry."+elementName, MessageType.Error);
		else
		{
			OMElement nameElement = (OMElement) nameElementIter.next();
			Iterator localIter = nameElement.getChildrenWithLocalName("LocalizedString");
			if(!localIter.hasNext())
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSDocumentEntry."+elementName, MessageType.Error);
			else
			{
				OMElement valueElement = (OMElement)localIter.next();
				OMAttribute attr = valueElement.getAttribute(new QName("value"));
				if(attr == null || attr.getAttributeValue() == null)
					errorRecorder.record(errorCode, "XDS Metadata Checklist",
							"XDSDocumentEntry."+elementName, MessageType.Error);
			}
			
		}
	}

	//rim:Name/rim:LocalizedString/@value MUST be present
	private void validateLocalizedStringValueWithConceptNameSpecialityArea(OMElement classElement, String errorCode, String elementName)
	{
		Iterator nameElementIter = classElement.getChildrenWithLocalName("Name");
		if(!nameElementIter.hasNext())
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSDocumentEntry."+elementName, MessageType.Error);
		else
		{
			OMElement nameElement = (OMElement) nameElementIter.next();
			Iterator localIter = nameElement.getChildrenWithLocalName("LocalizedString");
			if(!localIter.hasNext())
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSDocumentEntry."+elementName, MessageType.Error);
			else
			{
				OMElement valueElement = (OMElement)localIter.next();
				OMAttribute attr = valueElement.getAttribute(new QName("value"));
				if(attr == null || attr.getAttributeValue() == null)
					errorRecorder.record(errorCode, "XDS Metadata Checklist",
							"XDSDocumentEntry."+elementName, MessageType.Error);
				else
				{
					if(!ValidationUtil.isValidConceptNameSpecialityArea(attr.getAttributeValue()))
						errorRecorder.record(errorCode, "XDS Metadata Checklist",
								"XDSDocumentEntry."+elementName, MessageType.Error);
				}
			}
			
		}
	}
	
	//The "codingScheme" Slot rim:Slot/rim:ValueList/rim:Value SHOULD be 1.3.6.1.4.1.19376.1.2.3 
	//(except for concept code urn:nhin:names:acp:XACML)
	private void validateCodingSchemeValue(OMElement classElement, String errorCode, String elementName, String codeValue)
	{
		OMElement codeSlot = ValidationUtil.findSlot(classElement, "codingScheme");
		if(codeSlot == null)
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSDocumentEntry."+elementName, MessageType.Warning);
		else
		{
			List values = ValidationUtil.getSlotValueList(codeSlot);
			if(values == null || values.size() == 0)
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSDocumentEntry."+elementName, MessageType.Error);
			else
			{
				String value = (String)values.get(0);
				if(!value.equals(codeValue))
					errorRecorder.record(errorCode, "XDS Metadata Checklist",
							"XDSDocumentEntry."+elementName, MessageType.Error);
					
			}
		}
	}
	
	
	private void verifyAuthor(OMElement classElement)
	{
		OMElement authorInstSlot = ValidationUtil.findSlot(classElement, "authorInstitution");
		if(authorInstSlot == null)
			errorRecorder.record("XDR_MSG_167", "XDS Metadata Checklist",
					"XDSDocumentEntry.authorInstitution", MetadataType.instance.getMessageType(metadataType, "167"));
		else
		{
			List<String> values = ValidationUtil.getSlotValueList(authorInstSlot);
			if(values == null || values.size() == 0)
				errorRecorder.record("XDR_MSG_167_1", "XDS Metadata Checklist",
						"XDSDocumentEntry.authorInstitution", MessageType.Error);
		}

		OMElement authorPersonSlot = ValidationUtil.findSlot(classElement, "authorPerson");
		if(authorPersonSlot == null)
			errorRecorder.record("XDR_MSG_168", "XDS Metadata Checklist",
					"XDSDocumentEntry.authorPerson", MetadataType.instance.getMessageType(metadataType, "168"));
		else
		{
			List<String> values = ValidationUtil.getSlotValueList(authorPersonSlot);
			if(values == null || values.size() == 0)
				errorRecorder.record("XDR_MSG_168_1", "XDS Metadata Checklist",
						"XDSDocumentEntry.authorPerson", MessageType.Error);
		}
		
		OMElement authorRoleSlot = ValidationUtil.findSlot(classElement, "authorRole");
		if(authorRoleSlot == null)
			errorRecorder.record("XDR_MSG_169", "XDS Metadata Checklist",
					"XDSDocumentEntry.authorRole", MetadataType.instance.getMessageType(metadataType, "169"));
		else
		{
			List<String> values = ValidationUtil.getSlotValueList(authorRoleSlot);
			if(values == null || values.size() == 0)
				errorRecorder.record("XDR_MSG_169_1", "XDS Metadata Checklist",
						"XDSDocumentEntry.authorRole", MessageType.Error);
		}

		OMElement authorSpecialtySlot = ValidationUtil.findSlot(classElement, "authorSpecialty");
		if(authorSpecialtySlot == null)
			errorRecorder.record("XDR_MSG_170", "XDS Metadata Checklist",
					"XDSDocumentEntry.authorSpecialty", MetadataType.instance.getMessageType(metadataType, "170"));
		else
		{
			List<String> values = ValidationUtil.getSlotValueList(authorSpecialtySlot);
			if(values == null || values.size() == 0)
				errorRecorder.record("XDR_MSG_170_1", "XDS Metadata Checklist",
						"XDSDocumentEntry.authorSpecialty", MessageType.Error);
		}
	}
}
