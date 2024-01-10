package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import by.bsu.wialontransport.protocol.newwing.decoder.packages.NewWingPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingDataPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.builder.NewWingDataPackageBuilder;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.bsu.wialontransport.util.CollectionUtil.convertToList;

@Component
public final class NewWingDataPackageDecoder
        extends NewWingPackageDecoder<NewWingDataPackage, NewWingDataPackageBuilder> {
    private static final String PACKAGE_PREFIX = "GPRSSI";

    private final NewWingDataIteratorFactory dataIteratorFactory;

    public NewWingDataPackageDecoder(final NewWingDataIteratorFactory dataIteratorFactory) {
        super(PACKAGE_PREFIX);
        this.dataIteratorFactory = dataIteratorFactory;
    }

    @Override
    protected NewWingDataPackageBuilder createPackageBuilder() {
        return new NewWingDataPackageBuilder();
    }

    @Override
    protected void decodeUntilChecksum(final ByteBuf buffer, final NewWingDataPackageBuilder packageBuilder) {
        this.decodeData(buffer, packageBuilder);
    }

    private void decodeData(final ByteBuf buffer, final NewWingDataPackageBuilder packageBuilder) {
        final List<NewWingData> data = this.readData(buffer);
        packageBuilder.setData(data);
    }

    private List<NewWingData> readData(final ByteBuf buffer) {
        final NewWingDataIterator iterator = this.dataIteratorFactory.create(buffer);
        return convertToList(iterator);
    }
}