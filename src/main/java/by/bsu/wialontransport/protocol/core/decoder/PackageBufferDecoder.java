package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.protocolpackage.Package;
import io.netty.buffer.ByteBuf;

public abstract class PackageBufferDecoder<P extends Package> extends PackageDecoder<ByteBuf, P> {

}
