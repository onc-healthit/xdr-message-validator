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

public class XDSSubmissionSetValidator extends XDRValidator {
	MessageRecorder errorRecorder;
	String metadataType = MetadataType.instance.getMetadataType();
	@Override
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
			errorRecorder.record("XDS_MSG_181", Constants.XDS_Metadata_Checklist,
					Constants.XDSSubmissionSet_RegistryPackage, MetadataType.instance.getMessageType(metadataType, "181"));
			e.printStackTrace();
		}
	}

	private void validateRegistryPackage(OMElement registryObjectElement)
	{
		Iterator registryPackageIter = registryObjectElement
				.getChildrenWithLocalName("RegistryPackage");
		if (!registryPackageIter.hasNext())
		{
			errorRecorder.record("XDS_MSG_181",
					Constants.XDS_Metadata_Checklist, Constants.XDSSubmissionSet_RegistryPackage,
					MetadataType.instance.getMessageType(metadataType, "181"));
			return;
		}
		
		OMElement classificationElement = ValidationUtil.findClassificationChildElement(
				registryObjectElement,
				"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd");
		
		while (registryPackageIter.hasNext()) {
			OMElement registryPackageElement = (OMElement) registryPackageIter
					.next();
			
			if(registryPackageIter.hasNext())
				errorRecorder.record("XDS_MSG_181_1",
						Constants.XDS_Metadata_Checklist,
						Constants.XDSSubmissionSet_RegistryPackage, MessageType.Error);
				
			if (classificationElement == null) {
				classificationElement = ValidationUtil.findClassificationChildElement(
						registryPackageElement,
						"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd");
			}

			OMAttribute idAttr = registryPackageElement
					.getAttribute(new QName("id"));

			if (classificationElement == null)
				errorRecorder.record("XDS_MSG_181",
						Constants.XDS_Metadata_Checklist,
						Constants.XDSSubmissionSet_RegistryPackage, MetadataType.instance.getMessageType(metadataType, "181"));
			else {
				OMAttribute classAttr = classificationElement
						.getAttribute(new QName(
								"classifiedObject"));
				if (classAttr == null)
					errorRecorder.record("XDS_MSG_181",
							Constants.XDS_Metadata_Checklist,
							Constants.XDSSubmissionSet_RegistryPackage,
							MetadataType.instance.getMessageType(metadataType, "181"));
				else {
					if (!classAttr.getAttributeValue().equals(
							idAttr.getAttributeValue()))
						errorRecorder.record("XDS_MSG_181",
								Constants.XDS_Metadata_Checklist,
								Constants.XDSSubmissionSet_RegistryPackage,
								MetadataType.instance.getMessageType(metadataType, "181"));
				}
			}
			if (idAttr == null)
				errorRecorder.record("XDS_MSG_182",
						Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.RegistryPackage.id",
						MetadataType.instance.getMessageType(metadataType, "182"));

			OMAttribute statusAttr = registryPackageElement
					.getAttribute(new QName("status"));
			if (statusAttr != null)
				errorRecorder.record("XDS_MSG_183",
						Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.RegistryPackage.status",
						MetadataType.instance.getMessageType(metadataType, "183"));

			OMAttribute homeAttr = registryPackageElement
					.getAttribute(new QName("home"));
			if (homeAttr != null)
				errorRecorder.record("XDS_MSG_184",
						Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.RegistryPackage.home",
						MetadataType.instance.getMessageType(metadataType, "184"));
			
			validateSlots(registryPackageElement);
			ValidationUtil.validateRegistryPackageName(registryPackageElement, errorRecorder, "XDS_MSG_187", "XDSFolder.RegistryPackage.Name");
			ValidationUtil.validateRegistryPackageDescription(registryPackageElement, errorRecorder, "XDS_MSG_188", "XDSFolder.RegistryPackage.Description");
			new XDSSubmissionSetClassificationValidator().validate(registryPackageElement, errorRecorder);
		}
	}
	

	
	private void validateSlots(OMElement registryPackageElement)
	{
		//rim:ValueList contains list of one or more rim:Value elements. For each rim:Value:
		//	  - The format is organization|person, organization, or |person
		//	  - organization is in the HL7 V2 XON format (see example)
		//	  - person is in the HL7 V2 XCN format (see example)		
		OMElement slot = ValidationUtil.findSlot(registryPackageElement, "intendedRecipient");
		if(slot == null)
			errorRecorder.record("XDS_MSG_185",
					Constants.XDS_Metadata_Checklist,
					"XDSSubmissionSet.RegistryPackage.Slot.intendedRecipient",
					MetadataType.instance.getMessageType(metadataType, "185"));
		else
		{
			List valueList = ValidationUtil.getSlotValueList(slot);
			if(valueList == null || valueList.size() == 0)
				errorRecorder.record("XDS_MSG_185_1",
						Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.RegistryPackage.Slot.intendedRecipient",
						MessageType.Error);
			else
			{
				//Format validation
			}
				
		}
		
		//Verify: rim:ValueList/rim:Value:
		//	- MUST be a single value.
		//	- MUST be in DTM format.
		//	- MUST be a time in the past.		
		if(!ValidationUtil.isSlotUnique(registryPackageElement, "submissionTime"))
			errorRecorder.record("XDS_MSG_186_1",
					Constants.XDS_Metadata_Checklist,
					"XDSSubmissionSet.RegistryPackage.Slot.submissionTime",
					MessageType.Error);
		else
		{
			OMElement submissionSlot = ValidationUtil.findSlot(registryPackageElement, "submissionTime");
			if(submissionSlot == null)
				errorRecorder.record("XDR_MSG_186", Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.RegistryPackage.Slot.submissionTime", MetadataType.instance.getMessageType(metadataType, "186"));
			
			List<String> valueList = ValidationUtil.getSlotValueList(submissionSlot);
			if (valueList.size() == 0)
				errorRecorder.record("XDR_MSG_186_2", Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.RegistryPackage.Slot.submissionTime", MessageType.Error);

			String attrValue = valueList.get(0);
			Calendar currDtCal = Calendar.getInstance();
			Date currDt = currDtCal.getTime();

			if (!ValidationUtil.validateAndCompareUTCFormat(attrValue, currDt))
				errorRecorder.record("XDR_MSG_186_2", Constants.XDS_Metadata_Checklist,
						"XDSSubmissionSet.RegistryPackage.Slot.submissionTime", MessageType.Error);
			
		}
		
	}
	


	public String getName() {
		return "XDSSubmissionSetValidator";
	}

}
