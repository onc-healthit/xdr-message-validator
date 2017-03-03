package gov.onc.xdrtesttool.xml;

import org.springframework.ws.transport.TransportInputStream;

import java.io.IOException;

public interface SoapProtocolChooser {
 public boolean useSoap11(TransportInputStream transportInputStream) throws IOException;
}