package by.vladzuev.locationreceiver.protocol.apel.model;

import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelCurrentLocationResponsePackage;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelCurrentStateRequestPackage;
import lombok.Value;

import java.util.List;

@Value
public class ApelLogRecordsRequestPackage {
    List<ApelSensorStateResponsePackage> sensorStatePackages;
    List<InputOutputStateResponsePackage> inputOutputStatePackages;
    List<ApelCurrentLocationResponsePackage> currentLocationPackages;
    List<ApelCurrentStateRequestPackage> currentStatePackages;
}
