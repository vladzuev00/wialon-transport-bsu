package by.bsu.wialontransport.protocol.jt808;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;

import static by.zuevvlad.jt808.util.JT808Util.decodePhoneNumber;

@RequiredArgsConstructor
public abstract class JT808MessageDecoder<M> {
    private final short messageId;

    public final boolean isAbleDecode(short messageId) {
        return this.messageId == messageId;
    }

    public final M decode(ByteBuf buffer) {
        skipBodyProperties(buffer);
        String phoneNumber = decodePhoneNumber(buffer);
        skipSerialNumber(buffer);
        return decodeInternal(buffer, phoneNumber);
    }

    protected abstract M decodeInternal(ByteBuf buffer, String phoneNumber);

    private void skipBodyProperties(ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }

    private void skipSerialNumber(ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }
}
