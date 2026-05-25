package parsers.DocumentBuilderFactory;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;


class Foobar {

    private DocumentBuilderFactory dbFactory;

    public Foobar(File input) {
        //ruleid: documentbuilderfactory-xxe
        this.dbFactory = DocumentBuilderFactory.newInstance();
        try {
            this.dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            this.dbFactory.setExpandEntityReferences(false);
            this.dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            this.dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            this.dbFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        } catch (ParserConfigurationException e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }
        DocumentBuilder dBuilder = this.dbFactory.newDocumentBuilder();
        dBuilder.parse(input);
    }

    public void doSmth(File input) {
        //ruleid: documentbuilderfactory-xxe
        var dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        dBuilder.parse(input);
    }
}
