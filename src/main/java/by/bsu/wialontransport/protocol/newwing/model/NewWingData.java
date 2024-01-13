package by.bsu.wialontransport.protocol.newwing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class NewWingData {
    byte hour;
    byte minute;
    byte second;
    short latitudeIntegerPart;
    short latitudeFractionalPart;
    short longitudeIntegerPart;
    short longitudeFractionalPart;
    byte reductionPrecisionIntegerPart;
    byte reductionPrecisionFractionalPart;
    short course;
    short speedIntegerPart;
    byte speedFractionalPart;
    byte day;
    byte month;
    byte year;
    short firstAnalogInputLevel;
    short secondAnalogInputLevel;
    short thirdAnalogInputLevel;
    short fourthAnalogInputLevel;
    byte flagByte;
    byte discreteInputStateByte;
    short checksum;
}