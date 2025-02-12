package by.bsu.wialontransport.protocol.core.handler.packages.login.factory;

import by.bsu.wialontransport.protocol.core.model.login.LoginPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TrackerImeiFactoryTest {
    private final TrackerImeiFactory factory = new TrackerImeiFactory();

    @Test
    public void imeiShouldBeCreated() {
        final LoginPackage givenRequest = new TestPackage("2323");

        final String actual = factory.create(givenRequest);
        final String expected = "00000000000000002323";
        assertEquals(expected, actual);
    }

    private static final class TestPackage extends LoginPackage {

        public TestPackage(final String imei) {
            super(imei);
        }
    }
}
