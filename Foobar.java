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
