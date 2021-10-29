package Test;

import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;

public class UtilKeys {

  //================================================================================
  // GET KEY PAIR
  //================================================================================
  public static KeyStore.PrivateKeyEntry getKeyPair(
    String keyStoreName,     //"/ClientKeyStore.jks"
    String keyStorePassword, //"mypassword";
    String keyStoreType,     //"JKS"
    String keyAlias          //"clientkeys1"
  ) throws Exception {

    //GET PRIVATE KEY
    InputStream                 inputStream = UtilKeys.class.getResourceAsStream(keyStoreName);
    char[]                      password    = keyStorePassword.toCharArray();    //For KeyStore & Private Key
    KeyStore                    keyStore    = KeyStore.getInstance(keyStoreType);
                                keyStore.load(inputStream, password);
    KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(password);
    KeyStore.PrivateKeyEntry    keyPair = (KeyStore.PrivateKeyEntry) keyStore.getEntry(keyAlias, keyPassword);

    //RETURN KEY PAIR
    return keyPair;

  }

  //====================================================================================
  // GENERATE KEY PAIR RSA
  //====================================================================================
  // KeyPair             keyPair    = Test.UtilKeys.generateKeyPairRSA();
  // PrivateKey          privateKey = keyPair.getPrivate();
  // PublicKey           publicKey  = keyPair.getPublic();
  // System.err.println("Private key format: " + privateKey.getFormat());   //PKCS#8
  // System.err.println("Public  key format: " + publicKey .getFormat() );  //X.509
  static KeyPair generateKeyPairRSA() throws Exception {

    //GENERATE KEY PAIR
    KeyPairGenerator  keyPairGenerator  = KeyPairGenerator.getInstance("RSA");
                      keyPairGenerator.initialize(2048);
    KeyPair           keyPair          = keyPairGenerator.generateKeyPair();

    //RETURN KEY PAIR
    return keyPair;

  }

  //====================================================================================
  // GENERATE KEY PAIR DSA
  //====================================================================================
  // KeyPair             keyPair    = Test.UtilKeys.generateKeyPairDSA();
  // PrivateKey          privateKey = keyPair.getPrivate();
  // PublicKey           publicKey  = keyPair.getPublic();
  // System.err.println("Private key format: " + privateKey.getFormat());   //PKCS#8
  // System.err.println("Public  key format: " + publicKey .getFormat() );  //X.509
  static KeyPair generateKeyPairDSA() throws Exception {

    //GENERATE KEY PAIR
    SecureRandom        random     = SecureRandom.getInstance("SHA1PRNG", "SUN");
    KeyPairGenerator    keyGen     = KeyPairGenerator.getInstance("DSA", "SUN");
                        keyGen.initialize(1024, random);
    KeyPair             keyPair    = keyGen.generateKeyPair();

    //RETURN KEY PAIR
    return keyPair;

  }

}
