package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.crud.entity.DataEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.SOUTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.WESTERN;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

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
    private final Tracker tracker;
    private final Address address;

    public static Data createWithTracker(final Data source, final Tracker tracker) {
        return new Data(
                source.id, source.date, source.time, source.latitude, source.longitude, source.speed, source.course,
                source.altitude, source.amountOfSatellites, source.reductionPrecision, source.inputs, source.outputs,
                source.analogInputs, source.driverKeyCode, source.parametersByNames, tracker, source.address
        );
    }

    public static Data createWithAddress(final Data source, final Address address) {
        return new Data(
                source.id, source.date, source.time, source.latitude, source.longitude, source.speed, source.course,
                source.altitude, source.amountOfSatellites, source.reductionPrecision, source.inputs, source.outputs,
                source.analogInputs, source.driverKeyCode, source.parametersByNames, source.tracker, address
        );
    }

    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    public static abstract class GeographicCoordinate {
        private static final double MINUTES_DIVIDER_TO_FIND_DOUBLE_VALUE = 60.;
        private static final double MINUTE_SHARE_DIVIDER_TO_FIND_DOUBLE_VALUE = 3600.;

        private final int degrees;
        private final int minutes;
        private final int minuteShare;

        public GeographicCoordinate(final GeographicCoordinate other) {
            this.degrees = other.degrees;
            this.minutes = other.minutes;
            this.minuteShare = other.minuteShare;
        }

        public double findDoubleValue() {
            return (signum(this.degrees)
                    * (abs(this.degrees)
                    + this.minutes / MINUTES_DIVIDER_TO_FIND_DOUBLE_VALUE
                    + this.minuteShare / MINUTE_SHARE_DIVIDER_TO_FIND_DOUBLE_VALUE)
            ) * (this.signShouldBeInvertedToFindDoubleValue() ? -1 : 1);
        }

        protected abstract boolean signShouldBeInvertedToFindDoubleValue();
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

        @Override
        protected boolean signShouldBeInvertedToFindDoubleValue() {
            return this.type == SOUTH;
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

        @Override
        protected boolean signShouldBeInvertedToFindDoubleValue() {
            return this.type == WESTERN;
        }
    }
}
