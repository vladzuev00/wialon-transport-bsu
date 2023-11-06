package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import by.bsu.wialontransport.protocol.newwing.decoder.packages.NewWingPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.model.CurrentFrameEventCount;
import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import by.bsu.wialontransport.protocol.newwing.model.packages.DataNewWingPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.builder.DataNewWingPackageBuilder;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.OptionalInt;

import static by.bsu.wialontransport.util.CollectionUtil.convertToList;

public final class DataNewWingPackageDecoder
        extends NewWingPackageDecoder<DataNewWingPackage, DataNewWingPackageBuilder> {
    private static final String PACKAGE_PREFIX = "GPRSSI";

    private final CurrentFrameEventCount currentFrameEventCount;
    private final NewWingDataDecoder dataDecoder;

    public DataNewWingPackageDecoder(final CurrentFrameEventCount currentFrameEventCount,
                                     final NewWingDataDecoder dataDecoder) {
        super(PACKAGE_PREFIX, DataNewWingPackageBuilder::new);
        this.currentFrameEventCount = currentFrameEventCount;
        this.dataDecoder = dataDecoder;
    }

    @Override
    protected void decodeUntilChecksum(final ByteBuf buffer, final DataNewWingPackageBuilder packageBuilder) {
        this.decodeData(buffer, packageBuilder);
    }

    private void decodeData(final ByteBuf buffer, final DataNewWingPackageBuilder packageBuilder) {
        final List<NewWingData> data = this.readData(buffer);
        packageBuilder.setData(data);
    }

    private List<NewWingData> readData(final ByteBuf buffer) {
        final NewWingDataIterator iterator = this.createDataIterator(buffer);
        return convertToList(iterator);
    }

    private NewWingDataIterator createDataIterator(final ByteBuf buffer) {
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
