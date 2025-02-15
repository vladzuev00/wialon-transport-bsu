package by.vladzuev.locationreceiver.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class DateInterval {
    LocalDateTime start;
    LocalDateTime end;
}
