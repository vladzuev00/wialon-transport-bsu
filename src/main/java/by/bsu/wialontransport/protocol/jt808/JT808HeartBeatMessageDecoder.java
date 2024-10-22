package by.bsu.wialontransport.protocol.jt808;

import by.zuevvlad.jt808.model.JT808HeartBeatMessage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class JT808HeartBeatMessageDecoder extends JT808MessageDecoder<JT808HeartBeatMessage> {
    private static final short MESSAGE_ID = 2;

    public JT808HeartBeatMessageDecoder() {
        super(MESSAGE_ID);
    }

    @Override
    protected JT808HeartBeatMessage decodeInternal(ByteBuf buffer, String phoneNumber) {
        return new JT808HeartBeatMessage();
    }
}
