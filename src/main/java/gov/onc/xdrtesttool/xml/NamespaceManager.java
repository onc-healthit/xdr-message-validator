package gov.onc.xdrtesttool.xml;

import gov.onc.xdrtesttool.exception.XSDParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NamespaceManager {
	public static final String NS_RS = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0";
	public static final String NS_LCM = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0";
	public static final String NS_RIM = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0";
	public static final String NS_XOP = "http://www.w3.org/2004/08/xop/include";
	public static final String NS_XDSB = "urn:ihe:iti:xds-b:2007";
	public static final String NS_DIRECT = "urn:direct:addressing";
	public static final String NS_WSA = "http://www.w3.org/2005/08/addressing";
	public static final String NS_S = "http://www.w3.org/2003/05/soap-envelope";
	public static final String NS_QUERY = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0";
	public static final String NS_CMS = "urn:oasis:names:tc:ebxml-regrep:xsd:cms:3.0";

	private List<String> NS_S_List;
	private List<String> NS_WSA_List;
	private List<String> NS_DIRECT_List;
	private List<String> NS_XDSB_List;
	private List<String> NS_XOP_List;
	private List<String> NS_RIM_List;
	private List<String> NS_LCM_List;
	private List<String> NS_RS_List;
	private List<String> NS_QUERY_List;
	private List<String> NS_CMS_List;

	public static final NamespaceManager instance = new NamespaceManager();
	private static boolean loaded = false;
	
	private NamespaceManager() {
		NS_S_List = new ArrayList<String>();
		NS_WSA_List = new ArrayList<String>();
		NS_DIRECT_List = new ArrayList<String>();
		NS_XDSB_List = new ArrayList<String>();
		NS_XOP_List = new ArrayList<String>();
		NS_RIM_List = new ArrayList<String>();
		NS_LCM_List = new ArrayList<String>();
		NS_RS_List = new ArrayList<String>();
		NS_QUERY_List = new ArrayList<String>();
		NS_CMS_List = new ArrayList<String>();
	}

	private void loadNamespaceElementsFromSchema() throws XSDParserException{
		NS_S_List.addAll(load("gov/onc/xdrtesttool/schema/ws-soap.xsd"));
		NS_WSA_List.addAll(load("gov/onc/xdrtesttool/schema/ws-addr.xsd"));
		NS_DIRECT_List.add("addressBlock");
		NS_DIRECT_List.add("metadata-level");
		NS_DIRECT_List.add("from");
		NS_DIRECT_List.add("to");
		NS_XDSB_List
				.addAll(load("gov/onc/xdrtesttool/schema/IHE/XDS.b_DocumentRepository.xsd"));
		NS_XOP_List.add("Include");
		NS_RIM_List.addAll(load("gov/onc/xdrtesttool/schema/ebRS/rim.xsd"));
		NS_LCM_List.addAll(load("gov/onc/xdrtesttool/schema/ebRS/lcm.xsd"));
		NS_RS_List.addAll(load("gov/onc/xdrtesttool/schema/ebRS/rs.xsd"));
		NS_QUERY_List.addAll(load("gov/onc/xdrtesttool/schema/ebRS/query.xsd"));
		NS_CMS_List.addAll(load("gov/onc/xdrtesttool/schema/ebRS/cms.xsd"));
	}

	private  List<String> load(String xsdFile) throws XSDParserException{
		List<String> elementNames = new ArrayList<String>();
		InputStream xsdStream = null;
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			 xsdStream = NamespaceManager.class.getClassLoader()
					.getResourceAsStream(xsdFile);
			if(xsdStream == null)
				throw new XSDParserException("Failed reading XSD: " + xsdFile, xsdFile);
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(xsdStream);
			NodeList list = doc.getElementsByTagNameNS("*","*");

			// loop to print data
			for (int i = 0; i < list.getLength(); i++) {
				Element first = (Element) list.item(i);
				String localName = first.getNodeName();
				String elementName = localName;
				if(localName != null && localName.contains(":"))
					elementName = localName.substring(localName.indexOf(":")+1);

				if(elementName != null && elementName.equals("element"))
						elementNames.add(first.getAttribute("name"));
			}
		} catch (ParserConfigurationException e) {
			System.out.println("Failed reading XSD: " + xsdFile + ": "
					+ e.getMessage());
			throw new XSDParserException("Failed reading XSD: " + xsdFile + ": "
					+ e.getMessage(), xsdFile, e);
		} catch (SAXException e) {
			System.out.println("Failed reading XSD: " + xsdFile + ": "
					+ e.getMessage());
			throw new XSDParserException("Failed reading XSD: " + xsdFile + ": "
					+ e.getMessage(), xsdFile, e);
		} catch (IOException e) {
			System.out.println("Failed reading XSD: " + xsdFile + ": "
					+ e.getMessage());
			throw new XSDParserException("Failed reading XSD: " + xsdFile + ": "
					+ e.getMessage(), xsdFile, e);
		} catch (Exception e) {
			System.out.println("Failed reading XSD: " + xsdFile + ": "
					+ e.getMessage());
			throw new XSDParserException("Failed reading XSD: " + xsdFile + ": "
					+ e.getMessage(), xsdFile, e);
		}
		finally
		{
			if(xsdStream != null)
			{
				try {
					xsdStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return elementNames;
	}

	public boolean isValidNS(String elementName, String namespace) throws XSDParserException{
		if(!loaded)
		{
			loadNamespaceElementsFromSchema();
			loaded = true;
		}
		
		if(elementName.equals("Envelope") || elementName.equals("Header") || elementName.equals("Body"))
			return namespace.equals("http://schemas.xmlsoap.org/soap/envelope/");
		if (NS_RS.equals(namespace))
			return NS_RS_List.contains(elementName);
		else if (NS_S.equals(namespace))
			return NS_S_List.contains(elementName);
		else if (NS_WSA.equals(namespace))
			return NS_WSA_List.contains(elementName);
		else if (NS_DIRECT.equals(namespace))
			return NS_DIRECT_List.contains(elementName);
		else if (NS_XDSB.equals(namespace))
			return NS_XDSB_List.contains(elementName);
		else if (NS_XOP.equals(namespace))
			return NS_XOP_List.contains(elementName);
		else if (NS_RIM.equals(namespace))
			return NS_RIM_List.contains(elementName);
		else if (NS_LCM.equals(namespace))
			return NS_LCM_List.contains(elementName);
		else if (NS_QUERY.equals(namespace))
			return NS_QUERY_List.contains(elementName);
		else if (NS_CMS.equals(namespace))
			return NS_CMS_List.contains(elementName);
		else
			return false;
	}
}
