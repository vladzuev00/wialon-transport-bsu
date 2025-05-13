package by.vladzuev.locationreceiver.protocol.apel.decoder.location;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import io.netty.buffer.ByteBuf;
import lombok.Value;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ApelLocationPackageDecoderTest {
    private final ApelLocationPackageDecoder decoder = new TestApelLocationPackageDecoder();

    @Test
    public void packageShouldBeDecodedStartingFromBody() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("1011121314151617181920212223242526272829303132333435"));

        final Object actual = decoder.decodeStartingFromBody(givenBuffer);
        final TestPackage expected = new TestPackage(
                new ApelLocation(
                        319951120,
                        387323156,
                        555751704,
                        (short) 8994,
                        (byte) 36,
                        (short) 9765,
                        (short) 10279,
                        (byte) 41,
                        new double[]{12592, 13106, 13620}
                )
        );
        assertEquals(expected, actual);
        assertEquals(0, givenBuffer.readableBytes());
    }

    @Value
    private static class TestPackage {
        ApelLocation location;
    }

    private static final class TestApelLocationPackageDecoder extends ApelLocationPackageDecoder {
        private static final int ANALOG_INPUT_COUNT = 3;

        public TestApelLocationPackageDecoder() {
            super(Integer.MAX_VALUE);
        }

        @Override
        protected short readSpeed(final ByteBuf buffer) {
            return buffer.readShortLE();
        }

        @Override
        protected Byte readHdop(final ByteBuf buffer) {
            return buffer.readByte();
        }

        @Override
        protected Byte readSatelliteCount(final ByteBuf buffer) {
            return buffer.readByte();
        }

        @Override
        protected double[] readAnalogInputs(final ByteBuf buffer) {
            return range(0, ANALOG_INPUT_COUNT)
                    .mapToDouble(i -> readAnalogInput(buffer))
                    .toArray();
        }

        @Override
        protected TestPackage createPackage(final ApelLocation location) {
            return new TestPackage(location);
        }

        private short readAnalogInput(final ByteBuf buffer) {
            return buffer.readShortLE();
        }
    }
}
