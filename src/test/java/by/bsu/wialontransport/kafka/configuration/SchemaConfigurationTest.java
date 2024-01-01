package by.bsu.wialontransport.kafka.configuration;

import by.bsu.wialontransport.kafka.model.transportable.data.TransportableData;
import by.bsu.wialontransport.kafka.model.transportable.data.TransportableSavedData;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class SchemaConfigurationTest {
    private final SchemaConfiguration configuration = new SchemaConfiguration();

    @Test
    public void reflectDataShouldBeCreated() {
        final ReflectData actual = configuration.reflectData();
        assertNotNull(actual);
    }

    @Test
    public void transportableDataSchemaShouldBeCreated() {
        final ReflectData givenReflectData = mock(ReflectData.class);

        final Schema givenSchema = mock(Schema.class);
        when(givenReflectData.getSchema(same(TransportableData.class))).thenReturn(givenSchema);

        final Schema actual = configuration.transportableDataSchema(givenReflectData);
        assertSame(givenSchema, actual);
    }

    @Test
    public void transportableSavedDataSchemaShouldBeCreated() {
        final ReflectData givenReflectData = mock(ReflectData.class);

        final Schema givenSchema = mock(Schema.class);
        when(givenReflectData.getSchema(same(TransportableSavedData.class))).thenReturn(givenSchema);

        final Schema actual = configuration.transportableSavedDataSchema(givenReflectData);
        assertSame(givenSchema, actual);
    }
}
