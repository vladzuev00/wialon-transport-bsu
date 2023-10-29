package by.bsu.wialontransport.protocol.decoder;

import by.bsu.wialontransport.protocol.Package;

public abstract class PackageDecoder<S, P extends Package> {

    public abstract boolean isAbleToDecode(final S source);

    public abstract P decode(final S source);
}
