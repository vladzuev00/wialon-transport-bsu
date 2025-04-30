package by.vladzuev.locationreceiver.protocol.wialon.decoder.location;

import by.vladzuev.locationreceiver.protocol.wialon.decoder.location.parser.WialonLocationParser;
import by.vladzuev.locationreceiver.protocol.wialon.model.location.request.WialonLocation;
import by.vladzuev.locationreceiver.protocol.wialon.model.location.request.WialonRequestSingleLocationPackage;
import org.springframework.stereotype.Component;

import static by.vladzuev.locationreceiver.protocol.wialon.model.location.request.WialonRequestSingleLocationPackage.PREFIX;

@Component
public final class WialonRequestSingleLocationPackageDecoder extends WialonRequestLocationPackageDecoder {

    public WialonRequestSingleLocationPackageDecoder(final WialonLocationParser locationParser) {
        super(PREFIX, locationParser);
    }

    @Override
    protected WialonRequestSingleLocationPackage decodeMessageInternal(final String message,
                                                                       final WialonLocationParser locationParser) {
        final WialonLocation location = locationParser.parse(message);
        return new WialonRequestSingleLocationPackage(location);
    }
}
