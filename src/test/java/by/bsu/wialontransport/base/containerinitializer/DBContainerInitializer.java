package by.bsu.wialontransport.base.containerinitializer;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

import static org.testcontainers.utility.DockerImageName.parse;

public final class DBContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final String TEST_PROPERTY_KEY_DATASOURCE_URL = "spring.datasource.url";
    private static final String TEST_PROPERTY_KEY_USERNAME = "spring.datasource.username";
    private static final String TEST_PROPERTY_KEY_PASSWORD = "spring.datasource.password";

    private static final String FULL_IMAGE_NAME = "mdillon/postgis:9.5";
    private static final String OTHER_IMAGE_NAME = "postgres";
    private static final DockerImageName DOCKER_IMAGE_NAME = parse(FULL_IMAGE_NAME)
            .asCompatibleSubstituteFor(OTHER_IMAGE_NAME);

    private static final String DATA_BASE_NAME = "integration-tests-db";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "sa";

    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(DOCKER_IMAGE_NAME)
            .withDatabaseName(DATA_BASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    static {
        POSTGRE_SQL_CONTAINER.start();
    }

    @Override
    public void initialize(final ConfigurableApplicationContext context) {
        TestPropertyValues.of(
                Map.of(
                        TEST_PROPERTY_KEY_DATASOURCE_URL, POSTGRE_SQL_CONTAINER.getJdbcUrl(),
                        TEST_PROPERTY_KEY_USERNAME, POSTGRE_SQL_CONTAINER.getUsername(),
                        TEST_PROPERTY_KEY_PASSWORD, POSTGRE_SQL_CONTAINER.getPassword()
                )
        ).applyTo(context.getEnvironment());
    }
}