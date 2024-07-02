package by.bsu.wialontransport.protocol.core.packagepipeline;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.core.encoder.packages.PackageEncoder;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class PackagePipeline<REQUEST extends Package, RESPONSE extends Package> {
    private final PackageDecoder decoder;
    private final PackageHandler<REQUEST, RESPONSE> handler;
    private final PackageEncoder<RESPONSE> encoder;
}
