package by.bsu.wialontransport.protocol.jt808.decoder;

import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class JT808SingleLocationPackageDecoder extends JT808LocationPackageDecoder {
    static final int LOCATION_COUNT = 1;
    private static final byte[] REQUIRED_PREFIX = {126, 2, 0};

    public JT808SingleLocationPackageDecoder() {
        super(REQUIRED_PREFIX);
    }

    @Override
    protected int decodeLocationCount(final ByteBuf buffer) {
        return LOCATION_COUNT;
    }

    @Override
    protected void skipUntilFirstLocation(final ByteBuf buffer) {

    }

    @Override
    protected void skipLocationPrefix(final ByteBuf buffer) {

    }
}