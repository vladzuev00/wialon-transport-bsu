package by.bsu.wialontransport.config;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.config.GeometryFactoryConfig.SRID;
import static org.junit.Assert.*;

public final class GeometryFactoryConfigTest extends AbstractSpringBootTest {

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private PrecisionModel precisionModel;

    @Test
    public void geometryFactoryShouldBeCreated() {
        assertNotNull(geometryFactory);

        final PrecisionModel actualPrecisionModel = geometryFactory.getPrecisionModel();
        assertSame(precisionModel, actualPrecisionModel);

        final int actualSRID = geometryFactory.getSRID();
        assertEquals(SRID, actualSRID);
    }

    @Test
    public void precisionModelShouldBeCreated() {
        final PrecisionModel expected = new PrecisionModel();
        assertEquals(expected, precisionModel);
    }
}
