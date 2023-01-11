package by.bsu.wialontransport.protocol.wialon.wialonpackage.data;

import by.bsu.wialontransport.crud.dto.ExtendedData;
import lombok.*;

@Value
public class RequestDataPackage {
    public static final String PREFIX = "#D#";

    ExtendedData extendedData;
}
