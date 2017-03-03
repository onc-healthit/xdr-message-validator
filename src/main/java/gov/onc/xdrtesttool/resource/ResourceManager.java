package gov.onc.xdrtesttool.resource;

import gov.onc.xdrtesttool.validate.ValidationUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ResourceManager {
	public static final ResourceManager instance = new ResourceManager();
	List<String> conceptCodes = new ArrayList();
	List<String> conceptNameSpecialityArea = new ArrayList();
	Map<String, String> contextMetadataTypes = new HashMap();
	List<String> SNOMEDCTconceptCodes = new ArrayList();
	List<String> HITSP_C80_20_Table2_144conceptCodes = new ArrayList();
	List<String> HITSP_C80_20_Table2_147conceptCodes = new ArrayList();
	List<String> HITSP_C80_20_Table2_147conceptNames = new ArrayList();
	List<String> HITSP_C80_20_Table2_149conceptNames = new ArrayList();
	List<String> HITSP_C80_20_Table2_151conceptCodes = new ArrayList();
	List<String> HITSP_C80_20_Table2_153conceptCodes = new ArrayList();
	List<String> HITSP_C80_20_Table2_153conceptNames = new ArrayList();
	
	private ResourceManager()
	{
		try {
			loadSNOMEDCTConceptIds("NOMEDCTConceptIDs.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			loadHITSP_C80_20_Table2_151conceptCodes("HITSP-C80-2.0-Table2-151_ConceptCodes.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			loadHITSP_C80_20_Table2_153conceptCodes("HITSP-C80-2.0-Table2-153_ConceptCodes.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			loadHITSP_C80_20_Table2_153conceptNames("HITSP-C80-2.0-Table2-151_ConceptNames.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			loadHITSP_C80_20_Table2_149conceptNames("HITSP-C80-2.0-Table2-149_ConceptNames.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			loadHITSP_C80_20_Table2_144conceptCodes("HITSP-C80-2.0-Table2-144.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			loadHITSP_C80_20_Table2_147conceptCodes("HITSP-C80-2.0-Table2-147.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			loadHITSP_C80_20_Table2_147conceptNames("HITSP-C80-2.0-Table2-147-ConceptNames.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			loadConceptCodes("ConceptCodes.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			loadConceptNameSpecialityAreas("ConceptNameSpecialityArea.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadContextMetadataTypes("XDSContextMetadataTypes.properties");
	}

	public List<String> getSNOMEDCTConceptIds()
	{
		return SNOMEDCTconceptCodes;
	}

	public List<String> getHITSP_C80_20_Table2_144conceptCodes()
	{
		return HITSP_C80_20_Table2_144conceptCodes;
	}

	public List<String> getHITSP_C80_20_Table2_147conceptCodes()
	{
		return HITSP_C80_20_Table2_147conceptCodes;
	}

	public List<String> getHITSP_C80_20_Table2_151conceptCodes()
	{
		return HITSP_C80_20_Table2_151conceptCodes;
	}

	public List<String> getHITSP_C80_20_Table2_153conceptCodes()
	{
		return HITSP_C80_20_Table2_153conceptCodes;
	}

	public List<String> getHITSP_C80_20_Table2_153conceptNames()
	{
		return HITSP_C80_20_Table2_153conceptNames;
	}

	public List<String> getHITSP_C80_20_Table2_147conceptNames()
	{
		return HITSP_C80_20_Table2_147conceptNames;
	}

	public List<String> getHITSP_C80_20_Table2_149conceptNames()
	{
		return HITSP_C80_20_Table2_149conceptNames;
	}

	public List<String> getConceptCodes()
	{
		return conceptCodes;
	}
	
	public List<String> getConceptNameSpecialityAreas()
	{
		return conceptNameSpecialityArea;
	}

	private void loadContextMetadataTypes(String fileName)
	{
		Properties props = loadProperties(fileName);
		if(props != null)
		{
			Iterator iter = props.keySet().iterator();
			while(iter.hasNext())
			{
				String context = (String)iter.next();
				String metadata = props.getProperty(context);
				contextMetadataTypes.put(context, metadata);
			}
		}
	}
	
	private void loadHITSP_C80_20_Table2_144conceptCodes(String fileName) throws FileNotFoundException, IOException 
	{
		HITSP_C80_20_Table2_144conceptCodes.addAll(ValidationUtil.getListFromFile(fileName));
	}

	private void loadHITSP_C80_20_Table2_151conceptCodes(String fileName) throws FileNotFoundException, IOException 
	{
		HITSP_C80_20_Table2_151conceptCodes.addAll(ValidationUtil.getListFromFile(fileName));
	}

	private void loadHITSP_C80_20_Table2_153conceptCodes(String fileName) throws FileNotFoundException, IOException 
	{
		HITSP_C80_20_Table2_153conceptCodes.addAll(ValidationUtil.getListFromFile(fileName));
	}

	private void loadHITSP_C80_20_Table2_153conceptNames(String fileName) throws FileNotFoundException, IOException 
	{
		HITSP_C80_20_Table2_153conceptNames.addAll(ValidationUtil.getListFromFile(fileName));
	}

	private void loadHITSP_C80_20_Table2_147conceptCodes(String fileName) throws FileNotFoundException, IOException 
	{
		HITSP_C80_20_Table2_147conceptCodes.addAll(ValidationUtil.getListFromFile(fileName));
	}

	private void loadHITSP_C80_20_Table2_147conceptNames(String fileName) throws FileNotFoundException, IOException 
	{
		HITSP_C80_20_Table2_147conceptNames.addAll(ValidationUtil.getListFromFile(fileName));
	}
	
	private void loadHITSP_C80_20_Table2_149conceptNames(String fileName) throws FileNotFoundException, IOException 
	{
		HITSP_C80_20_Table2_149conceptNames.addAll(ValidationUtil.getListFromFile(fileName));
	}

	private void loadSNOMEDCTConceptIds(String fileName) throws FileNotFoundException, IOException
	{
		if(SNOMEDCTconceptCodes == null || SNOMEDCTconceptCodes.size() == 0)
			SNOMEDCTconceptCodes.addAll(ValidationUtil.getListFromFile(fileName));
	}

	private void loadConceptCodes(String fileName) throws FileNotFoundException, IOException
	{
		if(conceptCodes == null || conceptCodes.size() == 0)
			conceptCodes.addAll(ValidationUtil.getListFromFile(fileName));
	}
	
	private void loadConceptNameSpecialityAreas(String fileName) throws FileNotFoundException, IOException
	{
		conceptNameSpecialityArea.addAll(ValidationUtil.getListFromFile(fileName));
	}

	private Properties loadProperties(String fileName)
	{
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
    	return props;
	}
	
	public String getMetadataType(String context)
	{
		return contextMetadataTypes.get(context);
	}
	
}
