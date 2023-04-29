package by.bsu.wialontransport.crud.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class DateTimeRange {
    LocalDateTime fromDateTime;
    LocalDateTime toDateTime;
}
