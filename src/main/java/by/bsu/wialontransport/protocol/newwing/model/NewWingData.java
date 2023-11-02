package by.bsu.wialontransport.protocol.newwing.model;

import lombok.Value;

@Value
public class NewWingData {
    byte hours;
    byte minutes;
    byte seconds;
    short latitudeIntegerPart;
    short latitudeFractionalPart;
    short longitudeIntegerPart;
    short longitudeFractionalPart;
    byte hdopIntegerPart;
    byte hdopFractionalPart;
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