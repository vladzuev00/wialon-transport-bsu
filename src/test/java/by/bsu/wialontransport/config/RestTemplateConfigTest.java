package by.bsu.wialontransport.config;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.springframework.http.MediaType.ALL;

public final class RestTemplateConfigTest extends AbstractSpringBootTest {

    @Autowired
    private JtsModule jtsModule;

    @Autowired
    private JavaTimeModule javaTimeModule;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void jtsModuleShouldBeCreated() {
        assertNotNull(jtsModule);
    }

    @Test
    public void javaTimeModuleShouldBeCreated() {
        assertNotNull(javaTimeModule);
    }

    @Test
    public void objectMapperShouldBeCreated() {
        assertNotNull(objectMapper);

        final Set<Object> actualModuleIds = objectMapper.getRegisteredModuleIds();
        final Set<Object> expectedModuleIds = Set.of("jackson-datatype-jts", "jackson-datatype-jsr310");
        assertEquals(expectedModuleIds, actualModuleIds);

        final int actualSerializationFeatures = objectMapper.getSerializationConfig().getSerializationFeatures();
        final int expectedSerializationFeatures = 21770556;
        assertEquals(expectedSerializationFeatures, actualSerializationFeatures);
    }

    @Test
    public void messageConverterShouldBeCreated() {
        assertNotNull(messageConverter);

        final List<MediaType> actualMediaTypes = messageConverter.getSupportedMediaTypes();
        final List<MediaType> expectedMediaTypes = singletonList(ALL);
        assertEquals(expectedMediaTypes, actualMediaTypes);

        final ObjectMapper actualObjectMapper = messageConverter.getObjectMapper();
        assertSame(objectMapper, actualObjectMapper);
    }

    @Test
    public void restTemplateShouldBeCreated() {
        assertNotNull(restTemplate);

        final List<HttpMessageConverter<?>> actualMessageConverters = restTemplate.getMessageConverters();
        assertTrue(actualMessageConverters.contains(messageConverter));
    }
}
