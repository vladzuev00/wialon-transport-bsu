package by.bsu.wialontransport.protocol.newwing.decoder.data;

import by.bsu.wialontransport.protocol.core.decoder.packages.PrefixedByStringBinaryPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.model.NewWingLocation;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingLocationPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.collections4.IteratorUtils.toList;

@Component
public final class NewWingLocationPackageDecoder extends PrefixedByStringBinaryPackageDecoder {
    private static final String REQUIRED_PREFIX = "GPRSSI";

    private final NewWingLocationIteratorFactory locationIteratorFactory;

    public NewWingLocationPackageDecoder(final NewWingLocationIteratorFactory locationIteratorFactory) {
        super(REQUIRED_PREFIX);
        this.locationIteratorFactory = locationIteratorFactory;
    }

    @Override
    protected NewWingLocationPackage decodeInternal(final ByteBuf buffer) {
        final NewWingLocationIterator locationIterator = locationIteratorFactory.create(buffer);
        final List<NewWingLocation> locations = toList(locationIterator);
        return new NewWingLocationPackage(locations);
    }
}
