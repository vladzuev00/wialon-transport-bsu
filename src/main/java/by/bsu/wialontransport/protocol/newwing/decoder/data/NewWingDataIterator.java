package by.bsu.wialontransport.protocol.newwing.decoder.data;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public final class NewWingDataIterator implements Iterator<NewWingData> {
    private static final int BYTE_COUNT_TO_HAVE_NEXT_EVENT = 37;

    private final NewWingDataDecoder decoder;
    private final ByteBuf buffer;

    @Override
    public boolean hasNext() {
        return buffer.readableBytes() >= BYTE_COUNT_TO_HAVE_NEXT_EVENT;
    }

    @Override
    public NewWingData next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return decoder.decodeNext(buffer);
    }
}