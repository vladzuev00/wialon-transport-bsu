package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.crud.entity.DataEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class Data implements AbstractDto<Long> {
    Long id;
    LocalDate date;
    LocalTime time;
    Latitude latitude;
    Longitude longitude;
    int speed;
    int course;
    int height;
    int amountOfSatellites;

    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    public static abstract class GeographicCoordinate {
        private final int degrees;
        private final int minutes;
        private final int minuteShare;

        public GeographicCoordinate(final GeographicCoordinate other) {
            this.degrees = other.degrees;
            this.minutes = other.minutes;
            this.minuteShare = other.minuteShare;
        }
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Latitude extends GeographicCoordinate {
        private final DataEntity.Latitude.Type type;

        @Builder
        public Latitude(final int degrees, final int minutes, final int minuteShare,
                        final DataEntity.Latitude.Type type) {
            super(degrees, minutes, minuteShare);
            this.type = type;
        }

        public Latitude(final Latitude other) {
            super(other);
            this.type = other.type;
        }
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Longitude extends GeographicCoordinate {
        private final DataEntity.Longitude.Type type;

        @Builder
        public Longitude(final int degrees, final int minutes, final int minuteShare,
                         final DataEntity.Longitude.Type type) {
            super(degrees, minutes, minuteShare);
            this.type = type;
        }

        public Longitude(final Longitude other) {
            super(other);
            this.type = other.type;
        }
    }
}
