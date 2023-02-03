package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.crud.entity.DataEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Data implements AbstractDto<Long> {
    private final Long id;
    private final LocalDate date;
    private final LocalTime time;
    private final Latitude latitude;
    private final Longitude longitude;
    private final int speed;
    private final int course;
    private final int altitude;
    private final int amountOfSatellites;
    private final double reductionPrecision;
    private final int inputs;
    private final int outputs;
    private final double[] analogInputs;
    private final String driverKeyCode;

    /**
     * parameter's name to parameter
     */
    private final Map<String, Parameter> parametersByNames;
    private final DataCalculations dataCalculations;

    public Data(final Data other, final DataCalculations dataCalculations) {
        this.id = other.id;
        this.date = other.date;
        this.time = other.time;
        this.latitude = other.latitude;
        this.longitude = other.longitude;
        this.speed = other.speed;
        this.course = other.course;
        this.altitude = other.altitude;
        this.amountOfSatellites = other.amountOfSatellites;
        this.reductionPrecision = other.reductionPrecision;
        this.inputs = other.inputs;
        this.outputs = other.outputs;
        this.analogInputs = other.analogInputs;
        this.driverKeyCode = other.driverKeyCode;
        this.parametersByNames = other.parametersByNames;
        this.dataCalculations = dataCalculations;
    }

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
