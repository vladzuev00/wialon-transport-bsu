package by.bsu.wialontransport.protocol.jt808;

import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class JT808SingleLocationMessageDecoder extends JT808LocationMessageDecoder {
    static final int LOCATION_COUNT = 1;
    private static final short MESSAGE_ID = 512;

    public JT808SingleLocationMessageDecoder() {
        super(MESSAGE_ID);
    }

    @Override
    protected int decodeLocationCount(ByteBuf buffer) {
        return LOCATION_COUNT;
    }

    @Override
    protected void skipUntilFirstLocation(ByteBuf buffer) {

    }

    @Override
    protected void skipLocationPrefix(ByteBuf buffer) {

    }
}
