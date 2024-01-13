package by.bsu.wialontransport.protocol.wialon.model.coordinate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class GeographicCoordinate {
    private final int degrees;
    private final int minutes;
    private final int minuteShare;
}
