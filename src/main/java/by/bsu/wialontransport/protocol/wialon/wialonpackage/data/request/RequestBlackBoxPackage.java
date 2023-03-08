package by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request;

import by.bsu.wialontransport.crud.dto.Data;

import java.util.List;

public final class RequestBlackBoxPackage extends AbstractRequestDataPackage {
    public static final String PREFIX = "#B#";

    public RequestBlackBoxPackage(final List<Data> data) {
        super(data);
    }
}
