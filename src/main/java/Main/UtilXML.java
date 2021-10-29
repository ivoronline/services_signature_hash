package Main;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.StringWriter;

public class UtilXML {

  //================================================================================
  // FILE TO DOCUMENT
  //================================================================================
  // Document document = fileToDocument(fileName);
  public static Document fileToDocument(String fileName) throws Exception {

    //OPEN FILE FOR READING
    InputStream            inputStream     = UtilXML.class.getResourceAsStream(fileName);

    //PREPARE DOCUMENT BUILDER
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
                           documentFactory.setNamespaceAware(true);

    //READ DOCUMENT FROM FILE
    Document               document        = documentFactory.newDocumentBuilder().parse(inputStream);

    //RETURN DOCUMENT
    return document;

  }

  //=======================================================================================
  // DOCUMENT TO STRING
  //=======================================================================================
  public static String documentToString(Document document) throws Exception {

    DOMSource          domSource          = new DOMSource(document);

    StringWriter stringWriter = new StringWriter();
    StreamResult streamResult = new StreamResult(stringWriter);

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer        transformer;
                       transformer        = transformerFactory.newTransformer();
                       transformer.transform(domSource, streamResult);

    //RETURN STRING
    return stringWriter.toString();

  }

  //================================================================================
  // NODE TO DOCUMENT
  //================================================================================
  public static Document nodeToDocument(Node node) throws Exception {

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                           factory.setNamespaceAware(true);

    DocumentBuilder        builder = factory.newDocumentBuilder();

    Document               newDocument = builder.newDocument();
    Node                   importedNode = newDocument.importNode(node, true);
                           newDocument.appendChild(importedNode);

    return newDocument;

  }

}