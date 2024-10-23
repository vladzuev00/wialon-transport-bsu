package by.bsu.wialontransport.protocol.jt808.model;

import lombok.Value;

import java.util.List;

@Value
public class JT808LocationPackage {
    List<JT808Location> locations;
}
