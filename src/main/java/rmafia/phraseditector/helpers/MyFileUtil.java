package rmafia.phraseditector.helpers;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URL;

class MyFileUtil {

    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    public static StringBuffer fileGetContents( String url, String encode ) {
        StringBuffer buffer = new StringBuffer();
        try {
            InputStream is = new URL(url).openStream();
            InputStreamReader isr = new InputStreamReader(is, encode);
            BufferedReader in = new BufferedReader(isr);
            String s = null;
            while ( (s = in.readLine()) != null) {
                buffer.append(s).append("\n");
            }
        } catch ( Exception e ) {
            System.out.println( e.toString() );
            buffer = null;
        } finally {
            return buffer;
        }
    }

    public static StringBuffer fileGetContents( String url ) {
        return fileGetContents( url, "JISAutoDetect" );
    }
}