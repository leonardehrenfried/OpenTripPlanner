package org.opentripplanner.netex.loader;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.InputStream;
import org.rutebanken.netex.model.PublicationDeliveryStructure;

/**
 * Simple wrapper to perform typesafe xml parsing and simple error handling. An instance is not
 * thread-safe, use one per thread.
 */
public class NetexXmlParser {

  /** used to parse the XML. */
  private final Unmarshaller unmarshaller;

  public NetexXmlParser() {
    this.unmarshaller = createUnmarshaller();
  }

  /**
   * Parse an input stream and return the root document type for the given xml file (stream).
   */
  public PublicationDeliveryStructure parseXmlDoc(InputStream stream) throws JAXBException {
    JAXBElement<PublicationDeliveryStructure> root;

    //noinspection unchecked
    root = (JAXBElement<PublicationDeliveryStructure>) unmarshaller.unmarshal(stream);

    return root.getValue();
  }

  /** factory method for unmarshaller */
  private static Unmarshaller createUnmarshaller() {
    try {
      return ContextHolder.CONTEXT.createUnmarshaller();
    } catch (JAXBException e) {
      // This is a programming error - not expected!
      // We abort early and also allow for this to happen in the constructor;
      // Which in other cases would be considered bad practice.
      throw new RuntimeException(e);
    }
  }

  /**
   * The JAXB context is expensive to create and thread-safe, so it is shared by all parsers. An
   * unmarshaller is not thread-safe, so each parser has its own.
   */
  private static class ContextHolder {

    private static final JAXBContext CONTEXT = createContext();

    private static JAXBContext createContext() {
      try {
        return JAXBContext.newInstance(PublicationDeliveryStructure.class);
      } catch (JAXBException e) {
        // This is a programming error - not expected!
        throw new RuntimeException(e);
      }
    }
  }
}
