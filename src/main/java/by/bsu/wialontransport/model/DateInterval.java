package by.bsu.wialontransport.model;

import lombok.Value;

import java.time.LocalDate;

@Value
public class DateInterval {
    LocalDate start;
    LocalDate end;
}
