XDR Test Tool
=============
This tool supports testing XDR implementation over SOAP protocal. This tool takes XDR request over SOAP protocal as a request to validate and sends response with any validation messages. This tool also initiates XDR request over SOAP protocal to feed it as input to test any existing XDR implementations.

The current version of the [XDR and XDM for Direct Messaging specification (Version 1.0)] (http://wiki.directproject.org/file/view/2011-03-09%20PDF%20-%20XDR%20and%20XDM%20for%20Direct%20Messaging%20Specification_FINAL.pdf) was published on 3/9/2011. Download the XDR and XDM for Direct Messaging Specification [PDF - 485 KB].Web Site Disclaimers

Get more information at the [Direct Project website] (http://directproject.org/), or visit the project [wikiWeb] (http://wiki.directproject.org/).

To build and run the tool:
# Pre-requisites
* Java 1.7
* Maven 2.3.x
* Apache Tomcat 7.x

Make sure to set your JAVA_HOME, M2_HOME and M2_REPO environment variables. Open cmd window and navigate to XDRValidator. Run "mvn clean install". After successful completion, go to target folder and copy xdrvalidator.war to Apache Tomcat webapps folder. Start tomcat instance and the XDRValidator service is available at http://localhost:<port>/xdrwebservice/Dispatcher/XDRService
