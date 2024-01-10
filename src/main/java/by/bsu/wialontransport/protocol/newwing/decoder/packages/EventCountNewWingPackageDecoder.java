package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.function.ShortConsumer;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingEventCountPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.builder.EventCountNewWingPackageBuilder;
import io.netty.buffer.ByteBuf;

public final class EventCountNewWingPackageDecoder
        extends NewWingPackageDecoder<NewWingEventCountPackage, EventCountNewWingPackageBuilder> {
    private static final String PACKAGE_PREFIX = "GPRSGI";

    public EventCountNewWingPackageDecoder() {
        super(PACKAGE_PREFIX);
    }

    @Override
    protected EventCountNewWingPackageBuilder createPackageBuilder() {
        return new EventCountNewWingPackageBuilder();
    }

    @Override
    protected void decodeUntilChecksum(final ByteBuf buffer, final EventCountNewWingPackageBuilder packageBuilder) {
        decodeEventCount(buffer, packageBuilder);
        decodeFrameEventCount(buffer, packageBuilder);
    }

    private static void decodeEventCount(final ByteBuf buffer,
                                         final EventCountNewWingPackageBuilder packageBuilder) {
        readAndAccumulateShortValue(buffer, packageBuilder::setEventCount);
    }

    private static void decodeFrameEventCount(final ByteBuf buffer,
                                              final EventCountNewWingPackageBuilder packageBuilder) {
        readAndAccumulateShortValue(buffer, packageBuilder::setFrameEventCount);
    }

    private static void readAndAccumulateShortValue(final ByteBuf buffer, final ShortConsumer accumulator) {
        final short value = buffer.readShortLE();
        accumulator.accept(value);
    }
}