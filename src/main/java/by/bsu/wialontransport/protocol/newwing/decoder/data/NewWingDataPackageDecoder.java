package by.bsu.wialontransport.protocol.newwing.decoder.data;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.protocol.core.decoder.packages.PrefixedByStringBinaryPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.model.NewWingLocation;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingLocationPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.collections4.IteratorUtils.toList;

@Component
public final class NewWingDataPackageDecoder extends PrefixedByStringBinaryPackageDecoder {
    private static final String PREFIX = "GPRSSI";

    private final NewWingDataIteratorFactory dataIteratorFactory;

    public NewWingDataPackageDecoder(final NewWingDataIteratorFactory dataIteratorFactory) {
        super(PREFIX);
        this.dataIteratorFactory = dataIteratorFactory;
    }

    @Override
    protected Object decodeInternal(final ByteBuf buf) {
        final List<NewWingLocation> data = decodeData(buf);
        return new NewWingLocationPackage(data);
    }

    private List<NewWingLocation> decodeData(final ByteBuf buffer) {
        final NewWingDataIterator iterator = dataIteratorFactory.create(buffer);
        return toList(iterator);
    }
}
