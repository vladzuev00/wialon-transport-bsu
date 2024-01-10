package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public final class NewWingDataIterator implements Iterator<NewWingData> {
    private final NewWingDataDecoder decoder;
    private final ByteBuf buffer;

    @Override
    public boolean hasNext() {
        return buffer.isReadable();
    }

    @Override
    public NewWingData next() {
        checkNext();
        return decoder.decodeNext(buffer);
    }

    private void checkNext() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
    }
}