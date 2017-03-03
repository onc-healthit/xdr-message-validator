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
import java.util.Iterator;
import java.util.List;

public class XDSAssociationValidator extends XDRValidator{
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
					validateAssociation1(rolElement);
				}
			}
		} catch (XMLParserException e) {
			errorRecorder.record("XDS_MSG_199", Constants.XDS_Metadata_Checklist,
					"XDSFolder.RegistryPackage", MetadataType.instance.getMessageType(metadataType, "199"));
			e.printStackTrace();
		}
	}

	//rim:Association
	//WHERE
	//@sourceObject = rim:RegistryPackage/@id of an XDSSubmissionSet in this rim:RegistryObjectList
	private void validateAssociation1(OMElement registryObjectListElement) {
		Iterator registryPackageIter = registryObjectListElement
				.getChildrenWithLocalName("Association");
		if (!registryPackageIter.hasNext()) {
			errorRecorder.record("XDS_MSG_210", Constants.XDS_Metadata_Checklist,
					"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "210"));
			return;
		}

		while (registryPackageIter.hasNext()) {
			OMElement associationElement = (OMElement) registryPackageIter
					.next();
			OMAttribute sourceAttr = associationElement.getAttribute(new QName("sourceObject"));
			if(sourceAttr == null)
				errorRecorder.record("XDS_MSG_210", Constants.XDS_Metadata_Checklist,
						"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "210"));
			else {
				OMElement registryPackageEl = ValidationUtil.findRegistryPackageById(registryObjectListElement, sourceAttr.getAttributeValue());
				if(registryPackageEl == null)
					errorRecorder.record("XDS_MSG_210", Constants.XDS_Metadata_Checklist,
							"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "210"));
			}

			OMAttribute targetAttr = associationElement.getAttribute(new QName("targetObject"));
			if(targetAttr == null)
				errorRecorder.record("XDS_MSG_210", Constants.XDS_Metadata_Checklist,
						"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "210"));
			else {
				OMElement exObjEl = ValidationUtil.findExtrinsicObjectById(registryObjectListElement, targetAttr.getAttributeValue());
				if(exObjEl == null)
					errorRecorder.record("XDS_MSG_210", Constants.XDS_Metadata_Checklist,
							"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "210"));
			}
			validateSlot1(associationElement);
		}
		
	}

	//"rim:Slot
	//WHERE
	//@name=""SubmissionSetStatus"""

	private void validateSlot1(OMElement associationElement)
	{
		OMElement slot = ValidationUtil.findSlot(associationElement, "SubmissionSetStatus");
		if(slot == null)
			errorRecorder.record("XDS_MSG_211", Constants.XDS_Metadata_Checklist,
					"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "211"));
		else
		{
			List valueList = ValidationUtil.getSlotValueList(slot);
			if(valueList == null || valueList.size() == 0)
				errorRecorder.record("XDS_MSG_211", Constants.XDS_Metadata_Checklist,
						"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "211"));
			else
			{
				String value = (String)valueList.get(0);
				if(!value.equals("Original"))
					errorRecorder.record("XDS_MSG_211", Constants.XDS_Metadata_Checklist,
							"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "211"));
			}
		}
	}

	//"rim:Association
	//WHERE
	//@sourceObject = rim:RegistryPackage/@id of an XDSSubmissionSet in this rim:RegistryObjectList
	//AND
	//@targetObject = the @id of a rim:ObjectRef in this rim:RegistryObjectList"
	private void validateAssociation2(OMElement registryObjectListElement) {
		Iterator registryPackageIter = registryObjectListElement
				.getChildrenWithLocalName("Association");
		if (!registryPackageIter.hasNext()) {
			errorRecorder.record("XDS_MSG_212", Constants.XDS_Metadata_Checklist,
					"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "212"));
			return;
		}

		while (registryPackageIter.hasNext()) {
			OMElement associationElement = (OMElement) registryPackageIter
					.next();
			OMAttribute sourceAttr = associationElement.getAttribute(new QName("sourceObject"));
			if(sourceAttr == null)
				errorRecorder.record("XDS_MSG_212", Constants.XDS_Metadata_Checklist,
						"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "212"));
			else {
				OMElement registryPackageEl = ValidationUtil.findRegistryPackageById(registryObjectListElement, sourceAttr.getAttributeValue());
				if(registryPackageEl == null)
					errorRecorder.record("XDS_MSG_212", Constants.XDS_Metadata_Checklist,
							"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "212"));
			}

			OMAttribute targetAttr = associationElement.getAttribute(new QName("targetObject"));
			if(targetAttr == null)
				errorRecorder.record("XDS_MSG_212", Constants.XDS_Metadata_Checklist,
						"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "212"));
			else {
				OMElement exObjEl = ValidationUtil.findObjectRefById(registryObjectListElement, targetAttr.getAttributeValue());
				if(exObjEl == null)
					errorRecorder.record("XDS_MSG_212", Constants.XDS_Metadata_Checklist,
							"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "212"));
			}
			validateSlot2(associationElement);
		}
		
	}

	//"rim:Slot
	//WHERE
	//@name=""SubmissionSetStatus"""

	private void validateSlot2(OMElement associationElement)
	{
		OMElement slot = ValidationUtil.findSlot(associationElement, "SubmissionSetStatus");
		if(slot == null)
			errorRecorder.record("XDS_MSG_211", Constants.XDS_Metadata_Checklist,
					"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "211"));
		else
		{
			List valueList = ValidationUtil.getSlotValueList(slot);
			if(valueList == null || valueList.size() == 0)
				errorRecorder.record("XDS_MSG_211", Constants.XDS_Metadata_Checklist,
						"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "211"));
			else
			{
				String value = (String)valueList.get(0);
				if(!value.equals("Original"))
					errorRecorder.record("XDS_MSG_211", Constants.XDS_Metadata_Checklist,
							"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "211"));
			}
		}
	}
	
	//"rim:Association
		//WHERE
		//@sourceObject = rim:RegistryPackage/@id of an XDSSubmissionSet in this rim:RegistryObjectList
		//AND
		//@targetObject = the @id of a rim:ObjectRef in this rim:RegistryObjectList"
		private void validateAssociation3(OMElement registryObjectListElement) {
			Iterator registryPackageIter = registryObjectListElement
					.getChildrenWithLocalName("Association");
			if (!registryPackageIter.hasNext()) {
				errorRecorder.record("XDS_MSG_214", Constants.XDS_Metadata_Checklist,
						"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "214"));
				return;
			}

			while (registryPackageIter.hasNext()) {
				OMElement associationElement = (OMElement) registryPackageIter
						.next();
				OMAttribute sourceAttr = associationElement.getAttribute(new QName("sourceObject"));
				if(sourceAttr == null)
					errorRecorder.record("XDS_MSG_214", Constants.XDS_Metadata_Checklist,
							"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "214"));
				else {
					OMElement registryPackageEl = ValidationUtil.findRegistryPackageById(registryObjectListElement, sourceAttr.getAttributeValue());
					if(registryPackageEl == null)
						errorRecorder.record("XDS_MSG_214", Constants.XDS_Metadata_Checklist,
								"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "214"));
				}

				OMAttribute targetAttr = associationElement.getAttribute(new QName("targetObject"));
				if(targetAttr == null)
					errorRecorder.record("XDS_MSG_214", Constants.XDS_Metadata_Checklist,
							"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "214"));
				else {
					OMElement exObjEl = ValidationUtil.findRegistryPackageById(registryObjectListElement, targetAttr.getAttributeValue());
					if(exObjEl == null)
						errorRecorder.record("XDS_MSG_214", Constants.XDS_Metadata_Checklist,
								"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "214"));
				}
				validateSlot3(associationElement);
			}
			
		}

		//"rim:Slot
		//WHERE
		//@name=""SubmissionSetStatus"""

		private void validateSlot3(OMElement associationElement)
		{
			OMElement slot = ValidationUtil.findSlot(associationElement, "SubmissionSetStatus");
			if(slot == null)
				errorRecorder.record("XDS_MSG_211", Constants.XDS_Metadata_Checklist,
						"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "211"));
			else
			{
				List valueList = ValidationUtil.getSlotValueList(slot);
				if(valueList == null || valueList.size() == 0)
					errorRecorder.record("XDS_MSG_211", Constants.XDS_Metadata_Checklist,
							"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "211"));
				else
				{
					String value = (String)valueList.get(0);
					if(!value.equals("Original"))
						errorRecorder.record("XDS_MSG_211", Constants.XDS_Metadata_Checklist,
								"XDSAssociations.Association", MetadataType.instance.getMessageType(metadataType, "211"));
				}
			}
		}	
	@Override
	public String getName() {
		return "XDSAssosiationValidator";
	}
}
