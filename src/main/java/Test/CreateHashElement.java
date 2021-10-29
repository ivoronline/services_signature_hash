package Test;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.PrivateKey;

public class CreateHashElement {

  //KEY STORE
  static String keyStoreName     = "/ClientKeyStore.jks";
  static String keyStorePassword = "mypassword";
  static String keyStoreType     = "PKCS12";
  static String keyAlias         = "clientkeys";

  //XML FILES
  static String fileXMLInput     = "/Envelope.xml";
  static String fileXMLSigned    = "EnvelopeSigned.xml";

  //================================================================================
  // MAIN
  //================================================================================
  public static void main(String[] args) throws Exception {
    element();
    signDocument();
  }

  //================================================================================
  // ELEMENT
  //================================================================================
  public static void element() throws Exception {

    //GET HASH FOR ELEMENT
    Document XMLDocument2    = UtilXML2.fileToDocument("Envelope.xml");
    //String   XMLDocumentHash = UtilXMLSignature2.getBase64EncodedHashForElement(XMLDocument2, "Person");
    String   XMLDocumentHash = "fds";

    //GET PRIVATE KEY
    KeyStore.PrivateKeyEntry keyPair    = UtilKeys.getKeyPair(keyStoreName, keyStorePassword, keyStoreType, keyAlias);
    PrivateKey               privateKey = keyPair.getPrivateKey();

    //CONVERT NODE TO XML STRING
    Node                     node = XMLDocument2.getElementsByTagName("Person").item(0);
    StringWriter             stringWriter = new StringWriter();

    TransformerFactory       transformerFactory = new net.sf.saxon.TransformerFactoryImpl();

    Transformer           transformer = transformerFactory.newTransformer();
                          transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"  );
                          transformer.setOutputProperty(OutputKeys.METHOD              , "xml"  );
                          transformer.setOutputProperty(OutputKeys.ENCODING            , "UTF-8");
                          transformer.transform(new DOMSource(node), new StreamResult(stringWriter));

    String                XMLString = stringWriter.toString();

    byte[]     signatureBytes         = UtilSignature.sign("SHA1withRSA", privateKey, XMLString.getBytes());
    byte[]     signatureEncoded       = Base64.encodeBase64(signatureBytes);
    String     signatureEncodedString = new String(signatureEncoded);

    //DISPLAY HASH & SIGNATURE
    System.out.println(XMLString);
    System.out.println(XMLDocumentHash);
    System.out.println(signatureEncodedString);

  }

  //================================================================================
  // SIGN DOCUMENT
  //================================================================================
  private static void signDocument() throws Exception {

    //GET PRIVATE KEY
    KeyStore.PrivateKeyEntry  keyPair    = UtilKeys.getKeyPair(keyStoreName, keyStorePassword, keyStoreType, keyAlias);
    PrivateKey                privateKey = keyPair.getPrivateKey();

    //GET DOCUMENT FROM FILE
    Document                  document = UtilXML2.fileToDocument(fileXMLInput);

    //SIGN DOCUMENT
    UtilXMLSignature2.signDocument(document, privateKey, "Person", "#data", DigestMethod.SHA1, SignatureMethod.RSA_SHA1);

    //SAVE SIGNED DOCUMENT
    UtilXML2.documentToFile(fileXMLSigned, document);

  }

}
