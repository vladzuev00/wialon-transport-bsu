package by.bsu.wialontransport.protocol.decoder;

import by.bsu.wialontransport.protocol.Package;
import io.netty.buffer.ByteBuf;

import java.util.List;

public abstract class ProtocolBufferDecoder<P extends Package, D extends PackageBufferDecoder<P>>
        extends ProtocolDecoder<ByteBuf, P, D> {

    public ProtocolBufferDecoder(final List<D> packageDecoders) {
        super(packageDecoders);
    }

    @Override
    public final ByteBuf createSource(final ByteBuf byteBuf) {
        return byteBuf;
    }
}
