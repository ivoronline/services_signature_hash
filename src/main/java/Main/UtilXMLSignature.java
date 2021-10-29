package Main;

import net.sf.saxon.TransformerFactoryImpl;
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
import java.util.Collections;

public class UtilXMLSignature {

  //================================================================================
  // GET HASH FOR DOCUMENT
  //================================================================================
  public static byte[] getHashForDocument(Document XMLDocument, String algorithm) throws Exception {

    //CONVERT XML DOCUMENT TO XML STRING
    String XMLString      = transformDocumentToString(XMLDocument);
    String XMLStringCanon = canonicalizeXMLString(XMLString);
    byte[] hash           = UtilSignature.hash(algorithm, XMLStringCanon.getBytes(StandardCharsets.UTF_8));

    //RETURN HASH
    return hash;

  }

  //================================================================================
  // GET HASH FOR ELEMENT
  //================================================================================
  public static byte[] getHashForElement(Document XMLDocument, String tagName, String algorithm) throws Exception {

    //GET XML ELEMENT
    Node   node           = XMLDocument.getElementsByTagName(tagName).item(0);

    //CONVERT XML DOCUMENT TO XML STRING
    String XMLString      = transformDocumentToString(UtilXML.nodeToDocument(node));
    String XMLStringCanon = canonicalizeXMLString(XMLString);
    byte[] hash           = UtilSignature.hash(algorithm, XMLStringCanon.getBytes(StandardCharsets.UTF_8));

    //RETURN HASH
    return hash;

  }

  //================================================================================
  // TRANSFORM DOCUMENT TO STRING
  //================================================================================
  public static String transformDocumentToString(Document XMLDocument) throws Exception {

    //TRANSFORM XML DOCUMENT TO XML STRING
    StringWriter          stringWriter = new StringWriter();

    TransformerFactory    transformerFactory = new TransformerFactoryImpl();

    Transformer           transformer = transformerFactory.newTransformer();
                          transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                          transformer.setOutputProperty(OutputKeys.METHOD              , "xml");
                          transformer.setOutputProperty(OutputKeys.ENCODING            , "UTF-8");
                          transformer.transform(new DOMSource(XMLDocument), new StreamResult(stringWriter));

    String                XMLString = stringWriter.toString();

    //RETURN STRING
    return XMLString;

  }

  //================================================================================
  // CANONICALIZE XML STRING
  //================================================================================
  public static String canonicalizeXMLString(String XMLString) throws Exception {

    //CANONICALIZE XML STRING
    Init.init();
    ByteArrayOutputStream out   = new ByteArrayOutputStream();
    Canonicalizer         canon = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
                          canon.canonicalize(XMLString.getBytes(), out, false);
    String                XMLStringCanon = out.toString();

    //DISPLAYED CANONICALIZED STRING
    //System.out.println(XMLStringCanon);

    //RETURN CANONICALIZE STRING
    return XMLStringCanon;

  }

  //================================================================================
  // SIGN DOCUMENT
  //================================================================================
  // Test.UtilKeys.signDocument (document, privateKey, "Person", "data", DigestMethod.SHA1, SignatureMethod.RSA_SHA1);
  // <Person Id="data">
  public static void signDocument (
    Document   document,        //RETURN VALUE
    Key        privateKey,      //Private Key used to sign XML Element
    String     elementName,     //"Person"     Element to Sign
    String     referenceURI,    //"#data"
    String     digestMethod,    //DigestMethod.SHA1
    String     signatureMethod  //SignatureMethod.RSA_SHA1
  ) throws Exception {

    //CREATE SIGNATURE FACTORY
    XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM");

    //GET REFERENCE
    Reference reference = factory.newReference(
      referenceURI,
      factory.newDigestMethod(digestMethod, null),
      Collections.singletonList(factory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
      null,
      null
    );

    //SPECIFY SIGNATURE TYPE
    SignedInfo signedInfo = factory.newSignedInfo(
      factory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,(C14NMethodParameterSpec) null),
      factory.newSignatureMethod(signatureMethod, null),Collections.singletonList(reference)
    );

    //PREPARE SIGN CONTEXT
    DOMSignContext domSignContext = new DOMSignContext(privateKey, document.getElementsByTagName(elementName).item(0));

    //FIX IF referenceURI POINTS TO Id ATTRIBUTE
    if (!referenceURI.equals("") ) {
      Element element = (Element) document.getElementsByTagName(elementName).item(0);
      domSignContext.setIdAttributeNS(element, null, "Id");
    }

    //SIGN DOCUMENT
    XMLSignature signature = factory.newXMLSignature(signedInfo, null);
                 signature.sign(domSignContext);

  }

}
