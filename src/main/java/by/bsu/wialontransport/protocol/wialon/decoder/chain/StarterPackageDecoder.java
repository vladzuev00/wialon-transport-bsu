package by.bsu.wialontransport.protocol.wialon.decoder.chain;

import org.springframework.stereotype.Component;

@Component
public final class StarterPackageDecoder extends PackageDecoder {

    public StarterPackageDecoder(final RequestLoginPackageDecoder nextDecoder) {
        super(nextDecoder, null, null);
    }
}