package Test;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class UtilXML2 {

  //================================================================================
  // FILE TO DOCUMENT
  //================================================================================
  // Document document = fileToDocument(fileName);
  public static Document fileToDocument(String fileName) throws Exception {

    //OPEN FILE FOR READING
    InputStream            inputStream     = UtilXML2.class.getResourceAsStream(fileName);

    //PREPARE DOCUMENT BUILDER
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
                           documentFactory.setNamespaceAware(true);

    //READ DOCUMENT FROM FILE
    Document               document        = documentFactory.newDocumentBuilder().parse(inputStream);

    //RETURN DOCUMENT
    return document;

  }

  //================================================================================
  // DOCUMENT TO FILE
  //================================================================================
  public static void documentToFile(String fileName, Document document) throws Exception {

    //OPEN FILE FOR WRITING
    OutputStream       outputStream       = new FileOutputStream(fileName);
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer        transformer        = transformerFactory.newTransformer();
                       transformer.transform(new DOMSource(document), new StreamResult(outputStream));

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
  // STRING TO DOCUMENT
  //================================================================================
  public static Document stringToDocument(String xmlString) throws Exception {

    //READ XML STRING
    InputSource             inputSource = new InputSource();
                            inputSource.setCharacterStream(new StringReader(xmlString));

    //CONVERT TO DOCUMENT
    DocumentBuilderFactory  documentBuilderFactory = DocumentBuilderFactory.newInstance();
                            documentBuilderFactory.setNamespaceAware(true);    //IMPORTANT
    DocumentBuilder         documentBuilder = documentBuilderFactory.newDocumentBuilder();
    Document                document        = documentBuilder.parse(inputSource);

    //RETURN DOCUMENT
    return document;

  }

}