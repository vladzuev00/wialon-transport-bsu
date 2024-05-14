package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingEventCountPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class NewWingEventCountPackageDecoder extends NewWingPackageDecoder {
    private static final String PREFIX = "GPRSGI";

    public NewWingEventCountPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected PackageFactory decodeUntilChecksum(final ByteBuf buffer) {
        final short eventCount = buffer.readShortLE();
        final short frameEventCount = buffer.readShortLE();
        return checksum -> new NewWingEventCountPackage(checksum, eventCount, frameEventCount);
    }
}