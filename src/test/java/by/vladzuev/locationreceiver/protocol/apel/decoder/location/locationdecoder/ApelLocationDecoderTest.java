package by.vladzuev.locationreceiver.protocol.apel.decoder.location.locationdecoder;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static java.util.stream.IntStream.range;

public final class ApelLocationDecoderTest {
    private final ApelLocationDecoder decoder = new TestApelLocationDecoder();

    @Test
    public void locationShouldBeDecoded() {
        throw new RuntimeException();
    }

    private static final class TestApelLocationDecoder extends ApelLocationDecoder {
        private static final int ANALOG_INPUT_COUNT = 3;

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

        private short readAnalogInput(final ByteBuf buffer) {
            return buffer.readShortLE();
        }
    }
}
