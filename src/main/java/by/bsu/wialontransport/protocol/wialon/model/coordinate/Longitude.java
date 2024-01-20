package by.bsu.wialontransport.protocol.wialon.model.coordinate;

import lombok.*;

import static java.util.Arrays.stream;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class Longitude extends GeographicCoordinate {
    private final LongitudeHemisphere hemisphere;

    @Builder
    public Longitude(final int degrees, final int minutes, final int minuteShare, final LongitudeHemisphere hemisphere) {
        super(degrees, minutes, minuteShare);
        this.hemisphere = hemisphere;
    }

    @RequiredArgsConstructor
    @Getter
    public enum LongitudeHemisphere {
        EAST('E'), WESTERN('W');

        private final char value;

        public static LongitudeHemisphere findByValue(final char value) {
            return stream(values())
                    .filter(type -> type.value == value)
                    .findFirst()
                    .orElseThrow(
                            () -> new IllegalArgumentException(
                                    "There is no type with value '%s'".formatted(value)
                            )
                    );
        }
    }
}
