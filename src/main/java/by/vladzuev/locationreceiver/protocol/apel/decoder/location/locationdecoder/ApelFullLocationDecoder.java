package by.vladzuev.locationreceiver.protocol.apel.decoder.location.locationdecoder;

import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.util.stream.IntStream.range;

@Component
public final class ApelFullLocationDecoder extends ApelLocationDecoder {
    private static final int ANALOG_INPUT_COUNT = 8;

    @Override
    protected short readSpeed(final ByteBuf buffer) {
        return buffer.readByte();
    }

    @Override
    protected Double readHdop(final ByteBuf buffer) {
        return (double) buffer.readByte();
    }

    @Override
    protected Byte readSatelliteCount(final ByteBuf buffer) {
        return buffer.readByte();
    }

    @Override
    protected double[] readAnalogInputs(final ByteBuf buffer) {
        skipGSM(buffer);
        skipEventType(buffer);
        skipMetersTraveled(buffer);
        skipDI(buffer);
        skipDO(buffer);
        return range(0, ANALOG_INPUT_COUNT)
                .mapToDouble(i -> readAnalogInput(buffer))
                .toArray();
    }

    private void skipGSM(final ByteBuf buffer) {
        buffer.skipBytes(Byte.BYTES);
    }

    private void skipEventType(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }

    private void skipMetersTraveled(final ByteBuf buffer) {
        buffer.skipBytes(Integer.BYTES);
    }

    private void skipDI(final ByteBuf buffer) {
        buffer.skipBytes(Byte.BYTES);
    }

    private void skipDO(final ByteBuf buffer) {
        buffer.skipBytes(Byte.BYTES);
    }

    private short readAnalogInput(final ByteBuf buffer) {
        return buffer.readShortLE();
    }
}
