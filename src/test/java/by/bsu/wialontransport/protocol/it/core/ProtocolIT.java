package by.bsu.wialontransport.protocol.it.core;

import by.bsu.wialontransport.base.kafka.AbstractKafkaContainerTest;
import by.bsu.wialontransport.base.kafka.TestKafkaSavedDataConsumer;
import by.bsu.wialontransport.crud.entity.*;
import by.bsu.wialontransport.kafka.consumer.data.KafkaSavedDataConsumer;
import by.bsu.wialontransport.service.nominatim.NominatimService;
import lombok.Value;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
@Sql("classpath:sql/protocol-it/before-test.sql")
@Sql(value = "classpath:sql/protocol-it/after-test.sql", executionPhase = AFTER_TEST_METHOD)
public abstract class ProtocolIT extends AbstractKafkaContainerTest {
    protected static final AddressEntity GIVEN_EXISTING_ADDRESS = AddressEntity.builder()
            .id(102L)
            .build();

    @MockBean
    @SuppressWarnings("unused")
    private KafkaSavedDataConsumer mockedKafkaSavedConsumer;    //to turn off

    //TODO: do private
    @MockBean
    protected NominatimService mockedNominatimService;

    @Autowired
    private TestKafkaSavedDataConsumer savedDataConsumer;

    @After
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

    //TODO: delete
    protected List<DataEntity> findDataFetchingTrackerAndAddressOrderedById() {
        return entityManager.createQuery("SELECT e FROM DataEntity e JOIN FETCH e.tracker JOIN FETCH e.address ORDER BY e.id", DataEntity.class)
                .getResultList();
    }

    protected List<DataEntity> findDataFetchingParametersAndTrackerAndAddressOrderedById() {
        return entityManager.createQuery("SELECT e FROM DataEntity e LEFT JOIN FETCH e.parameters JOIN FETCH e.tracker JOIN FETCH e.address ORDER BY e.id", DataEntity.class)
                .getResultList();
    }

    //TODO: delete
    protected boolean isMatchingParametersExist(final ParameterView view) {
        return countMatchingParameters(view) >= 1;
    }

    //TODO: delete
    protected long countParameters() {
        return entityManager.createQuery("SELECT COUNT(e) FROM ParameterEntity e", Long.class)
                .getSingleResult();
    }

    protected TrackerMileageEntity findTrackerMileage(final TrackerEntity tracker) {
        return entityManager.createQuery("SELECT e.mileage FROM TrackerEntity e WHERE e.id = :trackerId", TrackerMileageEntity.class)
                .setParameter("trackerId", tracker.getId())
                .getSingleResult();
    }

    //TODO: delete
    private long countMatchingParameters(final ParameterView view) {
        return entityManager.createQuery("SELECT COUNT(e) FROM ParameterEntity e WHERE e.name = :name AND e.type = :type AND e.value = :value AND e.data = :data", Long.class)
                .setParameter("name", view.name)
                .setParameter("type", view.type)
                .setParameter("value", view.value)
                .setParameter("data", view.data)
                .getSingleResult();
    }

    @Value
    protected static class ParameterView {
        String name;
        ParameterEntity.Type type;
        String value;
        DataEntity data;
    }
}
