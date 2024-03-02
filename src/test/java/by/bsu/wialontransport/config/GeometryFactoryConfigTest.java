package by.bsu.wialontransport.config;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class GeometryFactoryConfigTest extends AbstractSpringBootTest {

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private PrecisionModel precisionModel;

    @Test
    public void geometryFactoryShouldBeCreated() {
        assertNotNull(geometryFactory);

        final PrecisionModel actualPrecisionModel = geometryFactory.getPrecisionModel();
        final PrecisionModel expectedPrecisionModel = new PrecisionModel();
        assertEquals(expectedPrecisionModel, actualPrecisionModel);

        final int actualSRID = geometryFactory.getSRID();
        final int expectedSRID = 4326;
        assertEquals(expectedSRID, actualSRID);
    }

    @Test
    public void precisionModelShouldBeCreated() {
        final PrecisionModel expected = new PrecisionModel();
        assertEquals(expected, precisionModel);
    }
}
