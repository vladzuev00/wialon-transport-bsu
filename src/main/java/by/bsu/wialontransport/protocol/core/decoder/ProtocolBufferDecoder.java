package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageBufferDecoder;
import io.netty.buffer.ByteBuf;

import java.util.List;

public abstract class ProtocolBufferDecoder<PREFIX> extends ProtocolDecoder<PREFIX, ByteBuf, PackageBufferDecoder<PREFIX, ?>> {

    public ProtocolBufferDecoder(final List<PackageBufferDecoder<PREFIX, ?>> packageDecoders) {
        super(packageDecoders);
    }

    @Override
    protected final ByteBuf createSource(final ByteBuf buffer) {
        return buffer;
    }
}
