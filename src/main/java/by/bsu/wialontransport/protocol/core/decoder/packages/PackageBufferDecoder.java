package by.bsu.wialontransport.protocol.core.decoder.packages;

import by.bsu.wialontransport.protocol.protocolpackage.Package;
import io.netty.buffer.ByteBuf;

public abstract class PackageBufferDecoder<P extends Package> extends PackageDecoder<ByteBuf, P> {

}
