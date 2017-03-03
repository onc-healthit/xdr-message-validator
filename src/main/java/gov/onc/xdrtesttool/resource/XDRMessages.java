package gov.onc.xdrtesttool.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class XDRMessages {
	public static final XDRMessages instance = new XDRMessages();
	Properties textProps = new Properties();
	Properties specProps = new Properties();
	Properties refProps = new Properties();
	Properties messageTypes = new Properties();
	
	private XDRMessages()
	{
		loadProperties(textProps, "XDRRecorderTextMessages.properties");
		loadProperties(specProps, "XDRRecorderMessageSpecs.properties");
		loadProperties(refProps, "XDRRecorderMessageReferences.properties");
		loadProperties(messageTypes, "XDRValidationTypes.properties");
	}
	
	public String getErrorText(String eCode)
	{
		return textProps.getProperty(eCode);
	}
	
	public String getErrorReference(String eCode)
	{
		return refProps.getProperty(eCode);
	}
	
	public String getErrorSpecification(String eCode)
	{
		return specProps.getProperty(eCode);
	}
	
	private void loadProperties(Properties props, String fileName)
	{
    	InputStream input = null;
    	try {
    		input = XDRMessages.class.getClassLoader().getResourceAsStream(fileName);
    		if(input==null){
    	            System.out.println("Failed to load message text properties "+ fileName +" from the classpath");
    		    return;
    		}
 
    		props.load(input);
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	}
        }
	}
	
	public String getMessageType(String itemID)
	{
		String prop = messageTypes.getProperty(itemID);
		if(prop == null)
			return "O";
		return prop;
	}
}
