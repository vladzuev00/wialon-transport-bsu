package by.bsu.wialontransport.protocol.wialon.wialonpackage.blackbox;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import lombok.*;

import java.util.List;

@Value
public class RequestBlackBoxPackage implements Package {
    public static final String PREFIX = "#B#";

    List<Data> data;
}
