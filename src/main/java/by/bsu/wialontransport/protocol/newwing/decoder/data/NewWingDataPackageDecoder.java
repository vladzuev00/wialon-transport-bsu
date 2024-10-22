package by.bsu.wialontransport.protocol.newwing.decoder.data;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.protocol.newwing.decoder.NewWingPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingDataPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.collections4.IteratorUtils.toList;

@Component
public final class NewWingDataPackageDecoder extends NewWingPackageDecoder {
    private static final String PREFIX = "GPRSSI";

    private final NewWingDataIteratorFactory dataIteratorFactory;

    public NewWingDataPackageDecoder(final NewWingDataIteratorFactory dataIteratorFactory) {
        super(PREFIX);
        this.dataIteratorFactory = dataIteratorFactory;
    }

    @Override
    protected PackageFactory decodeUntilChecksum(final ByteBuf buffer) {
        final List<Location> data = decodeData(buffer);
        return checksum -> new NewWingDataPackage(checksum, data);
    }

    private List<Location> decodeData(final ByteBuf buffer) {
        final NewWingDataIterator iterator = dataIteratorFactory.create(buffer);
        return toList(iterator);
    }
}
