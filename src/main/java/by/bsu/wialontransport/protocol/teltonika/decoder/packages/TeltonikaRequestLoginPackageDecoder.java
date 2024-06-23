package by.bsu.wialontransport.protocol.teltonika.decoder.packages;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoderByPrefix;
import by.bsu.wialontransport.protocol.teltonika.model.packages.login.TeltonikaRequestLoginPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.US_ASCII;

@Component
public final class TeltonikaRequestLoginPackageDecoder extends PackageDecoderByPrefix<Short> {
    private static final int IMEI_START_BYTE_INDEX = 2;

    @Override
    public TeltonikaRequestLoginPackage decode(final ByteBuf buffer) {
        final String imei = readImei(buffer);
        return new TeltonikaRequestLoginPackage(imei);
    }

    @Override
    protected Short getPrefix(final ByteBuf buffer) {
        return buffer.getShort(0);
    }

    @Override
    protected boolean isAbleToDecode(final Short prefix) {
        return prefix.compareTo((short) 0) > 0;
    }

    private String readImei(final ByteBuf buffer) {
        final short length = buffer.readShort();
        return buffer.toString(IMEI_START_BYTE_INDEX, length, US_ASCII);
    }
}
