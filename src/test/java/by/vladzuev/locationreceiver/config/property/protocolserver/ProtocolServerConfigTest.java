//package by.vladzuev.locationreceiver.config.property.protocolserver;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import by.vladzuev.locationreceiver.util.ConstraintViolationUtil;
//import lombok.Builder;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validator;
//import java.util.Set;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public final class ProtocolServerConfigTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private Validator validator;
//
//    @Test
//    public void configShouldBeValid() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("localhost")
//                .port(8080)
//                .threadCountProcessingConnection(5)
//                .threadCountProcessingData(10)
//                .connectionLifeTimeoutSeconds(50)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertTrue(constraints.isEmpty());
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfNotValidHost() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("      ")
//                .port(8080)
//                .threadCountProcessingConnection(5)
//                .threadCountProcessingData(10)
//                .connectionLifeTimeoutSeconds(50)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertEquals(1, constraints.size());
//        Assert.assertEquals("Invalid host", ConstraintViolationUtil.findFirstMessage(constraints));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfPortIsNull() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("localhost")
//                .threadCountProcessingConnection(5)
//                .threadCountProcessingData(10)
//                .connectionLifeTimeoutSeconds(50)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertEquals(1, constraints.size());
//        Assert.assertEquals("must not be null", ConstraintViolationUtil.findFirstMessage(constraints));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfPortIsLessThanMinValid() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("localhost")
//                .port(0)
//                .threadCountProcessingConnection(5)
//                .threadCountProcessingData(10)
//                .connectionLifeTimeoutSeconds(50)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertEquals(1, constraints.size());
//        Assert.assertEquals("must be greater than or equal to 1", ConstraintViolationUtil.findFirstMessage(constraints));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfPortIsMoreThanMaxValid() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("localhost")
//                .port(65536)
//                .threadCountProcessingConnection(5)
//                .threadCountProcessingData(10)
//                .connectionLifeTimeoutSeconds(50)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertEquals(1, constraints.size());
//        Assert.assertEquals("must be less than or equal to 65535", ConstraintViolationUtil.findFirstMessage(constraints));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfThreadCountProcessingConnectionIsNull() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("localhost")
//                .port(8080)
//                .threadCountProcessingData(10)
//                .connectionLifeTimeoutSeconds(50)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertEquals(1, constraints.size());
//        Assert.assertEquals("must not be null", ConstraintViolationUtil.findFirstMessage(constraints));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfThreadCountProcessingConnectionIsLessThanMinValid() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("localhost")
//                .port(8080)
//                .threadCountProcessingConnection(0)
//                .threadCountProcessingData(10)
//                .connectionLifeTimeoutSeconds(50)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertEquals(1, constraints.size());
//        Assert.assertEquals("must be greater than or equal to 1", ConstraintViolationUtil.findFirstMessage(constraints));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfThreadCountProcessingConnectionIsMoreThanMaxValid() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("localhost")
//                .port(8080)
//                .threadCountProcessingConnection(256)
//                .threadCountProcessingData(10)
//                .connectionLifeTimeoutSeconds(50)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertEquals(1, constraints.size());
//        Assert.assertEquals("must be less than or equal to 255", ConstraintViolationUtil.findFirstMessage(constraints));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfThreadCountProcessingDataIsNull() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("localhost")
//                .port(8080)
//                .threadCountProcessingConnection(5)
//                .connectionLifeTimeoutSeconds(50)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertEquals(1, constraints.size());
//        Assert.assertEquals("must not be null", ConstraintViolationUtil.findFirstMessage(constraints));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfThreadCountProcessingDataIsLessThanMinValid() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("localhost")
//                .port(8080)
//                .threadCountProcessingConnection(5)
//                .threadCountProcessingData(0)
//                .connectionLifeTimeoutSeconds(50)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertEquals(1, constraints.size());
//        Assert.assertEquals("must be greater than or equal to 1", ConstraintViolationUtil.findFirstMessage(constraints));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfThreadCountProcessingDataIsMoreThanMaxValid() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("localhost")
//                .port(8080)
//                .threadCountProcessingConnection(5)
//                .threadCountProcessingData(256)
//                .connectionLifeTimeoutSeconds(50)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertEquals(1, constraints.size());
//        Assert.assertEquals("must be less than or equal to 255", ConstraintViolationUtil.findFirstMessage(constraints));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfConnectionLifeTimeoutSecondsIsNull() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("localhost")
//                .port(8080)
//                .threadCountProcessingConnection(5)
//                .threadCountProcessingData(10)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertEquals(1, constraints.size());
//        Assert.assertEquals("must not be null", ConstraintViolationUtil.findFirstMessage(constraints));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfConnectionLifeTimeoutSecondsIsLessThanMinValid() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("localhost")
//                .port(8080)
//                .threadCountProcessingConnection(5)
//                .threadCountProcessingData(10)
//                .connectionLifeTimeoutSeconds(0)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertEquals(1, constraints.size());
//        Assert.assertEquals("must be greater than or equal to 1", ConstraintViolationUtil.findFirstMessage(constraints));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfConnectionLifeTimeoutSecondsIsMoreThanMaxValid() {
//        final TestProtocolServerConfig givenConfig = TestProtocolServerConfig.builder()
//                .host("localhost")
//                .port(8080)
//                .threadCountProcessingConnection(5)
//                .threadCountProcessingData(10)
//                .connectionLifeTimeoutSeconds(601)
//                .build();
//
//        final Set<ConstraintViolation<TestProtocolServerConfig>> constraints = validator.validate(givenConfig);
//        assertEquals(1, constraints.size());
//        Assert.assertEquals("must be less than or equal to 600", ConstraintViolationUtil.findFirstMessage(constraints));
//    }
//
//    private static final class TestProtocolServerConfig extends ProtocolServerConfig {
//
//        @Builder
//        public TestProtocolServerConfig(final String host,
//                                        final Integer port,
//                                        final Integer threadCountProcessingConnection,
//                                        final Integer threadCountProcessingData,
//                                        final Integer connectionLifeTimeoutSeconds) {
//            super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
//        }
//    }
//}
