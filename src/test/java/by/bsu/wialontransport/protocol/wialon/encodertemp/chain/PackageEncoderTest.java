package by.bsu.wialontransport.protocol.wialon.encodertemp.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class PackageEncoderTest {

    @Mock
    private PackageEncoder mockedNextEncoder;

    private PackageEncoder packageEncoder;

    @Before
    public void initializePackageEncoder() {
        this.packageEncoder = new TestPackageEncoder(this.mockedNextEncoder);
    }

    @Test
    public void packageShouldBeEncodedByEncoder() {
        final WialonPackage givenPackage = new TestPackage();

        final String actual = this.packageEncoder.encode(givenPackage);
        final String expected = "#TEST_PACKAGE#\r\n";
        assertEquals(expected, actual);

        verify(this.mockedNextEncoder, times(0))
                .encodeIndependentlyWithoutPostfix(any(WialonPackage.class));
    }

    //TODO: add test the same as PackageDecoderTests
    @Test
    public void packageShouldBeEncodedByNextEncoderBecauseOfNotSuitableType() {
        throw new RuntimeException();
    }

    private static final class TestPackageEncoder extends PackageEncoder {
        private static final String ENCODED_PACKAGE_WITHOUT_PREFIX = "#TEST_PACKAGE#";

        public TestPackageEncoder(final PackageEncoder nextEncoder) {
            super(TestPackage.class, nextEncoder);
        }

        @Override
        protected String encodeIndependentlyWithoutPostfix(final WialonPackage encodedPackage) {
            return ENCODED_PACKAGE_WITHOUT_PREFIX;
        }
    }

    private static final class TestPackage implements WialonPackage {

    }
}
