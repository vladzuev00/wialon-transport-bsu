package by.bsu.wialontransport.protocol.jt808.model;

import lombok.Value;

import java.util.List;

@Value
public class JT808LocationMessage {
    List<Location> location;
}
