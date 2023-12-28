package by.bsu.wialontransport.model;

import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
public class DateInterval {
    LocalDateTime start;
    LocalDateTime end;
}
