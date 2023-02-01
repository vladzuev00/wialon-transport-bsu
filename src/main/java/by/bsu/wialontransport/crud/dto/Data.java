package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.crud.entity.DataEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

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
    private final Map<String, Parameter> parameters;

    @Builder(builderMethodName = "dataBuilder")
    public Data(final Long id, final LocalDate date, final LocalTime time, final Latitude latitude,
                final Longitude longitude, final int speed, final int course, final int altitude,
                final int amountOfSatellites, final double reductionPrecision, final int inputs,
                final int outputs, final double[] analogInputs, final String driverKeyCode,
                final Map<String, Parameter> parameters) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.course = course;
        this.altitude = altitude;
        this.amountOfSatellites = amountOfSatellites;
        this.reductionPrecision = reductionPrecision;
        this.inputs = inputs;
        this.outputs = outputs;
        this.analogInputs = analogInputs;
        this.driverKeyCode = driverKeyCode;
        this.parameters = parameters;
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
