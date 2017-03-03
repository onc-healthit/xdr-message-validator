package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;
import gov.onc.xdrtesttool.exception.XMLParserException;
import gov.onc.xdrtesttool.resource.MetadataType;
import gov.onc.xdrtesttool.xml.XMLParser;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.springframework.ws.soap.SoapMessage;

import javax.ws.rs.core.UriBuilder;
import javax.xml.namespace.QName;
import java.util.Iterator;

public class EbRimValidator extends XDRValidator {
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
				errorRecorder.record("XDS_MSG_100",
						Constants.XDS_Metadata_Checklist,
						"ebRIM", MetadataType.instance.getMessageType(metadataType, "100"));
				return;
			}
			Iterator submitObjectsRequestIter = element
					.getChildrenWithLocalName("SubmitObjectsRequest");
			while (submitObjectsRequestIter.hasNext()) {
				OMElement sorElement = (OMElement) submitObjectsRequestIter
						.next();
				Iterator registryObjectListIter = sorElement
						.getChildrenWithLocalName("RegistryObjectList");
				if (!registryObjectListIter.hasNext())
					errorRecorder.record("XDS_MSG_101",
							Constants.XDS_Metadata_Checklist,
							"DS General:rim:RegistryObjectList",
							MetadataType.instance.getMessageType(metadataType, "101"));
				else {
					sorElement.getLineNumber();
					while (registryObjectListIter.hasNext()) {
						OMElement rolElement = (OMElement) registryObjectListIter
								.next();
						verifyIdentifiable(rolElement);
						validateObjectRef(rolElement);
						validateRegistryObject(rolElement);
						validateExtrinsicObject(rolElement);
					}
				}

			}
		} catch (XMLParserException e) {
			errorRecorder.record("XDS_MSG_100",
					Constants.XDS_Metadata_Checklist,
					"ebRIM", MetadataType.instance.getMessageType(metadataType, "100"));
			e.printStackTrace();
		}
	}

	private void verifyIdentifiable(OMElement registryObjectElement) {
		Iterator identifiableIter = registryObjectElement
				.getChildrenWithLocalName("Identifiable");
		if (!identifiableIter.hasNext()) {
			errorRecorder.record("XDS_MSG_102",
					Constants.XDS_Metadata_Checklist, "ebRIM.Identifiable",
					MetadataType.instance.getMessageType(metadataType, "102"));
			return;
		}

		while (identifiableIter.hasNext()) {
			OMElement identiElement = (OMElement) identifiableIter.next();
			OMAttribute homeAttr = identiElement
					.getAttribute(new QName("home"));
			if (homeAttr == null) {
				errorRecorder.record("XDS_MSG_103",
						Constants.XDS_Metadata_Checklist,
						"ebRIM.Identifiable.home", MetadataType.instance.getMessageType(metadataType, "103"));
			} else {
				try {
					UriBuilder.fromUri(homeAttr.getAttributeValue());
				} catch (IllegalArgumentException e) {
					errorRecorder.record("XDS_MSG_103_1",
							Constants.XDS_Metadata_Checklist,
							"ebRIM.Identifiable.home", MessageType.Error);
				}
			}

			OMAttribute idAttr = identiElement.getAttribute(new QName("id"));
			if (idAttr == null) {
				errorRecorder.record("XDS_MSG_104",
						Constants.XDS_Metadata_Checklist,
						"ebRIM.Identifiable.id", MetadataType.instance.getMessageType(metadataType, "104"));
			} else {
				try {
					String idValue = idAttr.getAttributeValue();
					if (idValue == null)
						errorRecorder.record("XDS_MSG_104",
								Constants.XDS_Metadata_Checklist,
								"ebRIM.Identifiable.id", MetadataType.instance.getMessageType(metadataType, "104"));
					else {
						try {
							UriBuilder.fromUri(idValue);
						} catch (IllegalArgumentException e) {
							errorRecorder.record("XDS_MSG_104_1",
									Constants.XDS_Metadata_Checklist,
									"ebRIM.Identifiable.id", MessageType.Error);
						}

						if (!ValidationUtil.isValidUUID(idValue))
							errorRecorder.record("XDS_MSG_104_2",
									Constants.XDS_Metadata_Checklist,
									"ebRIM.Identifiable.id", MessageType.Error);
					}
				} catch (IllegalArgumentException e) {
					errorRecorder.record("XDS_MSG_103_1",
							Constants.XDS_Metadata_Checklist,
							"ebRIM.Identifiable.home", MessageType.Error);
				}
			}
			validateIdentifiableSlot(identiElement);
		}

	}

	private void validateIdentifiableSlot(OMElement idElement) {
		Iterator slotIter = idElement.getChildrenWithLocalName("Slot");
		if (!slotIter.hasNext()) {
			errorRecorder.record("XDS_MSG_105",
					Constants.XDS_Metadata_Checklist,
					"ebRIM.Identifiable.Slot", MetadataType.instance.getMessageType(metadataType, "105"));
			return;
		} else {
			while (slotIter.hasNext()) {
				OMElement slot = (OMElement) slotIter.next();
				OMAttribute nameAttr = slot.getAttribute(new QName("name"));
				if (nameAttr == null)
					errorRecorder.record("XDS_MSG_106",
							Constants.XDS_Metadata_Checklist,
							"ebRIM.Identifiable.Slot", MetadataType.instance.getMessageType(metadataType, "106"));
				OMAttribute typeAttr = slot.getAttribute(new QName("slotType"));
				if (typeAttr == null)
					errorRecorder.record("XDS_MSG_107",
							Constants.XDS_Metadata_Checklist,
							"ebRIM.Identifiable.Slot", MetadataType.instance.getMessageType(metadataType, "107"));
				Iterator valueListIter = slot
						.getChildrenWithLocalName("ValueList");
				if (!valueListIter.hasNext())
					errorRecorder.record("XDS_MSG_108",
							Constants.XDS_Metadata_Checklist,
							"ebRIM.Identifiable.Slot", MetadataType.instance.getMessageType(metadataType, "108"));
				else {
					OMElement valueListElement = (OMElement) valueListIter
							.next();
					Iterator valueIter = valueListElement
							.getChildrenWithLocalName("Value");
					if (!valueIter.hasNext())
						errorRecorder.record("XDS_MSG_109",
								Constants.XDS_Metadata_Checklist,
								"ebRIM.Identifiable.Slot", MetadataType.instance.getMessageType(metadataType, "109"));
				}

			}
		}
	}

	private void validateObjectRef(OMElement idElement) {
		Iterator orefIter = idElement.getChildrenWithLocalName("ObjectRef");
		if (!orefIter.hasNext()) {
			errorRecorder.record("XDS_MSG_110",
					Constants.XDS_Metadata_Checklist,
					"ebRIM.Identifiable.ObjectRef", MetadataType.instance.getMessageType(metadataType, "110"));
			return;
		} else {
			while (orefIter.hasNext()) {
				OMElement objectRef = (OMElement) orefIter.next();
				OMAttribute nameAttr = objectRef.getAttribute(new QName("id"));
				if (nameAttr == null)
					errorRecorder.record("XDS_MSG_111",
							Constants.XDS_Metadata_Checklist,
							"ebRIM.ObjectRef", MetadataType.instance.getMessageType(metadataType, "111"));
				else if (!ValidationUtil.isValidUUID(nameAttr
						.getAttributeValue()))
					errorRecorder.record("XDS_MSG_111",
							Constants.XDS_Metadata_Checklist,
							"ebRIM.ObjectRef", MetadataType.instance.getMessageType(metadataType, "111"));

				OMAttribute replicaAttr = objectRef.getAttribute(new QName(
						"createReplica"));
				if (replicaAttr == null)
					errorRecorder.record("XDS_MSG_112",
							Constants.XDS_Metadata_Checklist,
							"ebRIM.ObjectRef", MetadataType.instance.getMessageType(metadataType, "112"));
			}
		}
	}

	private void validateRegistryObject(OMElement element) {
		Iterator orefIter = element.getChildrenWithLocalName("RegistryObject");
		if (!orefIter.hasNext()) {
			errorRecorder.record("XDS_MSG_113",
					Constants.XDS_Metadata_Checklist, "ebRIM.RegistryObject",
					MetadataType.instance.getMessageType(metadataType, "13"));
			return;
		} else {
			while (orefIter.hasNext()) {
				OMElement objectRef = (OMElement) orefIter.next();
				OMAttribute lidAttr = objectRef.getAttribute(new QName("lid"));
				if (lidAttr == null)
					errorRecorder.record("XDS_MSG_114",
							Constants.XDS_Metadata_Checklist,
							"ebRIM.RegistryObject.lid", MetadataType.instance.getMessageType(metadataType, "114"));

				OMAttribute replicaAttr = objectRef.getAttribute(new QName(
						"objectType"));
				if (replicaAttr == null)
					errorRecorder
							.record("XDS_MSG_115",
									Constants.XDS_Metadata_Checklist,
									"ebRIM.RegistryObject.objectType",
									MetadataType.instance.getMessageType(metadataType, "115"));

				OMAttribute statusAttr = objectRef.getAttribute(new QName(
						"status"));
				if (statusAttr != null)
					errorRecorder.record("XDS_MSG_116",
							Constants.XDS_Metadata_Checklist,
							"ebRIM.RegistryObject.status", MetadataType.instance.getMessageType(metadataType, "116"));

				Iterator nameIter = objectRef.getChildrenWithLocalName("Name");
				if (!nameIter.hasNext())
					errorRecorder.record("XDS_MSG_117",
							Constants.XDS_Metadata_Checklist,
							"ebRIM.RegistryObject.Name", MetadataType.instance.getMessageType(metadataType, "117"));
				else {
					while (nameIter.hasNext()) {
						OMElement nameElement = (OMElement) nameIter.next();
						Iterator localizedIter = nameElement
								.getChildrenWithLocalName("LocalizedString");
						if (!localizedIter.hasNext())
							errorRecorder.record("XDS_MSG_118",
									Constants.XDS_Metadata_Checklist,
									"ebRIM.RegistryObject.Name.LocalizedString",
									MetadataType.instance.getMessageType(metadataType, "118"));
						else {
							while (localizedIter.hasNext()) {
								OMElement localElement = (OMElement) localizedIter
										.next();
								OMAttribute charsetAttr = localElement
										.getAttribute(new QName("charset"));
								if (charsetAttr == null)
									errorRecorder
											.record("XDS_MSG_120",
													Constants.XDS_Metadata_Checklist,
													"ebRIM.RegistryObject.Name.LocalizedString.charset",
													MetadataType.instance.getMessageType(metadataType, "120"));
								OMAttribute valueAttr = localElement
										.getAttribute(new QName("value"));
								if (valueAttr == null)
									errorRecorder
											.record("XDS_MSG_121",
													Constants.XDS_Metadata_Checklist,
													"ebRIM.RegistryObject.Name.LocalizedString.value",
													MetadataType.instance.getMessageType(metadataType, "121"));
							}
						}
					}
				}
				Iterator descrIter = objectRef.getChildrenWithLocalName("Description");
				if (!descrIter.hasNext())
					errorRecorder.record("XDS_MSG_122",
							Constants.XDS_Metadata_Checklist,
							"ebRIM.RegistryObject.Description", MetadataType.instance.getMessageType(metadataType, "122"));
				else {
					while (descrIter.hasNext()) {
						OMElement descrElement = (OMElement) nameIter.next();
						Iterator localizedIter = descrElement
								.getChildrenWithLocalName("LocalizedString");
						if (!localizedIter.hasNext())
							errorRecorder.record("XDS_MSG_123",
									Constants.XDS_Metadata_Checklist,
									"ebRIM.RegistryObject.Description.LocalizedString",
									MetadataType.instance.getMessageType(metadataType, "123"));
						else {
							while (localizedIter.hasNext()) {
								OMElement localElement = (OMElement) localizedIter
										.next();
								OMAttribute charsetAttr = localElement
										.getAttribute(new QName("charset"));
								if (charsetAttr == null)
									errorRecorder
											.record("XDS_MSG_125",
													Constants.XDS_Metadata_Checklist,
													"ebRIM.RegistryObject.Description.LocalizedString.charset",
													MetadataType.instance.getMessageType(metadataType, "125"));
								OMAttribute valueAttr = localElement
										.getAttribute(new QName("value"));
								if (valueAttr == null)
									errorRecorder
											.record("XDS_MSG_126",
													Constants.XDS_Metadata_Checklist,
													"ebRIM.RegistryObject.Description.LocalizedString.value",
													MetadataType.instance.getMessageType(metadataType, "126"));
							}
						}
					}
					Iterator vInfoIter = objectRef.getChildrenWithLocalName("VersionInfo");
					if (vInfoIter.hasNext())
						errorRecorder.record("XDS_MSG_127",
								Constants.XDS_Metadata_Checklist,
								"ebRIM.RegistryObject.VersionInfo", MetadataType.instance.getMessageType(metadataType, "127"));
					
					Iterator classificationIter = objectRef.getChildrenWithLocalName("Classification");
					if (classificationIter.hasNext())
						errorRecorder.record("XDS_MSG_130",
								Constants.XDS_Metadata_Checklist,
								"ebRIM.RegistryObject.VersionInfo", MetadataType.instance.getMessageType(metadataType, "130"));
					else
					{
						while(classificationIter.hasNext())
						{
							OMElement classEle = (OMElement) classificationIter.next();
							OMAttribute schemeAttr = classEle
									.getAttribute(new QName("classificationScheme"));
							if (schemeAttr == null)
								errorRecorder
										.record("XDS_MSG_131",
												Constants.XDS_Metadata_Checklist,
												"ebRIM.RegistryObject.Classification.classificationScheme",
												MetadataType.instance.getMessageType(metadataType, "131"));
							{
								if(!ValidationUtil.isValidUUID(schemeAttr.getAttributeValue()))
									errorRecorder
									.record("XDS_MSG_131_1",
											Constants.XDS_Metadata_Checklist,
											"ebRIM.RegistryObject.Classification.classificationScheme",
											MessageType.Error);
							}
							
							OMAttribute classObjAttr = classEle
									.getAttribute(new QName("classifiedObject"));
							if (classObjAttr == null)
								errorRecorder
										.record("XDS_MSG_132",
												Constants.XDS_Metadata_Checklist,
												"ebRIM.RegistryObject.Classification.classifiedObject",
												MetadataType.instance.getMessageType(metadataType, "132"));
							else
							{
								if(lidAttr != null && !lidAttr.getAttributeValue().equals(classObjAttr.getAttributeValue()))
									errorRecorder
									.record("XDS_MSG_132_1",
											Constants.XDS_Metadata_Checklist,
											"ebRIM.RegistryObject.Classification.classifiedObject",
											MessageType.Error);
									
							}
							OMAttribute nodeRepAttr = classEle
									.getAttribute(new QName("nodeRepresentation"));
							if (nodeRepAttr == null)
								errorRecorder
										.record("XDS_MSG_133",
												Constants.XDS_Metadata_Checklist,
												"ebRIM.RegistryObject.Classification.nodeRepresentation",
												MetadataType.instance.getMessageType(metadataType, "133"));
						}
					}
					Iterator exIdIter = objectRef.getChildrenWithLocalName("ExternalIdentifier");
					if (exIdIter.hasNext())
						errorRecorder.record("XDS_MSG_134",
								Constants.XDS_Metadata_Checklist,
								"ebRIM.RegistryObject.ExternalIdentifier", MetadataType.instance.getMessageType(metadataType, "134"));
					else
					{
						while(exIdIter.hasNext())
						{
							OMElement exIdEle = (OMElement) exIdIter.next();
							OMAttribute schemeAttr = exIdEle
									.getAttribute(new QName("identificationScheme"));
							if (schemeAttr == null)
								errorRecorder
										.record("XDS_MSG_135",
												Constants.XDS_Metadata_Checklist,
												"ebRIM.RegistryObject.ExternalIdentifier.identificationScheme",
												MetadataType.instance.getMessageType(metadataType, "135"));
							else
							{
								if(!ValidationUtil.isValidUUID(schemeAttr.getAttributeValue()))
									errorRecorder
									.record("XDS_MSG_135_1",
											Constants.XDS_Metadata_Checklist,
											"ebRIM.RegistryObject.ExternalIdentifier.identificationScheme",
											MessageType.Error);
							}
							OMAttribute classObjAttr = exIdEle
									.getAttribute(new QName("registryObject"));
							if (classObjAttr == null)
								errorRecorder
										.record("XDS_MSG_136",
												Constants.XDS_Metadata_Checklist,
												"ebRIM.RegistryObject.ExternalIdentifier.registryObject",
												MetadataType.instance.getMessageType(metadataType, "136"));
							else
							{
								if(lidAttr != null && !lidAttr.getAttributeValue().equals(classObjAttr.getAttributeValue()))
									errorRecorder
									.record("XDS_MSG_136_1",
											Constants.XDS_Metadata_Checklist,
											"ebRIM.RegistryObject.ExternalIdentifier.registryObject",
											MessageType.Error);
									
							}
							OMAttribute valueAttr = exIdEle
									.getAttribute(new QName("value"));
							if (valueAttr == null)
								errorRecorder
										.record("XDS_MSG_137",
												Constants.XDS_Metadata_Checklist,
												"ebRIM.RegistryObject.ExternalIdentifier.value",
												MetadataType.instance.getMessageType(metadataType, "137"));
							else
							{
								if(valueAttr.getAttributeValue() != null && valueAttr.getAttributeValue().length() > 256)
									errorRecorder
									.record("XDS_MSG_137_1",
											Constants.XDS_Metadata_Checklist,
											"ebRIM.RegistryObject.ExternalIdentifier.value",
											MessageType.Error);
									
							}
							
						}
					}
				}
			}
		}
	}

	private void validateExtrinsicObject(OMElement rolElement)
	{
		Iterator orefIter = rolElement.getChildrenWithLocalName("ExtrinsicObject");
		if (!orefIter.hasNext()) {
			errorRecorder.record("XDS_MSG_138",
					Constants.XDS_Metadata_Checklist, "ebRIM.ExtrinsicObject",
					MessageType.Info);
			return;
		} else {
			while (orefIter.hasNext()) {
				OMElement exObjElement = (OMElement) orefIter.next();
				OMAttribute obTypeAttr = exObjElement
						.getAttribute(new QName("objectType"));
				if (obTypeAttr == null)
					errorRecorder
							.record("XDS_MSG_139",
									Constants.XDS_Metadata_Checklist,
									"ebRIM.RegistryObject.ExtrinsicObject.objectType",
									MetadataType.instance.getMessageType(metadataType, "139"));
				
				OMAttribute mimeTypeAttr = exObjElement
						.getAttribute(new QName("mimeType"));
				if (mimeTypeAttr == null || mimeTypeAttr.getAttributeValue() == null)
					errorRecorder
							.record("XDS_MSG_140",
									Constants.XDS_Metadata_Checklist,
									"ebRIM.RegistryObject.ExtrinsicObject.mimeType",
									MetadataType.instance.getMessageType(metadataType, "140"));
				else
				{
					if(mimeTypeAttr.getAttributeValue().length() > 256)
						errorRecorder
						.record("XDS_MSG_140_1",
								Constants.XDS_Metadata_Checklist,
								"ebRIM.RegistryObject.ExtrinsicObject.mimeType",
								MessageType.Error);
						
				}
				OMAttribute isOpaqueAttr = exObjElement
						.getAttribute(new QName("isOpaque"));
				if (isOpaqueAttr == null || isOpaqueAttr.getAttributeValue() == null)
					errorRecorder
							.record("XDS_MSG_141",
									Constants.XDS_Metadata_Checklist,
									"ebRIM.RegistryObject.ExtrinsicObject.isOpaque",
									MetadataType.instance.getMessageType(metadataType, "141"));
				else
				{
					if(!isOpaqueAttr.getAttributeValue().equals("true") && !isOpaqueAttr.getAttributeValue().equals("false"))
						errorRecorder
						.record("XDS_MSG_141_1",
								Constants.XDS_Metadata_Checklist,
								"ebRIM.RegistryObject.ExtrinsicObject.isOpaque",
								MessageType.Error);
						
				}
				
				Iterator contVerIter = rolElement.getChildrenWithLocalName("ContentVersionInfo");
				if (contVerIter.hasNext()) 
					errorRecorder.record("XDS_MSG_142",
							Constants.XDS_Metadata_Checklist, "ebRIM.ExtrinsicObject.ContentVersionInfo",
							MetadataType.instance.getMessageType(metadataType, "142"));
			}
		}
	}

	private void validateRegistryPackage(OMElement rolElement)
	{
		Iterator orefIter = rolElement.getChildrenWithLocalName("RegistryPackage");
		if (!orefIter.hasNext()) 
			errorRecorder.record("XDS_MSG_143",
					Constants.XDS_Metadata_Checklist, "ebRIM.RegistryPackagex",
					MetadataType.instance.getMessageType(metadataType, "143"));
	}
	
	private void validateAssociation(OMElement rolElement)
	{
		Iterator orefIter = rolElement.getChildrenWithLocalName("Association");
		if (!orefIter.hasNext()) {
			errorRecorder.record("XDS_MSG_144",
					Constants.XDS_Metadata_Checklist, "ebRIM.Association",
					MetadataType.instance.getMessageType(metadataType, "144"));
			return;
		} else {
			while (orefIter.hasNext()) {
				OMElement exObjElement = (OMElement) orefIter.next();
				OMAttribute assocTypeAttr = exObjElement
						.getAttribute(new QName("associationType"));
				if (assocTypeAttr == null || assocTypeAttr.getAttributeValue() == null)
					errorRecorder
							.record("XDS_MSG_145",
									Constants.XDS_Metadata_Checklist,
									"ebRIM.RegistryObject.Association.associationType",
									MetadataType.instance.getMessageType(metadataType, "145"));
				
				OMAttribute sourceObjAttr = exObjElement
						.getAttribute(new QName("sourceObject"));
				if (sourceObjAttr == null || sourceObjAttr.getAttributeValue() == null)
					errorRecorder
							.record("XDS_MSG_146",
									Constants.XDS_Metadata_Checklist,
									"ebRIM.RegistryObject.Association.sourceObject",
									MetadataType.instance.getMessageType(metadataType, "146"));
				else
				{
					Iterator rpIter = rolElement.getChildrenWithLocalName("RegistryPackage");
					if (rpIter.hasNext()) {
						OMElement rpElement = (OMElement) rpIter.next();
						OMAttribute idAttr = rpElement.getAttribute(new QName("id"));
						if(idAttr != null && !idAttr.getAttributeValue().equals(sourceObjAttr.getAttributeValue()))
							errorRecorder.record("XDS_MSG_146_1",
									Constants.XDS_Metadata_Checklist, "ebRIM.Association.sourceObject",
									MessageType.Error);
							
					}
				}
				OMAttribute targetObjAttr = exObjElement
						.getAttribute(new QName("targetObject"));
				if (targetObjAttr == null || targetObjAttr.getAttributeValue() == null)
					errorRecorder
							.record("XDS_MSG_147",
									Constants.XDS_Metadata_Checklist,
									"ebRIM.RegistryObject.Association.targetObjAttr",
									MetadataType.instance.getMessageType(metadataType, "147"));
				else
				{
					Iterator rpIter = rolElement.getChildrenWithLocalName("ExtrinsicObject");
					if (rpIter.hasNext()) {
						OMElement rpElement = (OMElement) rpIter.next();
						OMAttribute idAttr = rpElement.getAttribute(new QName("id"));
						if(idAttr != null && !idAttr.getAttributeValue().equals(sourceObjAttr.getAttributeValue()))
							errorRecorder.record("XDS_MSG_147_1",
									Constants.XDS_Metadata_Checklist, "ebRIM.Association.targetObjAttr",
									MessageType.Error);
							
					}
				}
			}
		}
	}

	@Override
	public String getName() {
		return "EbRimValidator";
	}
}
