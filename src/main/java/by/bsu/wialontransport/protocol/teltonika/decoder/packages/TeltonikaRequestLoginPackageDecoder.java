package by.bsu.wialontransport.protocol.teltonika.decoder.packages;

import by.bsu.wialontransport.protocol.core.decoder.packages.PrefixiedPackageDecoder;
import by.bsu.wialontransport.protocol.teltonika.model.packages.login.TeltonikaRequestLoginPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.US_ASCII;

@Component
public final class TeltonikaRequestLoginPackageDecoder extends PrefixiedPackageDecoder<Short> {

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
        return buffer.readCharSequence(length, US_ASCII).toString();
    }
}
