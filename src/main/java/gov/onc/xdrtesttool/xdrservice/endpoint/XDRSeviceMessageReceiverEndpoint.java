package gov.onc.xdrtesttool.xdrservice.endpoint;

import gov.onc.xdrtesttool.entities.MessageLog;
import gov.onc.xdrtesttool.error.MessageReader;
import gov.onc.xdrtesttool.error.XDRMessageRecorder;
import gov.onc.xdrtesttool.services.MessageLogService;
import gov.onc.xdrtesttool.validate.XDRValidator;
import gov.onc.xdrtesttool.xml.XMLParser;
import org.apache.axiom.om.OMElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;
import org.springframework.xml.transform.StringSource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriBuilder;
import javax.xml.transform.Source;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.SOAPBinding;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Endpoint(value = SOAPBinding.SOAP12HTTP_MTOM_BINDING)
@Addressing(enabled=true, required=true)
public class XDRSeviceMessageReceiverEndpoint extends SpringBeanAutowiringSupport{
	private final Logger log = LoggerFactory.getLogger(this.getClass().toString());
	private static final String NAMESPACE_RIM_URI = "urn:ihe:iti:xds-b:2007";
	public List<XDRValidator> validators = new ArrayList<XDRValidator>();
	private Source response;
	@Autowired
	private MessageLogService messageLogService;

	public List<XDRValidator> getValidators() {
		return validators;
	}

	public void setValidators(List<XDRValidator> validators) {
		this.validators = validators;
	}

	public @ResponsePayload Source handleProvideAndRegisterDocumentSetRequest(
			@RequestPayload Source source, MessageContext messageContext)
			throws Exception {
		SoapMessage soapMessage = (SoapMessage) messageContext.getRequest();
		MessageLog messageLog = new MessageLog();

		XDRMessageRecorder errorRecorder = new XDRMessageRecorder();

		if (validators != null && validators.size() > 0) {
			for (XDRValidator validator : validators) {
				log.info("Name: " + validator.getName());
				validator.validate(soapMessage, errorRecorder, null);
			}
		}
		MessageReader reader = new MessageReader(errorRecorder);
		StringSource responseSource = reader.buildResponse();
		response = responseSource;

		messageLog.setFromAddress(this.getFromAddress(soapMessage));
		messageLog.setIpAddress(this.geIpAddress());
		messageLog.setRequest(this.getSoapRequest(soapMessage));
		messageLog.setResponse(responseSource.toString());
		messageLog.setDateLogged(new Date().toString());
		messageLogService.saveLog(messageLog);
		return responseSource;
	}

	private String getSoapRequest(SoapMessage soapMessage)  {
		String response = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			soapMessage.writeTo(out);
			response = out.toString("UTF-8");
		} catch (Exception e) {
			response = "Error with attachment - request could not be saved.";
		}finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}

	private String geIpAddress(){
		TransportContext context = TransportContextHolder.getTransportContext();
		HttpServletConnection connection = (HttpServletConnection )context.getConnection();
		HttpServletRequest request = connection.getHttpServletRequest();
		return request.getRemoteAddr();
	}

	private String getFromAddress(SoapMessage soapMessage) {
		try {
			OMElement element = XMLParser.parseXMLSource(XMLParser
					.getEnvelopeAsInputStream(soapMessage.getEnvelope()));
			if(element == null) {
				log.error("Invalid SOAP Request. Unable to parse.");
				return null;
			}

			OMElement header = null;
			Iterator headerIter = element.getChildrenWithLocalName("Header");
			if (!headerIter.hasNext()) {
				log.error("Header is missing from the request");
				return null;
			} else
				header = (OMElement) headerIter.next();

			if(header == null) {
				log.error("Header is missing from the request");
				return null;
			} else {
				Iterator addressIter = header.getChildrenWithLocalName("addressBlock");
				if(!addressIter.hasNext()) {
					log.error("Address is missing from the Header");
					return null;
				} else {
					//S:Envelope/S:Header/direct:AddressBlock - Optional
					OMElement addressElement = (OMElement)addressIter.next();

					Iterator fromIter = addressElement.getChildrenWithLocalName("from");
					if(!fromIter.hasNext()) {
						log.error("Address from is missing from the Header");
						return null;
					} else {
						OMElement fromElement = (OMElement) fromIter.next();
						String fromAddr = null;
						try {
							UriBuilder.fromUri(fromElement.getText());
							fromAddr = fromElement.getText();
						} catch(IllegalArgumentException e) {
							log.error("Invalid from Address in the Header");
							return null;
						}
						return fromAddr;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			log.error("Failed to get from address from SOAP Header");
		}
		return null;
	}
}
