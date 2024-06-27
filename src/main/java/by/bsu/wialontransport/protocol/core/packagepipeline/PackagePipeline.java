package by.bsu.wialontransport.protocol.core.packagepipeline;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.core.encoder.packages.PackageEncoder;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PackagePipeline<PACKAGE extends Package> {
    private final PackageDecoder<PACKAGE> decoder;
    private final PackageHandler<PACKAGE> handler;
    private final PackageEncoder<PACKAGE> encoder;
}
