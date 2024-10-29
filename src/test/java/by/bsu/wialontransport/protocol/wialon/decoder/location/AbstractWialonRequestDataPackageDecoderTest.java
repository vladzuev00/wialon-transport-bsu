//package by.bsu.wialontransport.protocol.wialon.decoder.packages.data;
//
//import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
//import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.WialonMessageParser;
//import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception.NotValidSubMessageException;
//import by.bsu.wialontransport.protocol.wialon.model.WialonData;
//import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
//import by.bsu.wialontransport.protocol.wialon.model.packages.data.request.AbstractWialonRequestDataPackage;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class AbstractWialonRequestDataPackageDecoderTest {
//    private static final String GIVEN_PACKAGE_PREFIX = "#TEST#";
//    private static final WialonPackage GIVEN_NOT_VALID_SUB_MESSAGE_RESPONSE = new WialonPackage() {
//
//        @Override
//        public String findPrefix() {
//            throw new UnsupportedOperationException();
//        }
//    };
//
//    @Mock
//    private WialonMessageParser mockedMessageParser;
//
//    private TestWialonRequestDataPackageDecoder decoder;
//
//    @Before
//    public void initializeDecoder() {
//        decoder = new TestWialonRequestDataPackageDecoder(
//                GIVEN_PACKAGE_PREFIX,
//                mockedMessageParser,
//                GIVEN_NOT_VALID_SUB_MESSAGE_RESPONSE
//        );
//    }
//
//    @Test
//    public void messageShouldBeDecoded() {
//        final String givenMessage = "first|second|third";
//
//        final List<WialonData> givenData = List.of(
//                createData(LocalDate.of(2024, 1, 16)),
//                createData(LocalDate.of(2024, 1, 17)),
//                createData(LocalDate.of(2024, 1, 18))
//        );
//        when(mockedMessageParser.parse(same(givenMessage))).thenReturn(givenData);
//
//        final TestWialonRequestDataPackage actual = decoder.decodeMessage(givenMessage);
//        final TestWialonRequestDataPackage expected = new TestWialonRequestDataPackage(givenData);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void messageShouldNotBeDecodedBecauseOfNotValidSubMessage() {
//        final String givenMessage = "first|second|third";
//        when(mockedMessageParser.parse(same(givenMessage))).thenThrow(NotValidSubMessageException.class);
//
//        boolean exceptionArisen;
//        try {
//            decoder.decodeMessage(givenMessage);
//            exceptionArisen = false;
//        } catch (final AnswerableException exception) {
//            assertSame(GIVEN_NOT_VALID_SUB_MESSAGE_RESPONSE, exception.getAnswer());
//            assertNotNull(exception.getCause());
//            exceptionArisen = true;
//        }
//        assertTrue(exceptionArisen);
//    }
//
//    private static WialonData createData(final LocalDate date) {
//        return WialonData.builder()
//                .date(date)
//                .build();
//    }
//
//    private static final class TestWialonRequestDataPackage extends AbstractWialonRequestDataPackage {
//
//        public TestWialonRequestDataPackage(final List<WialonData> data) {
//            super(data);
//        }
//
//        @Override
//        public String findPrefix() {
//            throw new UnsupportedOperationException();
//        }
//    }
//
//    private static final class TestWialonRequestDataPackageDecoder
//            extends AbstractWialonRequestDataPackageDecoder<TestWialonRequestDataPackage, WialonPackage> {
//        private final WialonPackage notValidSubMessageResponse;
//
//        public TestWialonRequestDataPackageDecoder(final String packagePrefix,
//                                                   final WialonMessageParser messageParser,
//                                                   final WialonPackage notValidSubMessageResponse) {
//            super(packagePrefix, messageParser);
//            this.notValidSubMessageResponse = notValidSubMessageResponse;
//        }
//
//        @Override
//        protected TestWialonRequestDataPackage createPackage(final List<WialonData> data) {
//            return new TestWialonRequestDataPackage(data);
//        }
//
//        @Override
//        protected WialonPackage createNotValidSubMessageResponse() {
//            return notValidSubMessageResponse;
//        }
//    }
//}
