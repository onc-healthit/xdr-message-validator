package gov.onc.xdrtesttool.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ws.transport.TransportInputStream;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

public class XdrSoapProtocolChooser implements SoapProtocolChooser {

	private static final Log logger = LogFactory
			.getLog(XdrSoapProtocolChooser.class);
	private static final Pattern userAgentPattern = Pattern.compile("html",
			Pattern.CASE_INSENSITIVE);

	public boolean useSoap11(TransportInputStream transportInputStream)
			throws IOException {
		for (Iterator headerNames = transportInputStream.getHeaderNames(); headerNames
				.hasNext();) {
			String headerName = (String) headerNames.next();
			logger.debug("found headerName: " + headerName);
			for (Iterator headerValues = transportInputStream
					.getHeaders(headerName); headerValues.hasNext();) {
				String headerValue = (String) headerValues.next();
				logger.debug("     headerValue? " + headerValue);
				// Something weird with case names
				/*
				 * if (headerName.toLowerCase().contains("user-agent")) {
				 * System.out.println("UserAgent  - " + headerValue); Matcher m
				 * = userAgentPattern.matcher(headerValue); if (m.find()) {
				 * logger.debug("Found AXIS in header.  Using SOAP 1.1"); return
				 * true; } }
				 */
				// This is the code written in order to support multiple
				// Endpints by selection of SOAP
				if (headerName.toLowerCase().contains("content-type")) {
					logger.debug("Content Type  - " + headerValue);

					if (headerValue.trim().toLowerCase().contains("text/xml")) {
						logger.debug("Found text/xml in header.  Using SOAP 1.1");
						return true;
					}

				}
			}
		}
		return false;
	}
}
