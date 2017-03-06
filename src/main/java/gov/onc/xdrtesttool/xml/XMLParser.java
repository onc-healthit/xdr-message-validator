package gov.onc.xdrtesttool.xml;

import gov.onc.xdrtesttool.exception.XMLParserException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.soap.SOAPModelBuilder;
import org.springframework.ws.soap.SoapEnvelope;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class XMLParser {
	static public OMElement parseXMLFile(String filename) throws FactoryConfigurationError, XMLParserException {
		File infile = new File(filename);

		XMLStreamReader parser=null;
		try {
			parser = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(infile));
		} catch (XMLStreamException e) {
			throw new XMLParserException("gov.onc.xdrtesttool.xml: Could not create XMLStreamReader from " + filename, null);
		} catch (FileNotFoundException e) {
			throw new XMLParserException("gov.onc.xdrtesttool.xml: Could not find input file " + filename, null);
		}

		StAXOMBuilder builder = new StAXOMBuilder(parser);
		OMElement documentElement =  builder.getDocumentElement();	
		if (documentElement == null) throw new XMLParserException("gov.onc.xdrtesttool.xml: No document element", null);
		return documentElement;
	}

	static public OMElement parseXMLString(String input_string) throws XMLParserException {
		byte[] ba = input_string.getBytes();

		XMLStreamReader parser=null;

		try {
			parser = XMLInputFactory.newInstance().createXMLStreamReader(new ByteArrayInputStream(ba));
		} catch (XMLStreamException e) {
			throw new XMLParserException("gov.onc.xdrtesttool.xml: Could not create XMLStreamReader from " + "input stream", null);
		}
		StAXOMBuilder builder = new StAXOMBuilder(parser);
		OMElement documentElement =  builder.getDocumentElement();
		return documentElement;
	}

	static public OMElement parseXMLSource(Source source) throws XMLParserException {
		XMLStreamReader parser=null;

		OMElement documentElement = OMXMLBuilderFactory.createOMBuilder(source).getDocumentElement();
		//parser = XMLInputFactory.newInstance().createXMLStreamReader(source);
		//StAXOMBuilder builder = new StAXOMBuilder(parser);
		//OMElement documentElement =  builder.getDocumentElement();
		return documentElement;
	}

	static public OMElement parseXMLSource(InputStream stream){
		XMLStreamReader parser=null;

		try{
		//OMElement documentElement = OMXMLBuilderFactory.createOMBuilder(stream).getDocumentElement();
		SOAPModelBuilder builder = OMXMLBuilderFactory.createSOAPModelBuilder(stream, "UTF-8");
		OMElement documentElement = builder.getDocumentElement();
		//parser = XMLInputFactory.newInstance().createXMLStreamReader(source);
		//StAXOMBuilder builder = new StAXOMBuilder(parser);
		//OMElement documentElement =  builder.getDocumentElement();
		return documentElement;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	
	static public InputStream getEnvelopeAsInputStream(SoapEnvelope soapMsg) throws IOException, TransformerException {
		//OutputStream output = new ByteArrayOutputStream();
		//soapMsg.writeTo(output);
/*
		String outputDir = System.getProperty("java.io.tmpdir");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss.S");
		String formattedDate = sdf.format(date);

		System.out.println("outputDir " + outputDir);
		File file = new File(outputDir + File.separatorChar + "temp_"+formattedDate+".xml");
		OutputStream  ofile = new FileOutputStream(file);
		soapMsg.writeTo(ofile);
		ofile.close();
*/	
		//InputStream input = new ByteArrayInputStream(((ByteArrayOutputStream) output).toByteArray());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Source xmlSource = soapMsg.getSource();
		Result outputTarget = new StreamResult(outputStream);
		TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
		return new ByteArrayInputStream(outputStream.toByteArray());
	}
}
