package gov.onc.xdrtesttool.validate;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class ResourceResolver implements LSResourceResolver {

    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(systemId);
        return new LSInputImpl(publicId, systemId, resourceAsStream);
    }    
    
    protected class LSInputImpl implements LSInput {

        private String publicId;

        private String systemId;

        public String getPublicId() {
            return publicId;
        }

        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        
        public String getBaseURI() {
            return null;
        }

        
        public InputStream getByteStream() {            
            return null;
        }

        
        public boolean getCertifiedText() {
            return false;
        }

        
        public Reader getCharacterStream() {
            return null;
        }

        
        public String getEncoding() {
            return null;
        }

        
        public String getStringData() {
            synchronized (inputStream) {
                try {
                    byte[] input = new byte[inputStream.available()];
                    inputStream.read(input);
                    String contents = new String(input);
                    return contents;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        
        public void setBaseURI(String baseURI) {
        }

        
        public void setByteStream(InputStream byteStream) {         
        }

        
        public void setCertifiedText(boolean certifiedText) {
        }

        
        public void setCharacterStream(Reader characterStream) {
        }

        
        public void setEncoding(String encoding) {
        }

        
        public void setStringData(String stringData) {           
        }

        public String getSystemId() {
            return systemId;
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }

        public BufferedInputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(BufferedInputStream inputStream) {
            this.inputStream = inputStream;
        }

        private BufferedInputStream inputStream;

        public LSInputImpl(String publicId, String sysId, InputStream input) {
            this.publicId = publicId;
            this.systemId = sysId;
            this.inputStream = new BufferedInputStream(input);
        }

    }

}