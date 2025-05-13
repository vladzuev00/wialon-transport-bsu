package by.vladzuev.locationreceiver.protocol.apel.decoder.location.locationdecoder;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ApelLocationDecoderTest {
    private final ApelLocationDecoder decoder = new TestApelLocationDecoder();

    @Test
    public void locationShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("1010101110141015101810191022101025102710103010311032"));

        final ApelLocation actual = decoder.decode(givenBuffer);
        final ApelLocation expected = new ApelLocation(
                286265360,
                353375248,
                420485136,
                (short) 8720,
                (byte) 16,
                (short) 9488,
                (short) 10000,
                (byte) 16,
                new double[]{12304, 12560, 12816}
        );
        assertEquals(expected, actual);
        assertEquals(0, givenBuffer.readableBytes());
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
