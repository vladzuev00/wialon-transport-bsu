package by.bsu.wialontransport.protocol.newwing.model;

import org.springframework.stereotype.Component;

import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.MIN_VALUE;
import static java.util.OptionalInt.empty;

@Component
public final class CurrentFrameEventCount {
    private static final int NOT_DEFINED_VALUE = MIN_VALUE;

    private final AtomicInteger value = new AtomicInteger(NOT_DEFINED_VALUE);

    public void setValue(final int value) {
        this.value.set(value);
    }

    public OptionalInt takeValue() {
        final int value = this.value.getAndSet(NOT_DEFINED_VALUE);
        return value != NOT_DEFINED_VALUE ? OptionalInt.of(value) : empty();
    }
}