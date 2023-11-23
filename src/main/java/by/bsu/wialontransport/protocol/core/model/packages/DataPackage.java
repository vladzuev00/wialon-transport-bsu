package by.bsu.wialontransport.protocol.core.model.packages;

import by.bsu.wialontransport.crud.dto.Data;

import java.util.List;

public interface DataPackage extends Package {
    List<Data> findData();
}
