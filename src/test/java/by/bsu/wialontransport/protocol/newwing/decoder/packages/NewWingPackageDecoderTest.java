//package by.bsu.wialontransport.protocol.newwing.decoder.packages;
//
//import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingRequestPackage;
//import by.bsu.wialontransport.protocol.newwing.model.packages.request.builder.NewWingRequestPackageBuilder;
//import io.netty.buffer.ByteBuf;
//import lombok.Getter;
//import lombok.Setter;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class NewWingPackageDecoderTest {
//    private static final String GIVEN_PACKAGE_PREFIX = "#TEST#";
//
//    private final TestNewWingPackageDecoder decoder = new TestNewWingPackageDecoder(GIVEN_PACKAGE_PREFIX);
//
//    @Test
//    public void bufferShouldBeDecoded() {
//        final ByteBuf givenBuffer = mock(ByteBuf.class);
//
//        final int givenChecksum = 12345;
//        when(givenBuffer.readIntLE()).thenReturn(givenChecksum);
//
//        final NewWingRequestPackage actual = decoder.decode(givenBuffer);
//        final TestNewWingPackage expected = new TestNewWingPackage(givenChecksum);
//        assertEquals(expected, actual);
//
//        assertTrue(decoder.isDecodedUntilChecksum());
//    }
//
//    private static final class TestNewWingPackage extends NewWingRequestPackage {
//
//        public TestNewWingPackage(final int checksum) {
//            super(checksum);
//        }
//
//    }
//
//    private static final class TestNewWingPackageBuilder extends NewWingRequestPackageBuilder<TestNewWingPackage> {
//
//        @Override
//        protected TestNewWingPackage build(final int checksum) {
//            return new TestNewWingPackage(checksum);
//        }
//    }
//
//    @Setter
//    @Getter
//    private static final class TestNewWingPackageDecoder
//            extends NewWingPackageDecoder<TestNewWingPackage, TestNewWingPackageBuilder> {
//        private boolean decodedUntilChecksum;
//
//        public TestNewWingPackageDecoder(final String packagePrefix) {
//            super(packagePrefix);
//        }
//
//        @Override
//        protected TestNewWingPackageBuilder createPackageBuilder() {
//            return new TestNewWingPackageBuilder();
//        }
//
//        @Override
//        protected void decodeUntilChecksum(final ByteBuf buffer, final TestNewWingPackageBuilder packageBuilder) {
//            decodedUntilChecksum = true;
//        }
//    }
//}