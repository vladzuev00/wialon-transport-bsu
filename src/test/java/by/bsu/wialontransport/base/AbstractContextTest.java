package by.bsu.wialontransport.base;

import com.yannbriancon.interceptor.HibernateQueryInterceptor;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static java.lang.System.out;
import static java.util.TimeZone.getTimeZone;
import static java.util.TimeZone.setDefault;
import static org.junit.Assert.assertEquals;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {AbstractContextTest.DBContainerInitializer.class})
public abstract class AbstractContextTest {
    @SuppressWarnings("resource")
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    static {
        postgreSQLContainer.start();
    }

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    protected HibernateQueryInterceptor queryInterceptor;

    @BeforeClass
    public static void setDefaultTimeZone() {
        setDefault(getTimeZone("UTC"));
    }

    protected final void startQueryCount() {
        out.println("======================= START QUERY COUNTER ====================================");
        this.queryInterceptor.startQueryCount();
    }

    protected final Long getQueryCount() {
        return this.queryInterceptor.getQueryCount();
    }

    protected final void checkQueryCount(int expected) {
        this.entityManager.flush();
        out.println("======================= FINISH QUERY COUNTER ====================================");
        assertEquals("wrong count of queries", Long.valueOf(expected), this.getQueryCount());
    }

    static class DBContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
