package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import by.bsu.wialontransport.protocol.newwing.model.CurrentFrameEventCount;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.OptionalInt;

@Component
@RequiredArgsConstructor
public final class NewWingDataIteratorFactory {
    private final CurrentFrameEventCount currentFrameEventCount;
    private final NewWingDataDecoder dataDecoder;

    public NewWingDataIterator create(final ByteBuf buffer) {
        final int currentFrameEventCount = this.findCurrentFrameEventCount();
        return new NewWingDataIterator(this.dataDecoder, buffer, currentFrameEventCount);
    }

    private int findCurrentFrameEventCount() {
        final OptionalInt optionalValue = this.currentFrameEventCount.takeValue();
        return optionalValue.orElseThrow(NoCurrentFrameEventCount::new);
    }

    private static final class NoCurrentFrameEventCount extends RuntimeException {

        public NoCurrentFrameEventCount() {

        }

        @SuppressWarnings("unused")
        public NoCurrentFrameEventCount(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public NoCurrentFrameEventCount(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public NoCurrentFrameEventCount(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
