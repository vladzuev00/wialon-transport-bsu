package by.bsu.wialontransport.protocol.newwing.decoder.location;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class NewWingLocationIteratorFactory {
    private final NewWingLocationDecoder decoder;

    public NewWingLocationIterator create(final ByteBuf buffer) {
        return new NewWingLocationIterator(decoder, buffer);
    }
}
