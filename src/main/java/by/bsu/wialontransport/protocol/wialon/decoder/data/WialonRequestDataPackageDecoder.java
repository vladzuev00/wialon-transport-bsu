package by.bsu.wialontransport.protocol.wialon.decoder.data;


import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer.parser.DataParser;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestDataPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage;

import java.util.List;
import java.util.stream.Stream;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestDataPackage.PREFIX;
import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.Status.ERROR_PACKAGE_STRUCTURE;

public final class WialonRequestDataPackageDecoder
        extends AbstractWialonRequestDataPackageDecoder<WialonRequestDataPackage, WialonResponseDataPackage> {

    public WialonRequestDataPackageDecoder(final DataParser dataParser) {
        super(PREFIX, dataParser);
    }

    @Override
    protected Stream<String> splitIntoSubMessages(final String message) {
        return Stream.of(message);
    }

    @Override
    protected WialonRequestDataPackage createPackage(final List<Data> data) {
        checkContainingOneData(data);
        final Data firstData = data.get(0);
        return new WialonRequestDataPackage(firstData);
    }

    @Override
    protected WialonResponseDataPackage createResponseNotValidDataPackage() {
        return new WialonResponseDataPackage(ERROR_PACKAGE_STRUCTURE);
    }

    private static void checkContainingOneData(final List<Data> data) {
        if (!isOneData(data)) {
            throw new IllegalArgumentException("Data package should contain only one message");
        }
    }

    private static boolean isOneData(final List<Data> data) {
        return data.size() == 1;
    }
}
