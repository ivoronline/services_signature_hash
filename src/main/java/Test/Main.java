package Test;

import org.w3c.dom.Document;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.SignatureMethod;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Main {

  //XML FILES
  static String xml              = "<Person><name>John</name><age>20</age></Person>";
  static String fileXMLInput     = "/Person.xml";
  static String fileXMLSigned    = "PersonSigned.xml";

  //====================================================================================
  // MAIN
  //====================================================================================
  public static void main(String[] args) throws Exception {

    //GET XML
    Document          dataDocument = UtilXML2.stringToDocument(xml);
    byte[]            dataBytes    = xml.getBytes();

    //CREATE KEYS
    KeyPairGenerator  keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                      keyPairGenerator.initialize(2048);
    KeyPair           keyPair    = keyPairGenerator.generateKeyPair();
    PrivateKey        privateKey = keyPair.getPrivate();
    PublicKey         publicKey  = keyPair.getPublic();

    //SIGN AUTOMATICALLY & MANUALLY
    sigManual        (privateKey, publicKey, dataBytes);
    signAutomaticText(privateKey, publicKey, dataBytes);
    signAutomaticXML (privateKey, dataDocument);

  }

  //====================================================================================
  // SIGN MANUAL
  //====================================================================================
  private static void sigManual(PrivateKey privateKey, PublicKey publicKey, byte[] dataBytes) throws Exception {

    //LOG
    System.out.println("\nSIGN MANUAL");

    //MANUALLY SIGN & VERIFY
    byte[]  manualHashBytes         = UtilSignature.hash   ("SHA-1", dataBytes);
    byte[]  manualPaddingHashBytes  = UtilSignature.padding("SHA-1", manualHashBytes);
    byte[]  manualSignatureBytes    = UtilSignature.sign   ("NONEwithRSA", privateKey, manualPaddingHashBytes);
    Boolean manualVerified          = UtilSignature.verify ("NONEwithRSA", publicKey , manualPaddingHashBytes, manualSignatureBytes);

  }

  //====================================================================================
  // SIGN AUTOMATIC TEXT
  //====================================================================================
  private static void signAutomaticText(PrivateKey privateKey, PublicKey publicKey, byte[] dataBytes) throws Exception {

    //LOG
    System.out.println("\nSIGN AUTOMATIC");

    //AUTOMATICALLY SIGN & VERIFY
    byte[]  automaticSignatureBytes = UtilSignature.sign  ("SHA1withRSA", privateKey, dataBytes);
    Boolean automaticVerified       = UtilSignature.verify("SHA1withRSA", publicKey , dataBytes, automaticSignatureBytes);

  }

  //====================================================================================
  // SIGN AUTOMATIC XML
  //====================================================================================
  private static void signAutomaticXML(PrivateKey privateKey, Document dataDocument) throws Exception {

    //LOG
    System.out.println("\nSIGN XML AUTOMATIC");

    //SIGN XML
    UtilXMLSignature2.signDocument(dataDocument, privateKey, "Person", "", DigestMethod.SHA1, SignatureMethod.RSA_SHA1);

    //SAVE DOCUMENT TO FILE
    UtilXML2.documentToFile(fileXMLSigned, dataDocument);

  }

}
