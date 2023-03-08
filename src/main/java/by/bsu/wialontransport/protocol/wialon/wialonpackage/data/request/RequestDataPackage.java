package by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request;

import by.bsu.wialontransport.crud.dto.Data;

import static java.util.Collections.singletonList;

public final class RequestDataPackage extends AbstractRequestDataPackage {
    public static final String PREFIX = "#D#";

    public RequestDataPackage(final Data data) {
        super(singletonList(data));
    }
}
