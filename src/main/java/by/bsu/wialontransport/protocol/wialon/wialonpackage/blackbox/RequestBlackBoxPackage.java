package by.bsu.wialontransport.protocol.wialon.wialonpackage.blackbox;

import by.bsu.wialontransport.crud.dto.Data;
import lombok.*;

import java.util.List;

@Value
public class RequestBlackBoxPackage {
    public static final String PREFIX = "#B#";

    List<Data> data;
}
