package by.bsu.wialontransport.config;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import static org.junit.Assert.assertNotNull;

public final class GeoJsonConfigTest extends AbstractSpringBootTest {

    @Autowired
    private GeoJSONReader geoJSONReader;

    @Autowired
    private GeoJSONWriter geoJSONWriter;

    @Test
    public void geoJSONReaderShouldBeCreated() {
        assertNotNull(geoJSONReader);
    }

    @Test
    public void geoJSONWriterShouldBeCreated() {
        assertNotNull(geoJSONWriter);
    }
}
