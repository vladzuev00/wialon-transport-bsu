package by.bsu.wialontransport.protocol.wialon.decoder.location;

import by.bsu.wialontransport.protocol.wialon.decoder.location.parser.WialonLocationParser;
import by.bsu.wialontransport.protocol.wialon.model.packages.location.request.WialonRequestBulkLocationPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.model.packages.location.request.WialonRequestBulkLocationPackage.PREFIX;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Component
public final class WialonRequestBulkLocationPackageDecoder extends WialonRequestLocationPackageDecoder {
    private static final String LOCATION_DELIMITER_REGEX = "\\|";

    public WialonRequestBulkLocationPackageDecoder(final WialonLocationParser locationParser) {
        super(PREFIX, locationParser);
    }

    @Override
    protected WialonRequestBulkLocationPackage decodeMessageInternal(final String message,
                                                                     final WialonLocationParser locationParser) {
        return stream(message.split(LOCATION_DELIMITER_REGEX))
                .map(locationParser::parse)
                .collect(collectingAndThen(toList(), WialonRequestBulkLocationPackage::new));
    }
}
