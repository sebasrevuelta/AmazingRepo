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

    public void doSmthSecure(File input) {
        //ok: documentbuilderfactory-xxe
        var dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dBuilder.parse(input);
    }

    public void doSmthSecure2(File input) {
        //ok: documentbuilderfactory-xxe
        var dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        dbFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
        dBuilder.parse(input);
    }

    //TODO:
    // `dbFactory` initiated in the constructor
    // and then used in `todoExample`
    public Foobar() {
        //todoruleid: documentbuilderfactory-xxe
        this.dbFactory = DocumentBuilderFactory.newInstance();
    }

    public void todoExample(File input) {
        DocumentBuilder dBuilder = this.dbFactory.newDocumentBuilder();
        dBuilder.parse(input);
    }
}


// DocumentBuilderFactory is secured from DoS through Entity Expansion by default
public class Tests {


    public static void main(String [] args) {
        // ignore error messages
        System.setErr(new PrintStream(new OutputStream() {
            public void write(int b) {
            }
        }));


        try {
            System.out.println("TESTING DocumentBuilderFactory configurations");
            testDefaultConfig();
            testSetFeatureSecreProcessing();

            testAccessExternalDTD();
            testAccessExternalSchema();
            // access external stylesheet is not applicable to DocumentBuilderFactory
            testDisallowDoctypeDecl();
            testExternalGeneralEntities();
            testExternalParameterEntities();
            testSetValidating();
            testSetExpandEntities();
            testSetXIncludeAware();
            testXInclude();
            testLoadExternalDTD();
            testLoadDTDGrammar();
            testNamespaces();
            testSetNameSpaceAware();

            testDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
            // do nothing
        }
    }

    public static void testDefaultConfig() throws ParserConfigurationException {
        System.out.println("Default Config");
        //ruleid: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testSetFeatureSecreProcessing() throws ParserConfigurationException {
        System.out.println("setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)");
        //ok: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testSetFeatureSecreProcessing2() throws ParserConfigurationException {
        //ok: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testAccessExternalDTD() throws ParserConfigurationException {
        System.out.println("setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, \"\")");
        //ok: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testAccessExternalSchema() throws ParserConfigurationException {
        System.out.println("setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, \"\")");
        //ruleid: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "")
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testDisallowDoctypeDecl() throws ParserConfigurationException {
        System.out.println("setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true)");
        //ok: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testExternalGeneralEntities() throws ParserConfigurationException {
        System.out.println("setFeature(\"http://xml.org/sax/features/external-general-entities\", false)");
        //ok: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testExternalParameterEntities() throws ParserConfigurationException {
        System.out.println("setFeature(\"http://xml.org/sax/features/external-parameter-entities\", false)");
        //ruleid: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testSetValidating() throws ParserConfigurationException {
        System.out.println("setValidating(false)");
        //ruleid: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setValidating(false);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testSetExpandEntities() throws ParserConfigurationException {
        System.out.println("setExpandEntityReferences(false)");
        //ok: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setExpandEntityReferences(false);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testSetXIncludeAware() throws ParserConfigurationException {
        System.out.println("setXIncludeAware(false)");
        //ruleid: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setXIncludeAware(false);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testXInclude() throws ParserConfigurationException {
        System.out.println("setFeature(\"http://apache.org/xml/features/xinclude\", false)");
        //ruleid: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature("http://apache.org/xml/features/xinclude", false);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testLoadExternalDTD() throws ParserConfigurationException {
        System.out.println("setFeature(\"http://apache.org/xml/features/nonvalidating/load-external-dtd\", false)");
        //ruleid: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testLoadDTDGrammar() throws ParserConfigurationException {
        System.out.println("setFeature(\"http://apache.org/xml/features/nonvalidating/load-dtd-grammar\", false)");
        //ruleid: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testNamespaces() throws ParserConfigurationException {
        System.out.println("setFeature(\"http://apache.org/xml/features/namespaces\", false)");
        //ruleid: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testSetNameSpaceAware() throws ParserConfigurationException {
        System.out.println("setNameSapceAware(true)");
        //ruleid: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        parseAll(dBuilder);
    }

    public static void testDocumentBuilder() throws ParserConfigurationException {
        System.out.println("testDocumentBuilder set empty entity resolver");
        //ok: documentbuilderfactory-xxe
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        dBuilder.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return new InputSource(new StringReader(""));
            }
        });
        parseAll(dBuilder);
    }

    /*
        Parse the test files
     */

    public static void parseAll(DocumentBuilder db){
        parseXmlBomb(db);
        parseInputWithSchema(db);
        parseInputWithStylesheet(db);
        parseInputWithParameterEntity(db);
    }

    public static void parseXmlBomb(DocumentBuilder dBuilder){
        try {
            File input = new File("payloads/input-dos/xml-bomb.xml");
            dBuilder.parse(input);
            System.out.println("    Parse XML Bomb: Secure");
        } catch (Exception e) {
            if (e.getMessage().equals("JAXP00010001: The parser has encountered more than \"64000\" entity expansions in this document; this is the limit imposed by the JDK.")){
                System.out.println("        Exception: " + e.getMessage());
                System.out.println("    Parse XML Bomb: Insecure");
            } else if(e.getMessage().contains("DOCTYPE is disallowed")){
                System.out.println("        Exception: " + e.getMessage());
                System.out.println("    Parse XML Bomb: Secure");
            }else{
                System.out.println(e.getMessage());
            }
        }
    }

    public static void parseInputWithSchema(DocumentBuilder dBuilder){
        try {
            File input = new File("payloads/input-with-schema/input.xml");
            dBuilder.parse(input);
            System.out.println("    Parse XML Input With Schema: Secure");
        } catch (Exception e) {
            if(e.getMessage().contains("Connection refused")){
                System.out.println("    Parse XML Input With Schema: Insecure");
            } else if(e.getMessage().contains("External Entity: Failed to read external document 'localhost:8090', because 'http' access is not allowed due to restriction set by the accessExternalDTD property.")){
                System.out.println("        Exception: " + e.getMessage());
                System.out.println("    Parse XML Input With Schema: Secure");
            } else if(e.getMessage().contains("DOCTYPE is disallowed")){
                System.out.println("        Exception: " + e.getMessage());
                System.out.println("    Parse XML Input With Schema: Secure");
            }else{
                System.out.println(e.getMessage());
            }
        }
    }

    public static void parseInputWithStylesheet(DocumentBuilder dBuilder){
        try {
            File input = new File("payloads/input-with-stylesheet/input.xml");
            dBuilder.parse(input);
            System.out.println("    Parse XML Input With Schema: Secure");
        } catch (Exception e) {
            if(e.getMessage().contains("Connection refused")){
                System.out.println("    Parse Xml Input With Stylesheet: Insecure");
            } else if(e.getMessage().contains("External Entity: Failed to read external document 'localhost:8090', because 'http' access is not allowed due to restriction set by the accessExternalDTD property.")){
                System.out.println("        Exception: " + e.getMessage());
                System.out.println("    Parse Xml Input With Stylesheet: Secure");
            } else if(e.getMessage().contains("DOCTYPE is disallowed")){
                System.out.println("        Exception: " + e.getMessage());
                System.out.println("    Parse Xml Input With Stylesheet: Secure");
            }
            else {
                System.out.println(e.getMessage());
            }
        }
    }
    public static void parseInputWithParameterEntity(DocumentBuilder dBuilder){
        try {
            File input = new File("payloads/input-with-parameter-entity/input.xml");
            dBuilder.parse(input);
            System.out.println("    Parse Xml Input With Parameter Entity: Secure");
        } catch (Exception e){
            if(e.getMessage().contains("Connection refused")){
                System.out.println("    Parse Xml Input With Parameter Entity: Insecure");
            } else if(e.getMessage().contains("External Entity: Failed to read external document 'localhost:8090', because 'http' access is not allowed due to restriction set by the accessExternalDTD property.")){
                System.out.println("        Exception: " + e.getMessage());
                System.out.println("    Parse Xml Input With Parameter Entity: Secure");
            } else if(e.getMessage().contains("DOCTYPE is disallowed")){
                System.out.println("        Exception: " + e.getMessage());
                System.out.println("    Parse Xml Input With Parameter Entity: Secure");
            }
            else {
                System.out.println(e.getMessage());
            }
        }
    }
}
