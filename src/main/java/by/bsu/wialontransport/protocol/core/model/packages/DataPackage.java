package by.bsu.wialontransport.protocol.core.model.packages;

import by.bsu.wialontransport.crud.dto.Location;

import java.util.List;

public interface DataPackage {
    List<Location> getData();
}
