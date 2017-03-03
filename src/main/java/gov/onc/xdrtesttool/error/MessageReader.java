package gov.onc.xdrtesttool.error;

import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;
import gov.onc.xdrtesttool.resource.XDRMessages;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.xml.transform.StringSource;

import java.util.Iterator;
import java.util.List;

public class MessageReader {
	XDRMessageRecorder errorRecorder;
	public MessageReader(XDRMessageRecorder errorRecorder)
	{
		this.errorRecorder = errorRecorder;
	}

	public StringSource buildResponse(){
		StringBuffer sb = new StringBuffer();
		sb.append("<rs:RegistryResponse xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0 ../../schema/ebRS/rs.xsd\" ");
        List<MessageRecorderItem> errors = errorRecorder.getMessageErrors();
        List<MessageRecorderItem> warnings = errorRecorder.getMessageWarnings();
        List<MessageRecorderItem> infos = errorRecorder.getMessageInfos();
        StringBuffer errorStr = new StringBuffer();
        boolean errorFlag = false;
        boolean warningFlag = false;
        boolean infoFlag = false;
        if(errors != null && errors.size() >0)
        {
        	errorFlag = true;
        	Iterator iter = errors.iterator();
        	while(iter.hasNext())
        	{
        		MessageRecorderItem item = (MessageRecorderItem)iter.next(); 
        		errorStr.append(buildRegistryError(item));
        	}
        }

        if(warnings != null && warnings.size() > 0)
        {
        	warningFlag = true;
        	Iterator iter = warnings.iterator();
        	while(iter.hasNext())
        	{
        		MessageRecorderItem item = (MessageRecorderItem)iter.next(); 
        		errorStr.append(buildRegistryError(item));
        	}
        }

        if(infos != null && infos.size() > 0)
        {
        	infoFlag = true;
        	Iterator iter = infos.iterator();
        	while(iter.hasNext())
        	{
        		MessageRecorderItem item = (MessageRecorderItem)iter.next(); 
        		errorStr.append(buildRegistryError(item));
        	}
        }

        if(errorFlag)
        	sb.append(" status=\"urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure\"> ");
        else if(warningFlag)
        	sb.append(" status=\"urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:PartialSuccess\"> ");
        else
        	sb.append(" status=\"urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success\"> ");
        
        if(errorStr.length() > 0)
        {
        	sb.append("<rs:RegistryErrorList>");
        	sb.append(errorStr.toString());
        	sb.append("</rs:RegistryErrorList>");
        }
        sb.append("</rs:RegistryResponse>");
	    return new StringSource(sb.toString());
	}
	
	private String buildRegistryError(MessageRecorderItem item)
	{
		StringBuffer str = new StringBuffer();
		str.append("<rs:RegistryError ");
		if(item.getMessageType().equals(MessageType.Error))
			str.append("severity=\"urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error\"");	
		else if(item.getMessageType().equals(MessageType.Warning))
			str.append("severity=\"urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Warning\"");	
		else if(item.getMessageType().equals(MessageType.Info))
			str.append("severity=\"urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Info\"");	
		str.append(" errorCode=\"" + item.getErrorCode() +"\"");
		str.append(" codeContext=\"" + StringEscapeUtils.escapeXml(XDRMessages.instance.getErrorText(item.getErrorCode())) +"\"");
		str.append(" location=\"" + item.getLocation() +"\"");
		str.append(" />");
		return str.toString();
	}	
}
