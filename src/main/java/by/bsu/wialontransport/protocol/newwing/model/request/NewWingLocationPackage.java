package by.bsu.wialontransport.protocol.newwing.model.request;

import by.bsu.wialontransport.protocol.newwing.model.NewWingLocation;
import lombok.Value;

import java.util.List;

@Value
public class NewWingLocationPackage {
    List<NewWingLocation> locations;
}
