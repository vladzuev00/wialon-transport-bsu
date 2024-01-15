package by.bsu.wialontransport.protocol.wialon.decoder.packages.data;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.exception.AnsweredException;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.WialonMessageParser;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception.NotValidMessageException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractWialonRequestDataPackage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class AbstractWialonRequestDataPackageDecoderTest {
    private static final String GIVEN_PACKAGE_PREFIX = "#TEST#";
    private static final WialonPackage GIVEN_RESPONSE_NOT_VALID_DATA_PACKAGE = new WialonPackage() {
    };

    @Mock
    private WialonMessageParser mockedWialonMessageParser;

    private AbstractWialonRequestDataPackageDecoder<TestWialonRequestDataPackage, WialonPackage> decoder;

    @Before
    public void initializeDecoder() {
        this.decoder = new TestWialonRequestDataPackageDecoder(
                GIVEN_PACKAGE_PREFIX,
                this.mockedWialonMessageParser,
                GIVEN_RESPONSE_NOT_VALID_DATA_PACKAGE
        );
    }

    @Test
    public void messageShouldBeDecoded() {
        final String givenMessage = "first|second|third";

        final Data firstGivenData = createData(1L);
        when(this.mockedWialonMessageParser.parse(eq("first"))).thenReturn(firstGivenData);

        final Data secondGivenData = createData(2L);
        when(this.mockedWialonMessageParser.parse(eq("second"))).thenReturn(secondGivenData);

        final Data thirdGivenData = createData(3L);
        when(this.mockedWialonMessageParser.parse(eq("third"))).thenReturn(thirdGivenData);

        final TestWialonRequestDataPackage actual = this.decoder.decodeMessage(givenMessage);
        final TestWialonRequestDataPackage expected = new TestWialonRequestDataPackage(
                List.of(
                        firstGivenData,
                        secondGivenData,
                        thirdGivenData
                )
        );
        assertEquals(expected, actual);
    }

    @Test
    public void messageShouldNotBeDecodedBecauseOfNotValidDataException() {
        final String givenMessage = "first|second|third";

        final Data firstGivenData = createData(1L);
        when(this.mockedWialonMessageParser.parse(eq("first"))).thenReturn(firstGivenData);

        final Data secondGivenData = createData(2L);
        when(this.mockedWialonMessageParser.parse(eq("second"))).thenReturn(secondGivenData);

        when(this.mockedWialonMessageParser.parse(eq("third"))).thenThrow(NotValidMessageException.class);

        boolean exceptionArisen;
        try {
            this.decoder.decodeMessage(givenMessage);
            exceptionArisen = false;
        } catch (final AnsweredException exception) {
            assertSame(GIVEN_RESPONSE_NOT_VALID_DATA_PACKAGE, exception.getAnswer());
            assertNotNull(exception.getCause());
            exceptionArisen = true;
        }
        assertTrue(exceptionArisen);
    }

    private static Data createData(final Long id) {
        return Data.builder()
                .id(id)
                .build();
    }

    private static class TestWialonRequestDataPackage extends AbstractWialonRequestDataPackage {

        public TestWialonRequestDataPackage(final List<Data> data) {
            super(data);
        }
    }

    private static final class TestWialonRequestDataPackageDecoder
            extends AbstractWialonRequestDataPackageDecoder<TestWialonRequestDataPackage, WialonPackage> {
        private static final String REGEX_SUB_MESSAGES_DELIMITER = "\\|";

        private final WialonPackage responseNotValidDataPackage;

        public TestWialonRequestDataPackageDecoder(final String packagePrefix,
                                                   final WialonMessageParser wialonMessageParser,
                                                   final WialonPackage responseNotValidDataPackage) {
            super(packagePrefix, wialonMessageParser);
            this.responseNotValidDataPackage = responseNotValidDataPackage;
        }

        @Override
        protected Stream<String> splitIntoSubMessages(final String message) {
            final String[] subMessages = message.split(REGEX_SUB_MESSAGES_DELIMITER);
            return stream(subMessages);
        }

        @Override
        protected TestWialonRequestDataPackage createPackage(final List<Data> data) {
            return new TestWialonRequestDataPackage(data);
        }

        @Override
        protected WialonPackage createResponseNotValidDataPackage() {
            return this.responseNotValidDataPackage;
        }
    }
}
