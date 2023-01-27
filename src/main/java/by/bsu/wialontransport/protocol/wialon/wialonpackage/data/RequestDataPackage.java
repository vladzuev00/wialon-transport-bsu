package by.bsu.wialontransport.protocol.wialon.wialonpackage.data;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import lombok.*;

@Value
public class RequestDataPackage implements Package {
    public static final String PREFIX = "#D#";

    Data data;
}
