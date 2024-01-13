package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.function.ShortConsumer;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingEventCountPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.builder.NewWingEventCountPackageBuilder;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class NewWingEventCountPackageDecoder
        extends NewWingPackageDecoder<NewWingEventCountPackage, NewWingEventCountPackageBuilder> {
    private static final String PACKAGE_PREFIX = "GPRSGI";

    public NewWingEventCountPackageDecoder() {
        super(PACKAGE_PREFIX);
    }

    @Override
    protected NewWingEventCountPackageBuilder createPackageBuilder() {
        return new NewWingEventCountPackageBuilder();
    }

    @Override
    protected void decodeUntilChecksum(final ByteBuf buffer, final NewWingEventCountPackageBuilder packageBuilder) {
        decodeEventCount(buffer, packageBuilder);
        decodeFrameEventCount(buffer, packageBuilder);
    }

    private static void decodeEventCount(final ByteBuf buffer,
                                         final NewWingEventCountPackageBuilder packageBuilder) {
        readAndAccumulateShortValue(buffer, packageBuilder::setEventCount);
    }

    private static void decodeFrameEventCount(final ByteBuf buffer,
                                              final NewWingEventCountPackageBuilder packageBuilder) {
        readAndAccumulateShortValue(buffer, packageBuilder::setFrameEventCount);
    }

    private static void readAndAccumulateShortValue(final ByteBuf buffer, final ShortConsumer accumulator) {
        final short value = buffer.readShortLE();
        accumulator.accept(value);
    }
}