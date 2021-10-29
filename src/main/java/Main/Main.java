package Main;

import org.w3c.dom.Document;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.SignatureMethod;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Main {

  //XML FILES
  static String XMLFilePerson   = "/Person.xml";
  static String XMLFileEnvelope = "/Envelope.xml";

  //================================================================================
  // MAIN
  //================================================================================
  public static void main(String[] args) throws Exception {

    //CREATE KEYS
    KeyPairGenerator  keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                      keyPairGenerator.initialize(2048);
    KeyPair           keyPair    = keyPairGenerator.generateKeyPair();
    PrivateKey        privateKey = keyPair.getPrivate();
    PublicKey         publicKey  = keyPair.getPublic();

    //CALCULATE HASH. ADD SIGNATURE TO XML.
    XMLDocument(privateKey);
    XMLElement (privateKey);

  }

  //================================================================================
  // XML DOCUMENT
  //================================================================================
  public static void XMLDocument(PrivateKey privateKey) throws Exception {

    //GET HASH FOR XML DOCUMENT
    Document XMLPerson                 = UtilXML.fileToDocument(XMLFilePerson);
    byte[]   XMLDocumentHash           = UtilXMLSignature.getHashForDocument(XMLPerson, "SHA-1");
                                         UtilXMLSignature.signDocument(XMLPerson, privateKey, "Person", "#data", DigestMethod.SHA1, SignatureMethod.RSA_SHA1);
    byte[]   XMLDocumentSignatureBytes = UtilSignature.sign("SHA1withRSA", privateKey, UtilXML.documentToString(XMLPerson).getBytes());

    //DISPLAY HASHES & SIGNED XML
    System.out.println("\nPerson.xml =================== ");
    System.out.println("HASH            = " + Base64.getEncoder().encodeToString(XMLDocumentHash));
    System.out.println("SIGNATURE       = " + Base64.getEncoder().encodeToString(XMLDocumentSignatureBytes));
    System.out.println("SIGNED DOCUMENT = \n" + UtilXML.documentToString(XMLPerson));

  }

  //================================================================================
  // XML ELEMENT
  //================================================================================
  public static void XMLElement(PrivateKey privateKey) throws Exception {

    //GET HASH FOR SPECIFIC XML ELEMENT
    Document XMLEnvelope               = UtilXML.fileToDocument(XMLFileEnvelope);
    byte[]   XMLElementHash            = UtilXMLSignature.getHashForElement(XMLEnvelope, "Person", "SHA-1");
                                         UtilXMLSignature.signDocument(XMLEnvelope, privateKey, "Person", "#data", DigestMethod.SHA1, SignatureMethod.RSA_SHA1);

    //DISPLAY HASHES & SIGNED XML
    System.out.println("\nEnvelope.xml =================== ");
    System.out.println("HASH      = " + Base64.getEncoder().encodeToString(XMLElementHash));
    System.out.println("SIGNATURE = \n" + UtilXML.documentToString(XMLEnvelope));

  }

}
