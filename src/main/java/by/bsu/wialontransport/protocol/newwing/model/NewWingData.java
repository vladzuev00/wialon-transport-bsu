package by.bsu.wialontransport.protocol.newwing.model;

import lombok.Value;

@Value
public class NewWingData {
    short hours;
    short minutes;
    short seconds;
    GpsCoordinateView latitude;
    GpsCoordinateView longitude;
    HDOPView hdop;
    short course;
    SpeedView speed;
    short day;
    short month;
    short year;
    short firstAnalogInputLevel;
    short secondAnalogInputLevel;
    short thirdAnalogInputLevel;
    short fourthAnalogInputLevel;
    byte flagByte;
    byte discreteInputStateByte;
    short checksum;

    @Value
    public static class GpsCoordinateView {
        short integerPart;
        short fractionalPart;
    }

    @Value
    public static class HDOPView {
        byte integerPart;
        byte fractionalPart;
    }

    @Value
    public static class SpeedView {
        short integerPart;
        byte fractionalPart;
    }
}