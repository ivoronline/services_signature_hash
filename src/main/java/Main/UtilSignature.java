package Main;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

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

}
