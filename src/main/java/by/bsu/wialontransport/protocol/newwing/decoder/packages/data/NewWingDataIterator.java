package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import io.netty.buffer.ByteBuf;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class NewWingDataIterator implements Iterator<NewWingData> {
    private final NewWingDataDecoder dataDecoder;
    private final ByteBuf buffer;

    public NewWingDataIterator(final NewWingDataDecoder dataDecoder, final ByteBuf buffer) {
        this.dataDecoder = dataDecoder;
        this.buffer = buffer;
    }

    @Override
    public boolean hasNext() {
        return this.buffer.isReadable();
    }

    @Override
    public NewWingData next() {
        this.checkNext();
        return this.dataDecoder.decodeNext(this.buffer);
    }

    private void checkNext() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
    }
}