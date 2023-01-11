package by.bsu.wialontransport.protocol.wialon.wialonpackage.data;

import by.bsu.wialontransport.crud.dto.ExtendedData;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import lombok.*;

@Value
public class RequestDataPackage implements Package {
    public static final String PREFIX = "#D#";

    ExtendedData extendedData;
}
