package by.bsu.wialontransport.configuration.property;

import by.bsu.wialontransport.base.AbstractContextTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public final class DataValidationPropertyTest extends AbstractContextTest {

    @Autowired
    private DataValidationProperty property;

    @Test
    public void propertyShouldBeInitialized() {
        final DataValidationProperty expected = DataValidationProperty.builder()
                .minValidAmountOfSatellites(3)
                .maxValidAmountSatellites(999)
                .minValidDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .deltaSecondsFromNowMaxAllowableValidDateTime(15)
                .minValidDOP(1)
                .maxValidDOP(7)
                .build();
        assertEquals(expected, this.property);
    }
}
