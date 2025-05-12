package by.vladzuev.locationreceiver.protocol.apel.decoder.location.locationdecoder;

import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class ApelShortLocationDecoder extends ApelLocationDecoder {

    @Override
    protected short readSpeed(final ByteBuf buffer) {
        return buffer.readShortLE();
    }

    @Override
    protected Byte readHdop(final ByteBuf buffer) {
        return null;
    }

    @Override
    protected Byte readSatelliteCount(final ByteBuf buffer) {
        return null;
    }

    @Override
    protected double[] readAnalogInputs(final ByteBuf buffer) {
        return new double[0];
    }
}
