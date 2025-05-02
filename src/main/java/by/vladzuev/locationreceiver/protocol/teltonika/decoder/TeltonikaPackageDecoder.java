package by.vladzuev.locationreceiver.protocol.teltonika.decoder;

import by.vladzuev.locationreceiver.protocol.core.decoder.packages.PackageDecoder;
import by.vladzuev.locationreceiver.protocol.teltonika.holder.LoginSuccessHolder;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TeltonikaPackageDecoder implements PackageDecoder<ByteBuf> {
    private final LoginSuccessHolder loginSuccessHolder;
    private final boolean requiredLoginSuccess;

    @Override
    public final boolean isAbleDecode(final ByteBuf buffer) {
        return loginSuccessHolder.isSuccess() == requiredLoginSuccess;
    }

    @Override
    public final Object decode(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
        return decodeInternal(buffer);
    }

    protected abstract Object decodeInternal(final ByteBuf buffer);
}
