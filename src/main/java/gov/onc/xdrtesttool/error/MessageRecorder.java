package gov.onc.xdrtesttool.error;

import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;

import java.util.List;

public interface MessageRecorder {
	public void record(String errorCode, String resource, String location, MessageType messageType);
	public void record(String errorCode, String resource, String location, MessageType messageType, Exception e);
	public List<MessageRecorderItem> getMessageErrors();
	public List<MessageRecorderItem> getMessageWarnings();
	public List<MessageRecorderItem> getMessageInfos();
}
