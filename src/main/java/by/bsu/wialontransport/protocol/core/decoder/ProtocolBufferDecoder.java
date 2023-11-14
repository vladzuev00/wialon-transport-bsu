package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import io.netty.buffer.ByteBuf;

import java.util.List;

public abstract class ProtocolBufferDecoder<PREFIX> extends ProtocolDecoder<PREFIX, ByteBuf> {

    public ProtocolBufferDecoder(final List<PackageDecoder<PREFIX, ByteBuf, ?>> packageDecoders) {
        super(packageDecoders);
    }

    @Override
    protected final ByteBuf createSource(final ByteBuf buffer) {
        return buffer;
    }
}
