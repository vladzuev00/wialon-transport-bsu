package by.vladzuev.locationreceiver.protocol.teltonika.model.location;

import lombok.Value;

import java.util.List;

@Value
public class TeltonikaRequestLocationPackage {
    List<TeltonikaLocation> locations;
}
