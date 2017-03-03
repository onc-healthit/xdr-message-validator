package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorder;
import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;
import gov.onc.xdrtesttool.exception.XSDParserException;
import gov.onc.xdrtesttool.resource.ResourceManager;
import gov.onc.xdrtesttool.resource.XDRMessages;
import gov.onc.xdrtesttool.xml.NamespaceManager;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.springframework.ws.soap.SoapMessage;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ValidationUtil {

	public static boolean validateUTCFormat(String attrValue)
	{
		if(attrValue.length() % 2 != 0)
			return false;

		String format = getUTCFormat(attrValue);
		if(format == null)
			return false;
		
		return true;
	}

	public static String getUTCFormat(String attrValue)
	{
		if(attrValue.length() % 2 != 0)
			return null;
		
		String format = null;
		if(attrValue.length() == 4)
			format = "yyyy";
		else if(attrValue.length() == 6)
			format = "yyyyMM";
		else if(attrValue.length() == 8)
			format = "yyyyMMdd";
		else if(attrValue.length() == 10)
			format = "yyyyMMddhh";
		else if(attrValue.length() == 12)
			format = "yyyyMMddhhmm";
		else if(attrValue.length() == 14)
			format = "yyyyMMddhhmmss";
		return format;
	}

	public static boolean validateAndCompareUTCFormat(String attrValue, Date compareTo)
	{
		String format = getUTCFormat(attrValue);
		if(format == null)
			return false;
		
		DateFormat dtFormat = new SimpleDateFormat(format);
		Date creationDt;
		try {
			creationDt = dtFormat.parse(attrValue);
		} catch (ParseException e) {
			return false;
		} 
		int compare = compareTo.compareTo(creationDt);
		if(compare <= 0)
			return false;
		return true;
	}
	
	public static List<String> getSlotValueList(OMElement slotEle)
	{
		if(slotEle == null)
			return null;
		
		List<String> valueList = new ArrayList<String>();
		Iterator iter = slotEle.getChildrenWithLocalName("ValueList");
		if(iter.hasNext())
		{
			OMElement valueListElement = (OMElement)iter.next();
			Iterator valuesIter = valueListElement.getChildrenWithLocalName("Value");
			while(valuesIter.hasNext())
			{
				OMElement value = (OMElement)valuesIter.next();
				valueList.add(value.getText());
			}
		}
		return valueList;
	}
	
	public static OMElement findSlot(OMElement parentObject, String slotName)
	{
		Iterator exChildElements = parentObject.getChildrenWithLocalName("Slot");
		
		while(exChildElements.hasNext())
		{
			OMElement slot = (OMElement) exChildElements.next();
			OMAttribute nameAttr = slot.getAttribute(new QName("name"));
			if(nameAttr == null)
				return null;
			
			String attrValue = nameAttr.getAttributeValue();
			//Test case ID: 153
			if(attrValue.equals(slotName))
				return slot;
		}
		return null;
	}
	
	public static boolean isSlotUnique(OMElement extrinsicObject, String slotName)
	{
		Iterator exChildElements = extrinsicObject.getChildrenWithLocalName("Slot");
		boolean found = false;
		
		while(exChildElements.hasNext())
		{
			OMElement slot = (OMElement) exChildElements.next();
			OMAttribute nameAttr = slot.getAttribute(new QName("name"));
			String attrValue = nameAttr.getAttributeValue();
			if(attrValue.equals(slotName))
			{
				if(found)
					return false;
				else
					found = true;
			}
		}
		return true;
	}
	
	public static OMElement findClassificationByScheme(OMElement extrinsicObject, String schemeValue)
	{
		Iterator clChildElements = extrinsicObject.getChildrenWithLocalName("Classification");
		
		while(clChildElements.hasNext())
		{
			OMElement classElement = (OMElement) clChildElements.next();
			OMAttribute nameAttr = classElement.getAttribute(new QName("classificationScheme"));
			if(nameAttr == null)
				return null;
			
			String attrValue = nameAttr.getAttributeValue();
			if(attrValue.equals(schemeValue))
				return classElement;
		}
		return null;
	}	
	
	public static OMElement findClassificationByNode(OMElement extrinsicObject, String nodeValue)
	{
		Iterator clChildElements = extrinsicObject.getChildrenWithLocalName("Classification");
		
		while(clChildElements.hasNext())
		{
			OMElement classElement = (OMElement) clChildElements.next();
			OMAttribute nameAttr = classElement.getAttribute(new QName("classificationNode"));
			if(nameAttr == null)
				return null;
			
			String attrValue = nameAttr.getAttributeValue();
			if(attrValue.equals(nodeValue))
				return classElement;
		}
		return null;
	}	


	public static boolean isValidHITSP_C80_20_Table2_144conceptCode(String code)
	{
		List<String> codes = ResourceManager.instance.getHITSP_C80_20_Table2_144conceptCodes();
		Iterator iter = codes.iterator();
		while(iter.hasNext())
		{
			String value = (String) iter.next();
			if(value.equals(code))
				return true;
		}
		return false;
	}

	public static boolean isValidHITSP_C80_20_Table2_151conceptCode(String code)
	{
		List<String> codes = ResourceManager.instance.getHITSP_C80_20_Table2_151conceptCodes();
		Iterator iter = codes.iterator();
		while(iter.hasNext())
		{
			String value = (String) iter.next();
			if(value.equals(code))
				return true;
		}
		return false;
	}

	public static boolean isValidHITSP_C80_20_Table2_153conceptCode(String code)
	{
		List<String> codes = ResourceManager.instance.getHITSP_C80_20_Table2_153conceptCodes();
		Iterator iter = codes.iterator();
		while(iter.hasNext())
		{
			String value = (String) iter.next();
			if(value.equals(code))
				return true;
		}
		return false;
	}

	public static boolean isValidHITSP_C80_20_Table2_147conceptCode(String code)
	{
		List<String> codes = ResourceManager.instance.getHITSP_C80_20_Table2_147conceptCodes();
		Iterator iter = codes.iterator();
		while(iter.hasNext())
		{
			String value = (String) iter.next();
			if(value.equals(code))
				return true;
		}
		return false;
	}
	
	public static boolean isValidHITSP_C80_20_Table2_147conceptName(String code)
	{
		List<String> codes = ResourceManager.instance.getHITSP_C80_20_Table2_147conceptNames();
		Iterator iter = codes.iterator();
		while(iter.hasNext())
		{
			String value = (String) iter.next();
			if(value.equals(code))
				return true;
		}
		return false;
	}

	public static boolean isValidHITSP_C80_20_Table2_149conceptName(String code)
	{
		List<String> codes = ResourceManager.instance.getHITSP_C80_20_Table2_149conceptNames();
		Iterator iter = codes.iterator();
		while(iter.hasNext())
		{
			String value = (String) iter.next();
			if(value.equals(code))
				return true;
		}
		return false;
	}

	public static boolean isValidSNOMEDCTConceptId(String code)
	{
		List<String> codes = ResourceManager.instance.getSNOMEDCTConceptIds();
		Iterator iter = codes.iterator();
		while(iter.hasNext())
		{
			String value = (String) iter.next();
			if(value.equals(code))
				return true;
		}
		return false;
	}
	
	public static boolean isValidConceptCode(String code)
	{
		List<String> codes = ResourceManager.instance.getConceptCodes();
		Iterator iter = codes.iterator();
		while(iter.hasNext())
		{
			String value = (String) iter.next();
			if(value.equals(code))
				return true;
		}
		return false;
	}
	
	public static boolean isValidConceptNameSpecialityArea(String code)
	{
		List<String> codes = ResourceManager.instance.getConceptNameSpecialityAreas();
		Iterator iter = codes.iterator();
		while(iter.hasNext())
		{
			String value = (String) iter.next();
			if(value.equals(code))
				return true;
		}
		return false;
	}
	
	public static OMElement findExternalIdentifierByScheme(OMElement extrinsicObject, String schemeValue)
	{
		Iterator clChildElements = extrinsicObject.getChildrenWithLocalName("ExternalIdentifier");
		
		while(clChildElements.hasNext())
		{
			OMElement classElement = (OMElement) clChildElements.next();
			OMAttribute nameAttr = classElement.getAttribute(new QName("identificationScheme"));
			String attrValue = nameAttr.getAttributeValue();
			if(attrValue.equals(schemeValue))
				return classElement;
		}
		return null;
	}
	
	public static OMElement findClassificationChildElement(OMElement element,
			String classificationNode) {
		Iterator classIter = element.getChildrenWithLocalName("Classification");
		if (!classIter.hasNext())
			return null;
		else {
			while (classIter.hasNext()) {
				OMElement classEle = (OMElement) classIter.next();
				OMAttribute attr = classEle.getAttribute(new QName(
						"classificationNode"));
				if (attr != null
						&& attr.getAttributeValue().equals(classificationNode))
					return classEle;
			}
		}
		return null;
	}
	
	public static void validateRegistryPackageName(OMElement registryPackageElement, MessageRecorder errorRecorder, String meesageId, String location) {
		Iterator nameIter = registryPackageElement.getChildrenWithLocalName("Name");
		if (nameIter.hasNext()) {
			OMElement nameElement = (OMElement) nameIter.next();
			Iterator localIter = nameElement
					.getChildrenWithLocalName("LocalizedString");
			if (localIter.hasNext()) {
				OMElement local = (OMElement) localIter.next();
				OMAttribute attribute = local.getAttribute(new QName("value"));
				String attrValue = attribute.getAttributeValue();
				if (attrValue == null)
					errorRecorder.record(meesageId,
							"XDS Metadata Checklist", location,
							MessageType.Error);
				else {
					try {
						if (attrValue.getBytes("UTF-8").length > 128)
							errorRecorder.record(meesageId,
									"XDS Metadata Checklist", location,
									MessageType.Error);
					} catch (UnsupportedEncodingException e) {
						errorRecorder.record(meesageId,
								"XDS Metadata Checklist", location,
								MessageType.Error);
					}
				}
			} else {
				errorRecorder.record(meesageId,
						"XDS Metadata Checklist", location,
						MessageType.Error);
			}
		} else {
			errorRecorder.record(meesageId,
					"XDS Metadata Checklist", location,
					MessageType.Info);
		}
	}

	public static void validateRegistryPackageDescription(OMElement registryPackageElement, MessageRecorder errorRecorder, String meesageId, String location) {
		Iterator nameIter = registryPackageElement
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
					errorRecorder.record(meesageId,
							"XDS Metadata Checklist", location,
							MessageType.Error);
			} else {
				errorRecorder.record(meesageId,
						"XDS Metadata Checklist", location,
						MessageType.Error);
			}
		} else {
			errorRecorder.record(meesageId,
					"XDS Metadata Checklist", location,
					MessageType.Info);
		}
	}		
	
	////@classifiedObject = the value of rim:ExtrinsicObject/@id
	public static void validateClassifiedObject(OMElement extrinsicObject, OMElement classElement, MessageRecorder errorRecorder, String errorCode, String elementName)
	{
		OMAttribute attr1 = classElement.getAttribute(new QName("classifiedObject"));
		if(attr1 == null)
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSSubmissionSet."+elementName, MessageType.Error);
		else
		{
			OMAttribute eAttr = extrinsicObject.getAttribute(new QName("id"));
			if(eAttr == null || !eAttr.getAttributeValue().equals(attr1.getAttributeValue()))
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSSubmissionSet."+elementName, MessageType.Error);
		}
		
	}	
	
	//There MUST be a Slot element WHERE @name="codingScheme" AND rim:Slot/rim:ValueList/rim:Value is present
	public static void validateCodingScheme(OMElement classElement, String errorCode, String elementName, MessageRecorder errorRecorder)
	{
		OMElement slot1 = ValidationUtil.findSlot(classElement, "codingScheme");
		if(slot1 == null)
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSSubmissionSet."+elementName, MessageType.Error);
		else
		{
			List<String> values = ValidationUtil.getSlotValueList(slot1);
			if(values == null || values.size() == 0)
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSSubmissionSet."+elementName, MessageType.Error);
		}
	}

	//rim:Name/rim:LocalizedString/@value MUST be present
	public static void validateLocalizedString(OMElement classElement, String errorCode, String elementName, MessageRecorder errorRecorder)
	{
		Iterator nameElementIter = classElement.getChildrenWithLocalName("Name");
		if(!nameElementIter.hasNext())
			errorRecorder.record(errorCode, "XDS Metadata Checklist",
					"XDSSubmissionSet."+elementName, MessageType.Error);
		else
		{
			OMElement nameElement = (OMElement) nameElementIter.next();
			Iterator localIter = classElement.getChildrenWithLocalName("LocalizedString");
			if(!localIter.hasNext())
				errorRecorder.record(errorCode, "XDS Metadata Checklist",
						"XDSSubmissionSet."+elementName, MessageType.Error);
			else
			{
				OMElement valueElement = (OMElement)localIter.next();
				OMAttribute attr = valueElement.getAttribute(new QName("value"));
				if(attr == null || attr.getAttributeValue() == null)
					errorRecorder.record(errorCode, "XDS Metadata Checklist",
							"XDSSubmissionSet."+elementName, MessageType.Error);
			}
			
		}
	}
	
	public static OMElement findRegistryPackageById(OMElement registryObjectList, String id)
	{
		if(registryObjectList == null)
			return null;
		
		Iterator iter = registryObjectList.getChildrenWithLocalName("RegistryPackage");
		while(iter.hasNext())
		{
			OMElement element = (OMElement) iter.next();
			OMAttribute attr = element.getAttribute(new QName("id"));
			if(attr == null)
				continue;
			if(attr.getAttributeValue().equals(id))
				return element;
		}
		return null;
	}

	public static OMElement findExtrinsicObjectById(OMElement registryObjectList, String id)
	{
		if(registryObjectList == null)
			return null;
		
		Iterator iter = registryObjectList.getChildrenWithLocalName("ExtrinsicObject");
		while(iter.hasNext())
		{
			OMElement element = (OMElement) iter.next();
			OMAttribute attr = element.getAttribute(new QName("id"));
			if(attr == null)
				continue;
			if(attr.getAttributeValue().equals(id))
				return element;
		}
		return null;
	}
	
	public static OMElement findObjectRefById(OMElement registryObjectList, String id)
	{
		if(registryObjectList == null)
			return null;
		
		Iterator iter = registryObjectList.getChildrenWithLocalName("ObjectRef");
		while(iter.hasNext())
		{
			OMElement element = (OMElement) iter.next();
			OMAttribute attr = element.getAttribute(new QName("id"));
			if(attr == null)
				continue;
			if(attr.getAttributeValue().equals(id))
				return element;
		}
		return null;
	}
	
	
    public static boolean validateXML(String xsdPath, Source xml, MessageRecorder errorRecorder){
        
        try {
            SchemaFactory factory = 
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream xsdStream = ValidationUtil.class.getClassLoader().getResourceAsStream(xsdPath);	
        	factory.setResourceResolver(new ResourceResolver());
        	StreamSource xsdSource = new StreamSource(xsdStream);
            Schema schema = factory.newSchema(xsdSource);
           
            Validator validator = schema.newValidator();
            validator.validate(xml);
        } catch (IOException e) {
        	errorRecorder.record("XML_VALIDATION_WITH_XSD", xml.getSystemId(), xsdPath, MessageType.Error, e);
            return false;
	    } catch (SAXException e) {
        	errorRecorder.record("XML_VALIDATION_WITH_XSD", xml.getSystemId(), xsdPath, MessageType.Error, e);
	        return false;
	    }
        return true;
    }	
    
	public static void validateSchema(SoapMessage soapMsg, MessageRecorder errorRecorder)
	{
		Source source = soapMsg.getPayloadSource();
		validateXML("gov/onc/xdrtesttool/schema/IHE/XDS.b_DocumentRepository.xsd", source, errorRecorder);
	}
	
	public static void validateNamespaces(OMElement element, MessageRecorder errorRecorder)
	{
		if(element == null)
			return;
		
		OMNamespace ns = element.getNamespace();
		if(ns == null)
			errorRecorder.record("INVALID_NS", element.getLocalName(), element.getLineNumber()+"", MessageType.Error);
		boolean valid = true;
		try {
			valid = NamespaceManager.instance.isValidNS(element.getLocalName(), ns.getNamespaceURI());
		} catch (XSDParserException e) {
			e.printStackTrace();
		}
		if(!valid)
		{
			errorRecorder.record("INVALID_NS", element.getLocalName(), element.getLineNumber()+"", MessageType.Error);
		}

		Iterator children = element.getChildElements();
		while(children.hasNext())
		{
			OMElement child = (OMElement)children.next();
			validateNamespaces(child, errorRecorder);
		}
	}
	
	public static boolean isValidUUID(String uuId)
	{
		if (!uuId.startsWith("urn:uuid"))
			return false;
		else {
			try {
				UUID.fromString(uuId.substring(uuId
						.indexOf("urn:uuid:") + 1));
			} catch (IllegalArgumentException e) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isValidXTN(String xtnValue)
	{
		if(xtnValue == null || xtnValue.length() == 0)
			return false;
		
		int index = xtnValue.indexOf("^^Internet^");
		if(index < 0)
			return false;
		
		String email = xtnValue.substring(index + 11);
		if(email == null || email.length() == 0)
			return false;
		
		if(email.lastIndexOf("^") > 0)
			email = email.substring(0, email.lastIndexOf("^"));
		return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email);
	}
	
	public static LinkedList<String> getListFromFile(String fileName) throws FileNotFoundException, IOException
	{
    	InputStream input = null;
    	BufferedReader reader = null;
    	LinkedList<String> list = new LinkedList();
    	try {
    		input = XDRMessages.class.getClassLoader().getResourceAsStream(fileName);
    		if(input==null){
    	            System.out.println("Failed to load message text properties "+ fileName +" from the classpath");
    		    return null;
    		}
    		reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            while(line != null){
            	list.add(line);
                line = reader.readLine();
            }           
 
        } catch (FileNotFoundException ex) {
        	ex.printStackTrace();
        	throw ex;
        } catch (IOException ex) {
    		ex.printStackTrace();
    		throw ex;
        } finally {
            try {
                if(reader != null)
                	reader.close();
                if(input != null)
                	input.close();
            } catch (IOException ex) {
            	ex.printStackTrace();
            	throw ex;
            }
        }

    	return list;
	}
	
}
