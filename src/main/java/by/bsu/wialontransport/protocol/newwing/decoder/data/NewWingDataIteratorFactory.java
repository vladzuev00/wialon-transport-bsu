package by.bsu.wialontransport.protocol.newwing.decoder.data;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class NewWingDataIteratorFactory {
    private final NewWingDataDecoder decoder;

    public NewWingDataIterator create(final ByteBuf buffer) {
        return new NewWingDataIterator(decoder, buffer);
    }
}
