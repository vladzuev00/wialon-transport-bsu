package by.bsu.wialontransport.protocol.wialon.decoder.packages.data;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.WialonMessageParser;
import by.bsu.wialontransport.protocol.wialon.model.WialonData;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestBlackBoxPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseBlackBoxPackage;

import java.util.List;
import java.util.stream.Stream;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestBlackBoxPackage.PREFIX;
import static java.util.Arrays.stream;

public final class WialonRequestBlackBoxPackageDecoder
        extends AbstractWialonRequestDataPackageDecoder<WialonRequestBlackBoxPackage, WialonResponseBlackBoxPackage> {
    private static final String REGEX_SUB_MESSAGES_DELIMITER = "\\|";

    public WialonRequestBlackBoxPackageDecoder(final WialonMessageParser wialonMessageParser) {
        super(PREFIX, wialonMessageParser);
    }

    @Override
    protected WialonRequestBlackBoxPackage createPackage(List<WialonData> data) {
        return null;
    }

    @Override
    protected WialonResponseBlackBoxPackage createNotValidSubMessageResponse() {
        return new WialonResponseBlackBoxPackage(0);
    }
}
