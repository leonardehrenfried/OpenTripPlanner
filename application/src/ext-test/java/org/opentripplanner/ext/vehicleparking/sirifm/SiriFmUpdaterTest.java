package org.opentripplanner.ext.vehicleparking.sirifm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.File;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.opentripplanner.test.support.ResourceLoader;
import org.opentripplanner.updater.spi.HttpHeaders;
import uk.org.siri.siri21.AccessFacilityEnumeration;
import uk.org.siri.siri21.AllFacilitiesFeatureStructure;
import uk.org.siri.siri21.FacilityConditionStructure;
import uk.org.siri.siri21.FacilityMonitoringDeliveryStructure;
import uk.org.siri.siri21.FacilityRef;
import uk.org.siri.siri21.FacilityStatusEnumeration;
import uk.org.siri.siri21.FacilityStatusStructure;
import uk.org.siri.siri21.FacilityStructure;
import uk.org.siri.siri21.FacilityStructure.Features;
import uk.org.siri.siri21.ServiceDelivery;
import uk.org.siri.siri21.Siri;

class SiriFmUpdaterTest {

  @Test
  void parse() {
    var uri = ResourceLoader.of(this).uri("siri-fm.xml");
    var parameters = new SiriFmUpdaterParameters(
      "noi",
      uri,
      "noi",
      Duration.ofSeconds(30),
      HttpHeaders.empty()
    );
    var updater = new SiriFmDataSource(parameters);
    updater.update();
    var updates = updater.getUpdates();

    assertEquals(4, updates.size());
  }

  @Test
  void closedElevator() throws JAXBException {
    var ref = new FacilityRef();
    ref.setValue("elevator-123");

    var structure = new FacilityConditionStructure();
    var facility = new FacilityStructure();
    facility.setFacilityCode("elevator-123");
    var e = new AllFacilitiesFeatureStructure();
    e.setAccessFacility(AccessFacilityEnumeration.LIFT);
    var features = new Features();
    features.getFeatures().add(e);
    facility.setFeatures(features);
    structure.setFacility(facility);
    structure.setFacilityRef(ref);
    var status = new FacilityStatusStructure();
    status.setStatus(FacilityStatusEnumeration.NOT_AVAILABLE);
    structure.setFacilityStatus(status);

    var fm = new FacilityMonitoringDeliveryStructure();
    fm.getFacilityConditions().add(structure);

    var siri = new Siri();
    var serviceDelivery = new ServiceDelivery();
    serviceDelivery.getFacilityMonitoringDeliveries().add(fm);
    siri.setServiceDelivery(serviceDelivery);

    var context = JAXBContext.newInstance(Siri.class);
    var mar= context.createMarshaller();
    mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    mar.marshal(siri, new File("book.xml"));
  }
}
