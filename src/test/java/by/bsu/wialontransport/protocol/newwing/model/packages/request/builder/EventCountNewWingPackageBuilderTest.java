package by.bsu.wialontransport.protocol.newwing.model.packages.request.builder;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingEventCountPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class EventCountNewWingPackageBuilderTest {

    @Test
    public void packageShouldBeBuilt() {
        final EventCountNewWingPackageBuilder givenBuilder = new EventCountNewWingPackageBuilder();

        final short givenEventCount = 20;
        givenBuilder.setEventCount(givenEventCount);

        final short givenFrameEventCount = 30;
        givenBuilder.setFrameEventCount(givenFrameEventCount);

        final int givenChecksum = 100;

        final NewWingEventCountPackage actual = givenBuilder.build(givenChecksum);
        final NewWingEventCountPackage expected = new NewWingEventCountPackage(
                givenChecksum,
                givenEventCount,
                givenFrameEventCount
        );
        assertEquals(expected, actual);
    }
}