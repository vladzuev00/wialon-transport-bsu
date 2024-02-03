package by.bsu.wialontransport.protocol.it.core;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.base.kafka.TestKafkaSavedDataConsumer;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.kafka.consumer.data.KafkaSavedDataConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
@Sql("classpath:sql/protocol-it/before-test.sql")
@Sql(value = "classpath:sql/protocol-it/after-test.sql", executionPhase = AFTER_TEST_METHOD)
public abstract class ProtocolIT extends AbstractContextTest {
    private static final int WAIT_DATA_DELIVERING_IN_SECONDS = 7;

    protected static final AddressEntity GIVEN_EXISTING_ADDRESS = AddressEntity.builder()
            .id(103L)
            .build();
    protected static final TrackerEntity GIVEN_EXISTING_TRACKER = TrackerEntity.builder()
            .id(255L)
            .imei("11112222333344445555")
            .password("password")
            .build();

    @MockBean
    private KafkaSavedDataConsumer mockedKafkaSavedConsumer;    //to turn off

    @Autowired
    protected TestKafkaSavedDataConsumer savedDataConsumer;

    protected boolean waitDataDeliveringAndReturnDeliveredOrNot()
            throws InterruptedException {
        return savedDataConsumer
                .getCountDownLatch()
                .await(WAIT_DATA_DELIVERING_IN_SECONDS, SECONDS);
    }

    protected List<DataEntity> findAllDataOrderedById() {
        return entityManager.createQuery("SELECT e FROM DataEntity e ORDER BY e.id", DataEntity.class)
                .getResultList();
    }
}
