package by.bsu.wialontransport.protocol.wialon.decoder.data;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer.parser.DataParser;
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

@RunWith(MockitoJUnitRunner.class)
public final class AbstractWialonRequestDataPackageDecoderTest {
    private static final String GIVEN_PACKAGE_PREFIX = "#TEST#";
    private static final WialonPackage GIVEN_RESPONSE_NOT_VALID_DATA_PACKAGE = new WialonPackage() {
    };

    @Mock
    private DataParser mockedDataParser;

    private AbstractWialonRequestDataPackageDecoder<?, ?> decoder;

    @Before
    public void initializeDecoder() {
        this.decoder = new TestWialonRequestDataPackageDecoder(
                GIVEN_PACKAGE_PREFIX,
                this.mockedDataParser,
                GIVEN_RESPONSE_NOT_VALID_DATA_PACKAGE
        );
    }

    @Test
    public void messageShouldBeDecoded() {
        throw new RuntimeException();
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
                                                   final DataParser dataParser,
                                                   final WialonPackage responseNotValidDataPackage) {
            super(packagePrefix, dataParser);
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
