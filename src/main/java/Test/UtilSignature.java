package Test;

import net.sf.saxon.TransformerFactoryImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.xml.security.Init;
import org.apache.xml.security.c14n.Canonicalizer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Collections;

public class UtilSignature {

  //====================================================================================
  // HASH
  //====================================================================================
  public static byte[] hash(String algorithm, byte[] dataBytes) throws Exception {

    //CREATE HASH
    MessageDigest digest    = MessageDigest.getInstance(algorithm);
    byte[]        hashBytes = digest.digest(dataBytes);

    //DISPLAY ENCODED HASH
    //System.out.println("HASH      = " + java.util.Base64.getEncoder().encodeToString(hashBytes));

    //RETURN HASH
    return hashBytes;

  }

  //====================================================================================
  // PADDING
  //====================================================================================
  public static byte[] padding(String algorithm, byte[] hashBytes) throws Exception {

    //PREPARE PADDING
    byte[] padding = null;
    if (algorithm.equals("SHA-1"  )) { padding = new byte[] { 0x30, 0x21, 0x30, 0x09, 0x06, 0x05, 0x2b, (byte) 0x0e, 0x03, 0x02, 0x1a, 0x05, 0x00, 0x04, 0x14                         }; }
    if (algorithm.equals("SHA-256")) { padding = new byte[] { 0x30, 0x31, 0x30, 0x0d, 0x06, 0x09, 0x60, (byte) 0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x01, 0x05, 0x00, 0x04, 0x20 }; }

    //ADD PADDING & HASH TO RESULTING ARRAY
    byte[] paddingHash = new byte[padding.length + hashBytes.length];
    System.arraycopy(padding  , 0, paddingHash, 0             , padding.length  );
    System.arraycopy(hashBytes, 0, paddingHash, padding.length, hashBytes.length);

    //RETURN HASH
    return paddingHash;

  }

  //====================================================================================
  // SIGN
  //====================================================================================
  // "SHA256withRSA" => SHA256 Hash + SHA256 Padding + RSA Encryption
  // "SHA1withRSA"   => SHA1   Hash + SHA1   Padding + RSA Encryption
  // "NONEwithRSA"   =>                                RSA Encryption
  public static byte[] sign(String algorithm, PrivateKey privateKey, byte[] dataBytes) throws Exception {

    //INITIALIZE SIGNATURE
    Signature signature = Signature.getInstance(algorithm);
              signature.initSign(privateKey);
              signature.update(dataBytes);

    //CREATE SIGNATURE
    byte[]    signatureBytes = signature.sign();

    //DISPLAY ENCODED SIGNATURE
    System.out.println("SIGNATURE = " + java.util.Base64.getEncoder().encodeToString(signatureBytes));

    //RETURN SIGNATURE
    return signatureBytes;

  }

  //====================================================================================
  // VERIFY
  //====================================================================================
  public static Boolean verify(String algorithm, PublicKey publicKey, byte[] dataBytes, byte[] signatureBytes) throws Exception {

    //INITIALIZE SIGNATURE
    Signature signature = Signature.getInstance(algorithm);
              signature.initVerify(publicKey);
              signature.update(dataBytes);

    //VERIFY SIGNATURE
    boolean   verified = signature.verify(signatureBytes);

    //DISPLAY VERIFICATION
    System.out.println("VERIFIED  = " + verified);

    //RETURN VERIFICATION
    return verified;

  }

}
