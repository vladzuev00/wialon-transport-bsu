package by.vladzuev.locationreceiver.base.containerinitializer;

import com.redis.testcontainers.RedisContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;

public final class RedisContainerInitializer extends ContainerInitializer<RedisContainer> {
    private static final String IMAGE_NAME = "redis:latest";
    private static final String KEY_HOST = "spring.data.redis.host";
    private static final String KEY_PORT = "spring.data.redis.port";
    private static final Integer ORIGINAL_PORT = 6379;

    @Override
    protected String getImageName() {
        return IMAGE_NAME;
    }

    @Override
    protected Optional<String> getOtherImageName() {
        return empty();
    }

    @Override
    protected RedisContainer createContainer(final DockerImageName imageName) {
        return new RedisContainer(imageName);
    }

    @Override
    protected void configure(final RedisContainer container) {
        container.withExposedPorts(ORIGINAL_PORT);
    }

    @Override
    protected Map<String, String> getPropertiesByKeys(final RedisContainer container) {
        final Integer mappedPort = container.getMappedPort(ORIGINAL_PORT);
        return Map.of(KEY_HOST, container.getHost(), KEY_PORT, mappedPort.toString());
    }
}
