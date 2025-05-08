package by.vladzuev.locationreceiver.protocol.apel.model;

import lombok.Value;

@Value
public class ApelSensorStateResponsePackage {
    double[] analogInputs;
}
