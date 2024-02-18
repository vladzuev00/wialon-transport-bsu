package by.bsu.wialontransport.base;

import by.bsu.wialontransport.base.containerinitializer.DBContainerInitializer;
import by.bsu.wialontransport.base.containerinitializer.KafkaContainerInitializer;
import com.yannbriancon.interceptor.HibernateQueryInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static java.time.ZoneOffset.UTC;
import static java.util.TimeZone.getTimeZone;
import static java.util.TimeZone.setDefault;
import static org.junit.Assert.assertEquals;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {DBContainerInitializer.class, KafkaContainerInitializer.class})
public abstract class AbstractSpringBootTest {

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    protected HibernateQueryInterceptor queryInterceptor;

    @BeforeClass
    public static void setDefaultTimeZone() {
        setDefault(getTimeZone(UTC));
    }

    protected final void startQueryCount() {
        log.info("======================= START QUERY COUNTER ====================================");
        queryInterceptor.startQueryCount();
    }

    protected final long getQueryCount() {
        return queryInterceptor.getQueryCount();
    }

    protected final void checkQueryCount(final long expected) {
        entityManager.flush();
        log.info("======================= FINISH QUERY COUNTER ====================================");
        assertEquals("wrong count of queries", expected, getQueryCount());
    }
}
