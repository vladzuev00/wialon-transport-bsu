package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import by.bsu.wialontransport.protocol.newwing.decoder.packages.NewWingPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingDataPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.builder.DataNewWingPackageBuilder;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.bsu.wialontransport.util.CollectionUtil.convertToList;

@Component
public final class DataNewWingPackageDecoder
        extends NewWingPackageDecoder<NewWingDataPackage, DataNewWingPackageBuilder> {
    private static final String PACKAGE_PREFIX = "GPRSSI";

    private final NewWingDataIteratorFactory dataIteratorFactory;

    public DataNewWingPackageDecoder(final NewWingDataIteratorFactory dataIteratorFactory) {
        super(PACKAGE_PREFIX, DataNewWingPackageBuilder::new);
        this.dataIteratorFactory = dataIteratorFactory;
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
        final NewWingDataIterator iterator = this.dataIteratorFactory.create(buffer);
        return convertToList(iterator);
    }
}