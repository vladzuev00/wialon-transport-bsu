package by.bsu.wialontransport.protocol.jt808.model;

import by.bsu.wialontransport.crud.dto.Location;
import lombok.Value;

import java.util.List;

@Value
public class JT808LocationMessage {
    List<Location> locations;
}
