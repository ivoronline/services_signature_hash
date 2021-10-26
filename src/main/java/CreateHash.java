import org.w3c.dom.Document;

public class CreateHash {

  //================================================================================
  // MAIN
  //================================================================================
  public static void main(String[] args) throws Exception {

    //GET HASH FOR DOCUMENT
    Document XMLDocument1    = UtilXML.fileToDocument("Person.xml");
    String   XMLElementHash  = UtilSignature.getBase64EncodedHashForDocument(XMLDocument1);
    System.out.println(XMLElementHash);

    //GET HASH FOR ELEMENT
    Document XMLDocument2    = UtilXML.fileToDocument("Envelope.xml");
    String   XMLDocumentHash = UtilSignature.getBase64EncodedHashForElement(XMLDocument2, "Person");
    System.out.println(XMLDocumentHash);

  }

}
