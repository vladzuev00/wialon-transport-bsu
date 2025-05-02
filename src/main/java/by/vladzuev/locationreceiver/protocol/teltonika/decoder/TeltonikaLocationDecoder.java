package by.vladzuev.locationreceiver.protocol.teltonika.decoder;

import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaLocation;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class TeltonikaLocationDecoder {

    public TeltonikaLocation decode(final ByteBuf buffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
