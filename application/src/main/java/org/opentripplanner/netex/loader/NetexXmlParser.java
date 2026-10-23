package org.opentripplanner.netex.loader;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventLocator;
import java.io.InputStream;
import org.glassfish.jaxb.runtime.v2.runtime.Coordinator;
import org.rutebanken.netex.model.PublicationDeliveryStructure;

/** Simple wrapper to perform typesafe xml parsing and simple error handling. */
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

    try (var ignored = ThreadLocalGuard.activate()) {
      //noinspection unchecked
      root = (JAXBElement<PublicationDeliveryStructure>) unmarshaller.unmarshal(stream);
    }

    return root.getValue();
  }

  /** factory method for unmarshaller */
  private static Unmarshaller createUnmarshaller() {
    try {
      return JAXBContext.newInstance(PublicationDeliveryStructure.class).createUnmarshaller();
    } catch (JAXBException e) {
      // This is a programming error - not expected!
      // We abort early and also allow for this to happen in the constructor;
      // Which in other cases would be considered bad practice.
      throw new RuntimeException(e);
    }
  }

  /**
   * The JAXB runtime registers its unmarshalling context in a {@link ThreadLocal} for every SAX
   * event (start element, text, end element) and removes it again afterwards if no other
   * {@link Coordinator} was registered before. Removing and re-adding the thread-local entry for
   * every XML element allocates a new map entry each time and makes the thread-local map clean up
   * stale slots, which is a significant part of the parse time for large NeTEx files.
   * <p>
   * Registering this placeholder for the duration of the parse means the JAXB runtime only swaps
   * the value of an existing entry instead. The placeholder is never used while JAXB is processing
   * an element, since the unmarshalling context then registers itself.
   */
  private static class ThreadLocalGuard extends Coordinator implements AutoCloseable {

    static ThreadLocalGuard activate() {
      var guard = new ThreadLocalGuard();
      guard.pushCoordinator();
      return guard;
    }

    @Override
    public void close() {
      popCoordinator();
    }

    @Override
    protected ValidationEventLocator getLocation() {
      return null;
    }

    @Override
    public boolean handleEvent(ValidationEvent event) {
      return false;
    }
  }
}
