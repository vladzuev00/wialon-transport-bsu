package by.bsu.wialontransport.protocol.decoder.wialon.data;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.DataParser;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestBlackBoxPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseBlackBoxPackage;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public final class WialonRequestBlackBoxPackageDecoder
        extends AbstractWialonRequestDataPackageDecoder<WialonRequestBlackBoxPackage, WialonResponseBlackBoxPackage> {
    private static final String REGEX_MESSAGES_DELIMITER = "\\|";

    public WialonRequestBlackBoxPackageDecoder(final String packagePrefix, final DataParser dataParser) {
        super(packagePrefix, dataParser);
    }

    @Override
    protected Stream<String> splitIntoSubMessages(final String message) {
        final String[] subMessages = message.split(REGEX_MESSAGES_DELIMITER);
        return stream(subMessages);
    }

    @Override
    protected WialonRequestBlackBoxPackage createPackage(final List<Data> data) {
        return new WialonRequestBlackBoxPackage(data);
    }

    @Override
    protected WialonResponseBlackBoxPackage createResponseNotValidDataPackage() {
        return new WialonResponseBlackBoxPackage(0);
    }
}
