//package by.vladzuev.locationreceiver.config.property.protocolserver;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static org.junit.Assert.assertEquals;
//
//public final class WialonProtocolServerConfigTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private WialonProtocolServerConfig config;
//
//    @Test
//    public void configShouldBeCreated() {
//        final WialonProtocolServerConfig expected = WialonProtocolServerConfig.builder()
//                .host("localhost")
//                .port(6002)
//                .threadCountProcessingConnection(1)
//                .threadCountProcessingData(10)
//                .connectionLifeTimeoutSeconds(300)
//                .build();
//        assertEquals(expected, config);
//    }
//}
