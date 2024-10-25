package by.bsu.wialontransport.protocol.newwing.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PrefixedByStringBinaryPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingEventCountPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class NewWingEventCountPackageDecoder extends PrefixedByStringBinaryPackageDecoder {
    private static final String REQUIRED_PREFIX = "GPRSGI";

    public NewWingEventCountPackageDecoder() {
        super(REQUIRED_PREFIX);
    }

    @Override
    protected NewWingEventCountPackage decodeInternal(final ByteBuf buffer) {
        return new NewWingEventCountPackage();
    }
}
