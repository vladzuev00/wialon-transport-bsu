package by.vladzuev.locationreceiver.protocol.apel.model.location;

import lombok.Value;

import java.util.List;

@Value
public class ApelLogRecordsRequestPackage {
    List<ApelLocation> locations;
}
