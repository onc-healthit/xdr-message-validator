package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;
import gov.onc.xdrtesttool.resource.MetadataType;
import org.apache.axiom.om.OMElement;

import javax.ws.rs.core.UriBuilder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExtrinsicObjectSlotValidator {

	MessageRecorder errorRecorder;
	String metadataType = MetadataType.instance.getMetadataType();
	public String getName() {
		return "ExtrinsicObjectSlotValidator";
	}

	public void validate(OMElement extrinsicObject,
			MessageRecorder errorRecorder) {
		this.errorRecorder = errorRecorder;
		validateExtrinsicObjectSlots(extrinsicObject);
	}

	private void validateExtrinsicObjectSlots(OMElement extrinsicElement) {
		validateCreationTimeSlot(extrinsicElement);
		validateHashSlot(extrinsicElement);
		validateLanguageCodeSlot(extrinsicElement);
		validateLegalAuthenticatorSlot(extrinsicElement);
		validateRepositoryUniqueIdSlot(extrinsicElement);
		validateServiceStartTimeSlot(extrinsicElement);
		validateServiceStopTimeSlot(extrinsicElement);
		validateSizeSlot(extrinsicElement);
		validateSourcePatientIdSlot(extrinsicElement);
		validateSourcePatientInfoSlot(extrinsicElement);
		validateURISlot(extrinsicElement);
	}

	// Test case Id: 153
	private void validateCreationTimeSlot(OMElement extrinsicElement) {
		if (!ValidationUtil.isSlotUnique(extrinsicElement, "creationTime"))
			errorRecorder.record("XDR_MSG_153_1", "XDS Metadata Checklist",
					"XDSDocumentEntry.creationTime", MessageType.Error);
		else {
			OMElement slot = ValidationUtil.findSlot(extrinsicElement,
					"creationTime");
			if (slot == null)
				errorRecorder.record("XDR_MSG_153", "XDS Metadata Checklist",
						"XDSDocumentEntry.creationTime", MetadataType.instance.getMessageType(metadataType, "153"));
			else
				validateCreationTime(slot, errorRecorder);
		}

	}

	private void validateLanguageCodeSlot(OMElement extrinsicElement) {
		if (!ValidationUtil.isSlotUnique(extrinsicElement, "languageCode"))
			errorRecorder.record("XDR_MSG_155_1", "XDS Metadata Checklist",
					"XDSDocumentEntry.languageCode", MessageType.Error);
		else
		{
			OMElement slot = ValidationUtil.findSlot(extrinsicElement,
					"languageCode");
			if (slot == null)
				errorRecorder.record("XDR_MSG_155", "XDS Metadata Checklist",
						"XDSDocumentEntry.languageCode", MetadataType.instance.getMessageType(metadataType, "155"));
		}

	}

	private void validateLegalAuthenticatorSlot(OMElement extrinsicElement) {
		if (!ValidationUtil
				.isSlotUnique(extrinsicElement, "legalAuthenticator"))
			errorRecorder.record("XDR_MSG_156_1", "XDS Metadata Checklist",
					"XDSDocumentEntry.legalAuthenticator", MessageType.Error);
		else
		{
			OMElement slot = ValidationUtil.findSlot(extrinsicElement,
					"languageCode");
			if (slot == null)
				errorRecorder.record("XDR_MSG_156", "XDS Metadata Checklist",
						"XDSDocumentEntry.legalAuthenticator", MetadataType.instance.getMessageType(metadataType, "156"));
		}
	}

	private void validateRepositoryUniqueIdSlot(OMElement extrinsicElement) {
		OMElement slot = ValidationUtil.findSlot(extrinsicElement,
				"repositoryUniqueId");
		if (slot != null)
			errorRecorder.record("XDR_MSG_157", "XDS Metadata Checklist",
					"XDSDocumentEntry.repositoryUniqueId", MetadataType.instance.getMessageType(metadataType, "157"));
	}

	// Test case Id: 153
	private void validateCreationTime(OMElement slot,
			MessageRecorder errorRecorder) {
		List<String> valueList = ValidationUtil.getSlotValueList(slot);
		if (valueList.size() == 0)
		{
			errorRecorder.record("XDR_MSG_153_2", "XDS Metadata Checklist",
					"XDSDocumentEntry.creationTime", MessageType.Error);
			return;
		}
		String attrValue = valueList.get(0);
		Calendar currDtCal = Calendar.getInstance();
		Date currDt = currDtCal.getTime();

		if (!ValidationUtil.validateAndCompareUTCFormat(attrValue, currDt))
			errorRecorder.record("XDR_MSG_153_2", "XDS Metadata Checklist",
					"XDSDocumentEntry.creationTime", MessageType.Error);

	}

	private void validateHashSlot(OMElement extrinsicElement) {
		// Test Case Id: 154
		if (!ValidationUtil.isSlotUnique(extrinsicElement, "hash"))
			errorRecorder.record("XDR_MSG_154_1", "XDS Metadata Checklist",
					"XDSDocumentEntry.hash", MessageType.Error);
		else {
			OMElement slot = ValidationUtil.findSlot(extrinsicElement, "hash");
			if (slot == null)
				errorRecorder.record("XDR_MSG_154", "XDS Metadata Checklist",
						"XDSDocumentEntry.hash", MetadataType.instance.getMessageType(metadataType, "154"));
			else {
				List<String> values = ValidationUtil.getSlotValueList(slot);
				String slotValue = values.get(0);
				if (slotValue == null || slotValue.length() < 40)
					errorRecorder.record("XDR_MSG_154_2",
							"XDS Metadata Checklist", "XDSDocumentEntry.hash",
							MessageType.Error);
			}
		}
	}

	// Test case Id: 158
	private void validateServiceStartTimeSlot(OMElement extrinsicObject) {
		OMElement slot = ValidationUtil.findSlot(extrinsicObject,
				"serviceStartTime");
		if (slot == null)
			errorRecorder.record("XDR_MSG_158", "XDS Metadata Checklist",
					"XDSDocumentEntry.serviceStartTime", MetadataType.instance.getMessageType(metadataType, "158"));

		List<String> valueList = ValidationUtil.getSlotValueList(slot);
		if (valueList.size() == 0)
		{
			errorRecorder.record("XDR_MSG_158", "XDS Metadata Checklist",
					"XDSDocumentEntry.serviceStartTime", MetadataType.instance.getMessageType(metadataType, "158"));
			return;
		}

		String attrValue = valueList.get(0);
		Calendar currDtCal = Calendar.getInstance();
		Date currDt = currDtCal.getTime();

		if (!ValidationUtil.validateAndCompareUTCFormat(attrValue, currDt))
			errorRecorder.record("XDR_MSG_158", "XDS Metadata Checklist",
					"XDSDocumentEntry.serviceStartTime", MetadataType.instance.getMessageType(metadataType, "158"));
	}

/*
"rim:Slot
WHERE
@name=""serviceStopTime"""

 "Verify: rim:ValueList/rim:Value:
- MUST be in HL7 V2 DTM (UTC) format
- MUST be in past
- MUST be >= serviceStartTime
Description: XDSDocumentEntry.serviceStopTime metadata attribute.
Example: ""20041225212010"""
	
 */
	private void validateServiceStopTimeSlot(OMElement extrinsicObject) {
		OMElement stopSlot = ValidationUtil.findSlot(extrinsicObject,
				"serviceStopTime");
		if (stopSlot == null)
			errorRecorder.record("XDR_MSG_159", "XDS Metadata Checklist",
					"XDSDocumentEntry.serviceStartTime", MetadataType.instance.getMessageType(metadataType, "159"));

		List<String> stopValueList = ValidationUtil.getSlotValueList(stopSlot);
		if (stopValueList.size() == 0)
			errorRecorder.record("XDR_MSG_159", "XDS Metadata Checklist",
					"XDSDocumentEntry.serviceStopTime", MetadataType.instance.getMessageType(metadataType, "159"));
		String stopValue = stopValueList.get(0);

		String stopFormat = ValidationUtil.getUTCFormat(stopValue);
		if (stopFormat == null)
			errorRecorder.record("XDR_MSG_159_1", "XDS Metadata Checklist",
					"XDSDocumentEntry.serviceStopTime", MessageType.Error);

		DateFormat stopDateFormat = new SimpleDateFormat(stopFormat);
		Date stopDate = null;
		try {
			stopDate = stopDateFormat.parse(stopValue);
		} catch (ParseException e) {
			errorRecorder.record("XDR_MSG_159_1", "XDS Metadata Checklist",
					"XDSDocumentEntry.serviceStopTime", MessageType.Error);
		}

		OMElement startSlot = ValidationUtil.findSlot(extrinsicObject,
				"serviceStartTime");
		if (startSlot == null)
			errorRecorder.record("XDR_MSG_158", "XDS Metadata Checklist",
					"XDSDocumentEntry.serviceStartTime", MetadataType.instance.getMessageType(metadataType, "158"));

		List<String> valueList = ValidationUtil.getSlotValueList(startSlot);
		if (valueList.size() == 0)
			errorRecorder.record("XDR_MSG_158_1", "XDS Metadata Checklist",
					"XDSDocumentEntry.serviceStartTime", MessageType.Error);

		String startValue = valueList.get(0);
		String startFormat = ValidationUtil.getUTCFormat(startValue);
		if (startFormat == null)
			errorRecorder.record("XDR_MSG_158_1", "XDS Metadata Checklist",
					"XDSDocumentEntry.serviceStopTime", MessageType.Error);

		DateFormat startDateFormat = new SimpleDateFormat(startFormat);
		Date startDate = null;
		try {
			startDate = startDateFormat.parse(startValue);
		} catch (ParseException e) {
			errorRecorder.record("XDR_MSG_158_1", "XDS Metadata Checklist",
					"XDSDocumentEntry.serviceStopTime", MessageType.Error);
		}

		Calendar currDtCal = Calendar.getInstance();
		Date currDt = currDtCal.getTime();
		int compare = currDt.compareTo(stopDate);
		if (compare <= 0)
			errorRecorder.record("XDR_MSG_159_2", "XDS Metadata Checklist",
					"XDSDocumentEntry.creationTime", MessageType.Error);

		compare = stopDate.compareTo(startDate);

		if (compare < 0)
			errorRecorder.record("XDR_MSG_159_3", "XDS Metadata Checklist",
					"XDSDocumentEntry.creationTime", MessageType.Error);
	}

	private void validateSizeSlot(OMElement extrinsicElement) {
		OMElement slot = ValidationUtil.findSlot(extrinsicElement, "size");
		if (slot == null)
			errorRecorder.record("XDR_MSG_160", "XDS Metadata Checklist",
					"XDSDocumentEntry.size", MetadataType.instance.getMessageType(metadataType, "160"));
		else {
			List<String> valueList = ValidationUtil.getSlotValueList(slot);
			if (valueList == null || valueList.size() == 0)
				errorRecorder.record("XDR_MSG_160_1", "XDS Metadata Checklist",
						"XDSDocumentEntry.size", MessageType.Error);
			else {
				String sizeStr = valueList.get(0);
				try {
					Integer.parseInt(sizeStr);
				} catch (NumberFormatException e) {
					errorRecorder.record("XDR_MSG_160_1",
							"XDS Metadata Checklist", "XDSDocumentEntry.size",
							MessageType.Error);
				}
			}
		}
	}

/*
"rim:Slot
WHERE
@name=""sourcePatientId"""

 "Verify: rim:ValueList/rim:Value:
- MUST be formatted in HL7 V2 CX type. This contains two parts formatted as """"ID^^^&amp;OIDofAA&amp;ISO""""
  - OIDofAA: Authority Domain Id
  - ID: An Id in the above domain
- The values above are not further verified in this context.
Description: XDSDocumentEntry.sourcePatientId metadata attribute.
Example:
<rim:ExternalIdentifier
  identificationScheme=
    """"urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446""""
  value=""""6578946^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO""""
  id=�ID_051""""
</rim:ExternalIdentifier>"

 */
	private void validateSourcePatientIdSlot(OMElement extrinsicElement) {
		OMElement slot = ValidationUtil.findSlot(extrinsicElement,
				"sourcePatientId");
		if (slot == null)
			errorRecorder.record("XDR_MSG_161", "XDS Metadata Checklist",
					"XDSDocumentEntry.sourcePatientId", MetadataType.instance.getMessageType(metadataType, "161"));
		else {
			List<String> valueList = ValidationUtil.getSlotValueList(slot);
			if (valueList == null || valueList.size() == 0)
				errorRecorder.record("XDR_MSG_161_1", "XDS Metadata Checklist",
						"XDSDocumentEntry.sourcePatientId", MessageType.Error);
			else {
				String valueStr = valueList.get(0);
				int index1 = valueStr.indexOf("^^^&");
				int index2 = valueStr.indexOf("&ISO");
				if (index1 <= 0 || (index2 <= 0 || index2 <= index1))
					errorRecorder.record("XDR_MSG_161_1",
							"XDS Metadata Checklist",
							"XDSDocumentEntry.sourcePatientId",
							MessageType.Error);
			}
		}

	}

	private void validateSourcePatientInfoSlot(OMElement extrinsicElement) {
		OMElement slot = ValidationUtil.findSlot(extrinsicElement,
				"sourcePatientInfo");
		if (slot == null)
			errorRecorder.record("XDR_MSG_162", "XDS Metadata Checklist",
					"XDSDocumentEntry.sourcePatientId", MetadataType.instance.getMessageType(metadataType, "162"));
		else {
			List<String> valueList = ValidationUtil.getSlotValueList(slot);
			if (valueList == null || valueList.size() == 0)
				errorRecorder.record("XDR_MSG_162", "XDS Metadata Checklist",
						"XDSDocumentEntry.sourcePatientId", MessageType.Error);
			else {
				for (String value : valueList) {
					String uValue = value.toUpperCase();
					if (!uValue.startsWith("PID-")
							|| uValue.startsWith("PID-2")
							|| uValue.startsWith("PID-4")
							|| uValue.startsWith("PID-12")
							|| uValue.startsWith("PID-19")) {
						errorRecorder.record("XDR_MSG_162_6",
								"XDS Metadata Checklist",
								"XDSDocumentEntry.sourcePatientId",
								MessageType.Error);
						continue;
					} else if (uValue.startsWith("PID-3|")) {
						OMElement patientIdSlot = ValidationUtil.findSlot(
								extrinsicElement, "sourcePatientId");
						String patientId = null;
						if (slot == null)
							errorRecorder.record("XDR_MSG_162_1",
									"XDS Metadata Checklist",
									"XDSDocumentEntry.sourcePatientId",
									MessageType.Error);
						else {
							List<String> pValueList = ValidationUtil
									.getSlotValueList(slot);
							if (pValueList == null || pValueList.size() == 0)
								errorRecorder.record("XDR_MSG_162_1",
										"XDS Metadata Checklist",
										"XDSDocumentEntry.sourcePatientId",
										MessageType.Error);
							else
								patientId = pValueList.get(0);
						}

						if (patientId == null)
							errorRecorder.record("XDR_MSG_162_1",
									"XDS Metadata Checklist",
									"XDSDocumentEntry.sourcePatientId",
									MessageType.Error);
						else {
							if (!uValue.equals("PID-3|" + patientId))
								errorRecorder.record("XDR_MSG_162_1",
										"XDS Metadata Checklist",
										"XDSDocumentEntry.sourcePatientId",
										MessageType.Error);
						}
					} else if (uValue.startsWith("PID-8|")) {
						if (!uValue.equals("PID-8|M")
								&& !uValue.equals("PID-8|F")
								&& !uValue.equals("PID-8|O")
								&& !uValue.equals("PID-8|U"))
							errorRecorder.record("XDR_MSG_162_3",
									"XDS Metadata Checklist",
									"XDSDocumentEntry.sourcePatientId",
									MessageType.Error);
					}

				}
			}
		}

	}

	private void validateURISlot(OMElement extrinsicElement) {
		OMElement slot = ValidationUtil.findSlot(extrinsicElement, "URI");
		if (slot == null)
			errorRecorder.record("XDR_MSG_163", "XDS Metadata Checklist",
					"XDSDocumentEntry.URI", MetadataType.instance.getMessageType(metadataType, "163"));
		else {
			List<String> valueList = ValidationUtil.getSlotValueList(slot);
			if (valueList == null || valueList.size() == 0)
				errorRecorder.record("XDR_MSG_163_1", "XDS Metadata Checklist",
						"XDSDocumentEntry.URI", MessageType.Error);
			else {
				String valueStr = valueList.get(0);
				if (valueStr.length() > 128) {
					try {
						UriBuilder.fromUri(valueStr);
					} catch (IllegalArgumentException e) {
						errorRecorder.record("XDR_MSG_163_1",
								"XDS Metadata Checklist",
								"XDSDocumentEntry.sourcePatientId",
								MessageType.Error);
					}
				}
			}
		}

	}

}
