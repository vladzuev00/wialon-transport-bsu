package by.vladzuev.locationreceiver.protocol.core.handler.packages.login.factory;

import by.vladzuev.locationreceiver.protocol.core.model.LoginPackage;
import lombok.Value;
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

    @Value
    private static class TestPackage implements LoginPackage {
        String imei;
    }
}
