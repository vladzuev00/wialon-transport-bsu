package by.bsu.wialontransport.protocol.wialon.decoder.packages.data;

import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.WialonMessageParser;
import by.bsu.wialontransport.protocol.wialon.model.WialonData;
import by.bsu.wialontransport.protocol.wialon.model.packages.data.request.WialonRequestBlackBoxPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseBlackBoxPackage;

import java.util.List;

import static by.bsu.wialontransport.protocol.wialon.model.packages.data.request.WialonRequestBlackBoxPackage.PREFIX;

public final class WialonRequestBlackBoxPackageDecoder extends AbstractWialonRequestDataPackageDecoder {

    public WialonRequestBlackBoxPackageDecoder(final WialonMessageParser messageParser) {
        super(PREFIX, messageParser);
    }

    @Override
    protected WialonRequestBlackBoxPackage createPackage(final List<WialonData> data) {
        return new WialonRequestBlackBoxPackage(data);
    }

    @Override
    protected WialonResponseBlackBoxPackage createNotValidSubMessageResponse() {
        return new WialonResponseBlackBoxPackage(0);
    }
}
