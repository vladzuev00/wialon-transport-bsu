package by.bsu.wialontransport.protocol.newwing.decoder.location;

import by.bsu.wialontransport.protocol.newwing.model.NewWingLocation;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public final class NewWingLocationIterator implements Iterator<NewWingLocation> {
    static final int LOCATION_BYTE_COUNT = 37;

    private final NewWingLocationDecoder decoder;
    private final ByteBuf buffer;

    @Override
    public boolean hasNext() {
        return buffer.readableBytes() >= LOCATION_BYTE_COUNT;
    }

    @Override
    public NewWingLocation next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return decoder.decode(buffer);
    }
}
