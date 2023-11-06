package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import io.netty.buffer.ByteBuf;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.String.format;

public final class NewWingDataIterator implements Iterator<NewWingData> {
    private final NewWingDataDecoder dataDecoder;
    private final ByteBuf buffer;
    private final int dataCount;
    private int readDataCount;

    public NewWingDataIterator(final NewWingDataDecoder dataDecoder, final ByteBuf buffer, final int dataCount) {
        this.dataDecoder = dataDecoder;
        this.buffer = buffer;
        this.dataCount = dataCount;
        this.readDataCount = 0;
    }

    @Override
    public boolean hasNext() {
        return this.readDataCount < this.dataCount;
    }

    @Override
    public NewWingData next() {
        this.checkNext();
        final NewWingData data = this.dataDecoder.decodeNext(this.buffer);
        this.readDataCount++;
        return data;
    }

    private void checkNext() {
        if (!this.hasNext()) {
            throw new NoSuchElementException(
                    format("%d data have been already read from given buffer", this.dataCount)
            );
        }
    }
}