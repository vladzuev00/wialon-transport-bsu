package by.bsu.wialontransport.protocol.wialon.model.coordinate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class Longitude extends GeographicCoordinate {
    private final LongitudeType type;

    public Longitude(final int degrees, final int minutes, final int minuteShare, final LongitudeType type) {
        super(degrees, minutes, minuteShare);
        this.type = type;
    }

    @RequiredArgsConstructor
    @Getter
    public enum LongitudeType {
        EAST('E'), WESTERN('W');

        private final char value;
    }
}
