package gov.onc.xdrtesttool.error;

import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class XDRMessageRecorder implements MessageRecorder {

	private List<MessageRecorderItem> errorMessages = new ArrayList<MessageRecorderItem>();
	private List<MessageRecorderItem> warningMessages = new ArrayList<MessageRecorderItem>();
	private List<MessageRecorderItem> infoMessages = new ArrayList<MessageRecorderItem>();
	private boolean hasErrors = false;
	private boolean hasWarnings = false;
	private boolean hasInfos = false;
	public XDRMessageRecorder()
	{
	}
	
	public boolean isHasErrors() {
		return hasErrors;
	}

	public boolean isHasWarnings() {
		return hasWarnings;
	}

	public boolean isHasInfos() {
		return hasInfos;
	}

	private void recordError(MessageRecorderItem recItem, Exception e) {
		recItem.setMessageType(MessageRecorderItem.MessageType.Error);
		if(e != null)
			recItem.setExtraMessage(getExceptionDetails(e, null));
		errorMessages.add(recItem);
		hasErrors = true;
	}

	private void recordWarning(MessageRecorderItem recItem) {
		recItem.setMessageType(MessageRecorderItem.MessageType.Warning);
		warningMessages.add(recItem);
		hasWarnings = true;
	}
	
	private void recordInfo(MessageRecorderItem recItem) {
		recItem.setMessageType(MessageRecorderItem.MessageType.Info);
		infoMessages.add(recItem);
		hasInfos = true;
	}

	public void record(String errorCode, String resource, String location, MessageType messageType, Exception e)
	{
		MessageRecorderItem item = new MessageRecorderItem(errorCode, resource, location, messageType);
		if(messageType.equals(MessageType.Error))
			recordError(item, e);
		else if(messageType.equals(MessageType.Warning))
			recordWarning(item);
		else if(messageType.equals(MessageType.Info))
			recordInfo(item);
	}
	public void record(String errorCode, String resource, String location, MessageType messageType)
	{
		record(errorCode, resource, location, messageType, null);
	}
	
	private String getExceptionDetails(Exception e, String message) {
		if (e == null)
			return "No stack trace available";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);

		String emessage = e.getMessage();
		if (emessage == null || emessage.equals(""))
			emessage = "No Message";

		return ("Exception thrown: " + e.getClass().getName() + "\n" + 
		((message != null) ? message + "\n" : "") +
		emessage + "\n" + new String(baos.toByteArray()));
	}

	public List<MessageRecorderItem> getMessageErrors() {
		return errorMessages;
	}

	public List<MessageRecorderItem> getMessageWarnings() {
		return warningMessages;
	}

	public List<MessageRecorderItem> getMessageInfos() {
		return infoMessages;
	}
}
