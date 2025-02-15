package by.vladzuev.locationreceiver.protocol.newwing.model.request;

import by.vladzuev.locationreceiver.protocol.newwing.model.NewWingLocation;
import lombok.Value;

import java.util.List;

@Value
public class NewWingLocationPackage {
    List<NewWingLocation> locations;
}
