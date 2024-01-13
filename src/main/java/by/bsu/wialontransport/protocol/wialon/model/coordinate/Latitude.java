package by.bsu.wialontransport.protocol.wialon.model.coordinate;

import lombok.*;

import static java.util.Arrays.stream;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class Latitude extends GeographicCoordinate {
    private final LatitudeType type;

    @Builder
    public Latitude(final int degrees, final int minutes, final int minuteShare, final LatitudeType type) {
        super(degrees, minutes, minuteShare);
        this.type = type;
    }

    @RequiredArgsConstructor
    @Getter
    public enum LatitudeType {
        NORTH('N'), SOUTH('S');

        private final char value;

        public static LatitudeType findByValue(final char value) {
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
