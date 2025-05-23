package by.vladzuev.locationreceiver.protocol.newwing.decoder.location;

import by.vladzuev.locationreceiver.protocol.core.decoder.packages.PrefixedByStringBinaryPackageDecoder;
import by.vladzuev.locationreceiver.protocol.newwing.model.request.NewWingLocation;
import by.vladzuev.locationreceiver.protocol.newwing.model.request.NewWingLocationPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.collections4.IteratorUtils.toList;

@Component
public final class NewWingLocationPackageDecoder extends PrefixedByStringBinaryPackageDecoder {
    private static final String PREFIX = "GPRSSI";

    private final NewWingLocationIteratorFactory locationIteratorFactory;

    public NewWingLocationPackageDecoder(final NewWingLocationIteratorFactory locationIteratorFactory) {
        super(PREFIX);
        this.locationIteratorFactory = locationIteratorFactory;
    }

    @Override
    protected NewWingLocationPackage decodeInternal(final ByteBuf buffer) {
        final List<NewWingLocation> locations = toList(locationIteratorFactory.create(buffer));
        return new NewWingLocationPackage(locations);
    }
}
