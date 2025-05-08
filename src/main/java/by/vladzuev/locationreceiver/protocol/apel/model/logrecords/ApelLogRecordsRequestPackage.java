package by.vladzuev.locationreceiver.protocol.apel.model.logrecords;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import lombok.Value;

import java.util.List;

@Value
public class ApelLogRecordsRequestPackage {
    List<ApelLocation> locations;
}
