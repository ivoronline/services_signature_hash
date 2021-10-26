import net.sf.saxon.TransformerFactoryImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.xml.security.Init;
import org.apache.xml.security.c14n.Canonicalizer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class UtilSignature {

  //================================================================================
  // GET BASE64 ENCODED HASH FOR DOCUMENT
  //================================================================================
  public static String getBase64EncodedHashForDocument(Document XMLDocument) throws Exception {

    //CONVERT XML DOCUMENT TO XML STRING
    StringWriter          stringWriter = new StringWriter();

    TransformerFactory    transformerFactory = new TransformerFactoryImpl();

    Transformer           transformer = transformerFactory.newTransformer();
                          transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                          transformer.setOutputProperty(OutputKeys.METHOD              , "xml");
                          transformer.setOutputProperty(OutputKeys.ENCODING            , "UTF-8");
                          transformer.transform(new DOMSource(XMLDocument), new StreamResult(stringWriter));

    String                XMLString = stringWriter.toString();

    //CANONICALIZE XML STRING
    Init.init();
    ByteArrayOutputStream out   = new ByteArrayOutputStream();
    Canonicalizer         canon = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
                          canon.canonicalize(XMLString.getBytes(), out, false);
    String                XMLStringCanon = out.toString();

    //CREATE HASH
    MessageDigest         digest = MessageDigest.getInstance("SHA-1");
    byte[]                hash   = digest.digest(XMLStringCanon.getBytes(StandardCharsets.UTF_8));

    //BASE64 ENCODED HASH
    byte[]                hashEncoded = Base64.encodeBase64(hash);
    String                XMLStringHashEncoded = new String(hashEncoded);

    //RETURN HASH
    return XMLStringHashEncoded;

  }

  //================================================================================
  // GET BASE64 ENCODED HASH FROM ELEMENT
  //================================================================================
  public static String getBase64EncodedHashForElement(Document XMLDocument, String tagName) throws Exception {

    //GET XML ELEMENT
    Node                  node = XMLDocument.getElementsByTagName(tagName).item(0);

    //CONVERT XML DOCUMENT TO XML STRING
    StringWriter          stringWriter = new StringWriter();

    TransformerFactory    transformerFactory = new net.sf.saxon.TransformerFactoryImpl();

    Transformer           transformer = transformerFactory.newTransformer();
                          transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"  );
                          transformer.setOutputProperty(OutputKeys.METHOD              , "xml"  );
                          transformer.setOutputProperty(OutputKeys.ENCODING            , "UTF-8");
                          transformer.transform(new DOMSource(node), new StreamResult(stringWriter));

    String                XMLString = stringWriter.toString();

    //CANONICALIZE XML STRING
    Init.init();
    ByteArrayOutputStream out   = new ByteArrayOutputStream();
    Canonicalizer         canon = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
                          canon.canonicalize(XMLString.getBytes(), out, false);
    String                XMLStringCanon = out.toString();

    //CREATE HASH
    MessageDigest         digest = MessageDigest.getInstance("SHA-1");
    byte[]                hash   = digest.digest(XMLStringCanon.getBytes(StandardCharsets.UTF_8));
    String                XMLStringHash = new String(hash);

    //BASE64 ENCODED HASH
    byte[]                hashEncoded = Base64.encodeBase64(hash);
    String                XMLStringHashEncoded = new String(hashEncoded);

    //RETURN HASH
    return XMLStringHashEncoded;

  }

}
