package by.bsu.wialontransport.protocol.it.core;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.base.kafka.TestKafkaSavedDataConsumer;
import by.bsu.wialontransport.crud.entity.*;
import by.bsu.wialontransport.kafka.consumer.data.KafkaSavedDataConsumer;
import by.bsu.wialontransport.service.nominatim.NominatimService;
import lombok.Getter;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
@Sql("classpath:sql/protocol-it/before-test.sql")
@Sql(value = "classpath:sql/protocol-it/after-test.sql", executionPhase = AFTER_TEST_METHOD)
public abstract class ProtocolIT extends AbstractSpringBootTest {
    protected static final AddressEntity GIVEN_EXISTING_ADDRESS = AddressEntity.builder()
            .id(102L)
            .build();

    @MockBean
    @SuppressWarnings("unused")
    private KafkaSavedDataConsumer mockedKafkaSavedConsumer;    //to turn off

    @MockBean
    @Getter(PROTECTED)
    private NominatimService mockedNominatimService;

    @Autowired
    private TestKafkaSavedDataConsumer savedDataConsumer;

    @Before
    public void resetSavedDataConsumer() {
        savedDataConsumer.reset();
    }

    @SuppressWarnings("SameParameterValue")
    protected void resetSavedDataConsumer(final int consumedRecordCount) {
        savedDataConsumer.reset(consumedRecordCount);
    }

    protected boolean isSuccessDataDelivering() {
        return savedDataConsumer.isSuccessConsuming();
    }

    protected String getKafkaSavedDataConsumerPayload() {
        return savedDataConsumer.getPayload();
    }

    protected List<DataEntity> findDataFetchingParametersAndTrackerAndAddressOrderedById() {
        return entityManager.createQuery("SELECT DISTINCT e FROM DataEntity e LEFT JOIN FETCH e.parameters JOIN FETCH e.tracker JOIN FETCH e.address ORDER BY e.id", DataEntity.class)
                .getResultList();
    }

    protected List<ParameterEntity> findParametersFetchingDataOrderedById() {
        return entityManager.createQuery("SELECT e FROM ParameterEntity e JOIN FETCH e.data ORDER BY e.id", ParameterEntity.class)
                .getResultList();
    }

    protected TrackerMileageEntity findTrackerMileage(final TrackerEntity tracker) {
        return entityManager.createQuery("SELECT e.mileage FROM TrackerEntity e WHERE e.id = :trackerId", TrackerMileageEntity.class)
                .setParameter("trackerId", tracker.getId())
                .getSingleResult();
    }

    protected void verifyNoRequestsToNominatim() {
        verifyNoInteractions(mockedNominatimService);
    }
}
