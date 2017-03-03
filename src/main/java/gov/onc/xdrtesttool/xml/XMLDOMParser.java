package gov.onc.xdrtesttool.xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class XMLDOMParser {
    static DocumentBuilderFactory factory = null;
    static DocumentBuilder builder = null;
    
    /** Creates a new instance of XmlUtil */
    public XMLDOMParser() {
    }
    
    static private void init()  throws ParserConfigurationException {
        if (factory == null) {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        }
    }
    
    static String getStringFromInputStream(InputStream in) throws IOException {
        int count;
        byte[] by = new byte[256];
        StringBuffer buf = new StringBuffer();
        while ( (count=in.read(by)) > 0 )
            buf.append(new String(by,0,count));
        return new String(buf);
    }
    
    static public Document parse(String xml_string) throws ParserConfigurationException, SAXException,IOException {
    	byte[] bytes = xml_string.getBytes();
    	ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	Document document = null;
    	dbFactory.setNamespaceAware(true);
    	DocumentBuilder builder = dbFactory.newDocumentBuilder();

    	document = builder.parse(bais);

    	return document;
    }
    
    static public Document newDocument() throws Exception {
    	init();
    	return builder.newDocument();
    }
    
    static public Document parse(InputStream is)  throws IOException, SAXException, ParserConfigurationException {
        init();
        try {
            //System.out.println(getStringFromInputStream(is));
            Document doc = builder.parse(is);
            return doc;
        } catch (SAXException e) {
            throw new SAXException("XML:parse: XML document failed to parse");
            
        } //catch (java.io.IOException e) {
        // throw new Exception("XML:parse: XML document failed to parse");
        //}
    }

    static public Document parse(File file) throws FileNotFoundException, Exception {
        FileInputStream is = new FileInputStream(file);
        return parse(is);
    }
    
    static public String getAttribute(String xml, String attName) throws Exception {
        int ro = xml.indexOf(attName + "=");
        if (ro == -1)
            throw new Exception("XML:getAttribute: cannot find attribute " + attName + " in " + xml);
        int start = xml.indexOf("\"", ro);
        int end = xml.indexOf("\"", start+1);
        return xml.substring(start+1, end);
    }
    
    static public String stripXMLHeader(String xml) {
        xml = xml.trim();
        
        if (xml.startsWith("<?xml")) {
            xml = xml.substring(xml.indexOf("?>")+2);
        }
        return xml;
    }
    
    static public String getTagContent(String xmlString, String tag) {
        String startTag = tag.substring(0, tag.length()-1); // skip ending >
        String endTag = "</" + tag.substring(1);
        int from = xmlString.indexOf(startTag) + startTag.length();
        from = xmlString.indexOf(">", from) + 1;
        int to = xmlString.indexOf(endTag);
        if (to == -1)
            return null;
        return xmlString.substring(from, to);
    }
    
    // does not return enclosing brackets
    static public String getRootTag(String xml) {
        int open = xml.indexOf('<');
        if (open == -1)
            return "";
        int close = xml.indexOf(' ', open);
        if (close == -1)
            return "";
        return xml.substring(open+1, close);
    }
    
    // starting at < find matching >
    // return index of next char following
    static public int elementEnd(String xmlFrag) {
        int i = xmlFrag.indexOf('>');
        if (i == -1)
            return -1;
        return i+1;
    }
    
    static public int nodeEnd(String xmlFrag) {
        String elementName = elementName(xmlFrag);
        if (elementName == null)
            return -1;   // must be valid element
        int elementEnd = elementEnd(xmlFrag);
        if (elementEnd > 2 && xmlFrag.charAt(elementEnd-1) == '>' && xmlFrag.charAt(elementEnd-2) == '/')
            return elementEnd;
        String close = "</" + elementName + ">";
        int closeI = xmlFrag.indexOf(close);
        if (closeI == -1)
            return -1;
        return closeI + close.length();
    }
    
    static public String nodeContents(String xmlFrag) {
        String elementName = elementName(xmlFrag);
        int from = xmlFrag.indexOf(">") + 1;
        if (xmlFrag.charAt(from-2) == '/')
            return null;
        String endTag = "</" + elementName + ">";
        int to = xmlFrag.indexOf(endTag);
        if (from < to)
            return xmlFrag.substring(from, to);
        return null;
    }
    
    static public String elementName(String xml)  {
        if (xml == null) return null;
        if (xml.length() < 3) return null;
        if (xml.charAt(0) != '<') return null;
        if (xml.charAt(1) == '/') return null;
        int space = xml.indexOf(' ');
        int close = xml.indexOf('>');
        int slash = xml.indexOf('/');
        int end = 1000;
        if (close == -1)
            return null;
        if (space != -1 && space < end)
            end = space;
        if (close != -1 && close < end)
            end = close;
        if (slash != -1 && slash < end)
            end = slash;
        if (end == 1000)
            return null;
        return xml.substring(1, end);
    }
}
