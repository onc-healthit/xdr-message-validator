package gov.onc.xdrtesttool.validate;

import java.util.Arrays;
import java.util.List;

public final class Constants {
	public static final String XDS_Metadata_Checklist = "XDS Metadata Checklist";
	public static final String XDSDocumentEntry = "XDSDocumentEntry";
	public static final String XDSSubmissionSet_RegistryPackage = "XDSSubmissionSet.RegistryPackage";

	public static final List<String> xdrMessageNSList = Arrays.asList("http://www.w3.org/2003/05/soap-envelope",
													"http://www.w3.org/2005/08/addressing",
													"urn:ihe:iti:xds-b:2007",
													"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0",
													"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0",
													"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0",
													"http://www.w3.org/2004/08/xop/include");

	public static final List<String> directXdrNSList = Arrays.asList(
													"http://www.w3.org/2001/XMLSchema",
													"http://www.w3.org/2001/XMLSchema-instance",
													"http://www.w3.org/2003/05/soap-envelope",
													"http://www.w3.org/2005/08/addressing",
													"urn.direct.addressing");
	
}
