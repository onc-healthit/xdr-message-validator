package gov.onc.xdrtesttool.resource;

import gov.onc.xdrtesttool.error.MessageRecorderItem.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MetadataType {
	public static final MetadataType instance = new MetadataType();
	Map<String, HashMap> metadataTypesMap = new LinkedHashMap<String, HashMap>();
	List<String> metadataTypes = new ArrayList<String>();
	private String metadataType;
	public static final String MINIMAL = "minimal";
	
	private MetadataType()
	{
		loadMetadaTypes("XDSMetadataTypesValidationLevel.properties");
	}
	
	private void loadMetadaTypes(String fileName)
	{
		//System.out.println("Loading metadata types......"+fileName);
		Properties props = loadProperties(fileName);
		if(props != null)
		{
			Iterator iter = props.keySet().iterator();
			boolean first = true;
			int typeCount = 0;
			while(iter.hasNext())
			{
				String types = (String)iter.next();
				StringTokenizer tokens = new StringTokenizer(types, ",");
				if(first)
				{
					first = false;
					while(tokens.hasMoreTokens())
					{
						typeCount++;
						String token = (String) tokens.nextToken();
						//First token is ID
						if(typeCount == 1)
							continue;
						metadataTypesMap.put(token, new HashMap());
						metadataTypes.add(token);
					}
				}
				else
				{
					if(tokens.countTokens() != typeCount)
						continue;
					
					int counter = 0;
					String id = null;
					boolean idToken = true;
					while(tokens.hasMoreTokens())
					{
						String token = (String) tokens.nextToken();
						if(idToken)
						{
							id = token;
							idToken = false;
							continue;
						}
						Map option = metadataTypesMap.get(metadataTypes.get(counter));
						option.put(id, token);
						counter++;
					}
				}
			}
		}
		//System.out.println("Loading metadata types......"+metadataTypesMap);
	}
	
	private Properties loadProperties(String fileName)
	{
		//System.out.println("Loading properties......"+fileName);
    	InputStream input = null;
    	Properties props = null;
    	try {
    		input = XDRMessages.class.getClassLoader().getResourceAsStream(fileName);
    		if(input==null){
    	            System.out.println("Failed to load message text properties "+ fileName +" from the classpath");
    		    return null;
    		}
    		props = new LinkedProperties();
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
    	//System.out.println("Loading properties......"+props);
    	return props;
	}
	
	public MessageType getMessageType(String metadataType, String itemID)
	{
		Map types = metadataTypesMap.get(metadataType);
		if(types == null)
			return MessageType.Info;
		
		String type = ((String)types.get(itemID)).toUpperCase();
		if(type.equals("R"))
			return MessageType.Error;
		else if(type.equals("R2"))
			return MessageType.Warning;
		else if(type.equals("O"))
			return MessageType.Info;
		else if(type.equals("NA"))
			return MessageType.Error;
		else if(type.equals("C"))
			return MessageType.Error;
		else
			return MessageType.Info;
	}

	public String getMetadataType() {
		return metadataType;
	}

	public void setMetadataType(String metadataType) {
		this.metadataType = metadataType;
	}
	
	
}
