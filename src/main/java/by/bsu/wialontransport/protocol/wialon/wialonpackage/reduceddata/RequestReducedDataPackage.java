package by.bsu.wialontransport.protocol.wialon.wialonpackage.reduceddata;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import lombok.Value;

@Value
public class RequestReducedDataPackage implements Package {
    public static final String PREFIX = "#SD#";

    Data data;
}
