package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.packages.Package;
import io.netty.buffer.ByteBuf;

public abstract class PackageBufferDecoder<PREFIX, PACKAGE extends Package>
        extends PackageDecoder<PREFIX, ByteBuf, PACKAGE> {

    public PackageBufferDecoder(final PREFIX packagePrefix) {
        super(packagePrefix);
    }

}
