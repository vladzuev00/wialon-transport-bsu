package by.vladzuev.locationreceiver.protocol.newwing.model.request;

import lombok.Value;

import java.util.List;

@Value
public class NewWingLocationPackage {
    List<NewWingLocation> locations;
}
