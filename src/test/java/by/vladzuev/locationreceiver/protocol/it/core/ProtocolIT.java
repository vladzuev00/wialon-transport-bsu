//package by.vladzuev.locationreceiver.protocol.it.core;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import by.vladzuev.locationreceiver.kafka.consumer.data.KafkaSavedDataConsumer;
//import by.vladzuev.locationreceiver.service.nominatim.NominatimService;
//import by.vladzuev.locationreceiver.service.nominatim.model.NominatimReverseResponse;
//import by.vladzuev.locationreceiver.crud.entity.*;
//import lombok.Getter;
//import org.junit.Before;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.transaction.annotation.Transactional;
//import org.wololo.jts2geojson.GeoJSONWriter;
//
//import java.util.List;
//
//import static by.vladzuev.locationreceiver.util.GeometryTestUtil.createPolygon;
//import static lombok.AccessLevel.PROTECTED;
//import static org.mockito.Mockito.verifyNoInteractions;
//import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
//import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;
//
//@Transactional(propagation = NOT_SUPPORTED)
//@Sql("classpath:sql/protocol-it/before-test.sql")
//@Sql(value = "classpath:sql/protocol-it/after-test.sql", executionPhase = AFTER_TEST_METHOD)
//public abstract class ProtocolIT extends AbstractSpringBootTest {
//    protected static final AddressEntity GIVEN_EXISTING_ADDRESS = AddressEntity.builder()
//            .id(102L)
//            .build();
//
//    @MockBean
//    @SuppressWarnings("unused")
//    private KafkaSavedDataConsumer mockedKafkaSavedConsumer;    //to turn off
//
//    @MockBean
//    @Getter(PROTECTED)
//    private NominatimService mockedNominatimService;
//
//    @Autowired
//    private KafkaSavedDataAccumulatingPayloadConsumer savedDataConsumer;
//
//    @Autowired
//    private GeometryFactory geometryFactory;
//
//    @Autowired
//    private GeoJSONWriter geoJSONWriter;
//
//    @Before
//    public void resetSavedDataConsumer() {
////        savedDataConsumer.reset();
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    protected void resetSavedDataConsumer(final int consumedRecordCount) {
//        savedDataConsumer.expect(consumedRecordCount);
//    }
//
//    protected boolean isSuccessDataDelivering() {
//        return savedDataConsumer.isSuccessConsuming();
//    }
//
//    protected String getKafkaSavedDataConsumerPayload() {
//        return savedDataConsumer.getPayload();
//    }
//
//    protected List<DataEntity> findDataFetchingParametersAndTrackerAndAddressOrderedById() {
//        return entityManager.createQuery("SELECT DISTINCT e FROM DataEntity e LEFT JOIN FETCH e.parameters JOIN FETCH e.tracker JOIN FETCH e.address ORDER BY e.id", DataEntity.class)
//                .getResultList();
//    }
//
//    protected List<ParameterEntity> findParametersFetchingDataOrderedById() {
//        return entityManager.createQuery("SELECT e FROM ParameterEntity e JOIN FETCH e.data ORDER BY e.id", ParameterEntity.class)
//                .getResultList();
//    }
//
//    protected TrackerMileageEntity findTrackerMileage(final TrackerEntity tracker) {
//        return entityManager.createQuery("SELECT e.mileage FROM TrackerEntity e WHERE e.id = :trackerId", TrackerMileageEntity.class)
//                .setParameter("trackerId", tracker.getId())
//                .getSingleResult();
//    }
//
//    protected void verifyNoRequestsToNominatim() {
//        verifyNoInteractions(mockedNominatimService);
//    }
//
//    protected NominatimReverseResponse createNominatimReverseResponse() {
//        return NominatimReverseResponse.builder()
//                .centerLatitude(5.5)
//                .centerLongitude(6.6)
//                .address(new NominatimReverseResponse.Address("city", "town", "country"))
//                .boundingBoxCoordinates(new double[]{4.4, 5.5, 6.6, 7.7})
//                .geometry(
//                        geoJSONWriter.write(
//                                createPolygon(
//                                        geometryFactory,
//                                        4.4, 5.5, 8.8, 5.5, 8.8, 9.9, 4.4, 9.9
//                                )
//                        )
//                )
//                .extraTags(new NominatimReverseResponse.ExtraTags("city", "yes"))
//                .build();
//    }
//
//    protected static AddressEntity createAddress(final Long id) {
//        return AddressEntity.builder()
//                .id(id)
//                .build();
//    }
//}
