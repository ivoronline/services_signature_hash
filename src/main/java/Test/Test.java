package Test;

import org.w3c.dom.Document;

import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.SignatureMethod;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Test {

  //XML FILES
  static String xml = "<Person><name>John</name><age>20</age></Person>";

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

    //SIGN IN TWO DIFFERENT WAYS
    byte[]  automaticSignatureBytes = UtilSignature.sign("SHA1withRSA", privateKey, dataBytes);
                                      UtilXMLSignature2.signDocument(dataDocument, privateKey, "Person", "", DigestMethod.SHA1, SignatureMethod.RSA_SHA1);

    System.out.println(UtilXML2.documentToString(dataDocument));

  }

}
