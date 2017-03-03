package gov.onc.xdrtesttool.validate;

import gov.onc.xdrtesttool.error.MessageRecorderItem;

import java.util.List;

public class ValidationStepResult {
	public String stepName;
	public List<MessageRecorderItem> er;
	
	public ValidationStepResult() {} // For GWT
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		for (MessageRecorderItem item : er)
			buf.append(item).append("\n");
		return buf.toString();
	}
	
}
