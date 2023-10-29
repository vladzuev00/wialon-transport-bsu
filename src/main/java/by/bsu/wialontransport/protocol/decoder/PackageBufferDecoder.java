package by.bsu.wialontransport.protocol.decoder;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import io.netty.buffer.ByteBuf;

public abstract class PackageBufferDecoder<P extends Package> extends PackageDecoder<ByteBuf, P> {

}
