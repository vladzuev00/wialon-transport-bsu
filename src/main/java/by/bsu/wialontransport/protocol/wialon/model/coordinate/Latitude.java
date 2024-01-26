package by.bsu.wialontransport.protocol.wialon.model.coordinate;

import lombok.*;

import static java.util.Arrays.stream;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class Latitude extends GeographicCoordinate {
    private final LatitudeHemisphere hemisphere;

    @Builder
    public Latitude(final int degrees, final int minutes, final int minuteShare, final LatitudeHemisphere hemisphere) {
        super(degrees, minutes, minuteShare);
        this.hemisphere = hemisphere;
    }

    @RequiredArgsConstructor
    @Getter
    public enum LatitudeHemisphere {
        NORTH('N'), SOUTH('S');

        private final char value;

        public static LatitudeHemisphere findByValue(final char value) {
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
