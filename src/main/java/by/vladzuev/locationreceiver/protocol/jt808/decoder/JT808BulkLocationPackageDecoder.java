package by.vladzuev.locationreceiver.protocol.jt808.decoder;

import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class JT808BulkLocationPackageDecoder extends JT808LocationPackageDecoder {
    private static final byte[] PREFIX = {126, 7, 4};

    public JT808BulkLocationPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected int decodeLocationCount(final ByteBuf buffer) {
        return buffer.readShort();
    }

    @Override
    protected void skipUntilFirstLocation(final ByteBuf buffer) {
        buffer.skipBytes(Byte.BYTES);
    }

    @Override
    protected void skipLocationPrefix(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }
}
