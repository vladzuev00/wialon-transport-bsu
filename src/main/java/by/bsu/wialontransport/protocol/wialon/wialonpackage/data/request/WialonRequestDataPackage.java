package by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request;

import by.bsu.wialontransport.crud.dto.Data;

import static java.util.Collections.singletonList;

public final class WialonRequestDataPackage extends AbstractWialonRequestDataPackage {
    public static final String PREFIX = "#D#";

    public WialonRequestDataPackage(final Data data) {
        super(singletonList(data));
    }
}
