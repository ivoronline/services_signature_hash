package Test;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;

import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.SignatureMethod;
import java.security.KeyStore;
import java.security.PrivateKey;

public class CreateHash {

  //KEY STORE
  static String keyStoreName     = "/ClientKeyStore.jks";
  static String keyStorePassword = "mypassword";
  static String keyStoreType     = "PKCS12";
  static String keyAlias         = "clientkeys";

  //XML FILES
  static String fileXMLInput     = "/Person.xml";
  static String fileXMLSigned    = "PersonSigned.xml";

  //================================================================================
  // MAIN
  //================================================================================
  public static void main(String[] args) throws Exception {
    document();
    addSignature0();
    //element();
  }

  //================================================================================
  // DOCUMENT
  //================================================================================
  public static void document() throws Exception {

    //GET HASH FOR DOCUMENT
    Document                  XMLDocument    = UtilXML2.fileToDocument(fileXMLInput);
    byte[]                    XMLElementHash = UtilXMLSignature2.getHashForDocument(XMLDocument, "SHA-1");

    //GET PRIVATE KEY
    KeyStore.PrivateKeyEntry  keyPair    = UtilKeys.getKeyPair(keyStoreName, keyStorePassword, keyStoreType, keyAlias);
    PrivateKey                privateKey = keyPair.getPrivateKey();

    //CREATE DIGITAL SIGNATURE
    byte[]                    signatureBytes         = UtilSignature.sign("SHA1withRSA", privateKey, XMLElementHash);
    byte[]                    signatureEncoded       = Base64.encodeBase64(signatureBytes);
    String                    signatureEncodedString = new String(signatureEncoded);

    //DISPLAY HASH & SIGNATURE
    System.out.println(XMLElementHash);
    System.out.println(signatureEncodedString);

  }

  //================================================================================
  // ELEMENT
  //================================================================================
  public static void element() throws Exception {

    //GET HASH FOR ELEMENT
    Document XMLDocument2    = UtilXML2.fileToDocument("Envelope.xml");
    //String   XMLDocumentHash = UtilXMLSignature2.getBase64EncodedHashForElement(XMLDocument2, "Person");
    //System.out.println(XMLDocumentHash);

  }

  //================================================================================
  // ADD SIGNATURE
  //================================================================================
  private static void addSignature0() throws Exception {

    //GET PRIVATE KEY
    KeyStore.PrivateKeyEntry keyPair    = UtilKeys.getKeyPair(keyStoreName, keyStorePassword, keyStoreType, keyAlias);
    PrivateKey               privateKey = keyPair.getPrivateKey();

    //GET DOCUMENT FROM FILE
    Document document = UtilXML2.fileToDocument(fileXMLInput);

    //SIGN DOCUMENT
    UtilXMLSignature2.signDocument(document, privateKey, "Person", "", DigestMethod.SHA1, SignatureMethod.RSA_SHA1);

    //SAVE SIGNED DOCUMENT
    UtilXML2.documentToFile(fileXMLSigned, document);

  }

}
