package by.bsu.wialontransport.crud.entity;

import by.bsu.wialontransport.crud.entity.converter.LatitudeTypeConverter;
import by.bsu.wialontransport.crud.entity.converter.LongitudeTypeConverter;
import io.hypersistence.utils.hibernate.type.array.DoubleArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.util.Arrays.stream;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "data")
@TypeDef(
        name = "double-array",
        typeClass = DoubleArrayType.class
)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class DataEntity extends AbstractEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "data_id_seq")
    @SequenceGenerator(name = "data_id_seq", sequenceName = "data_id_seq")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "degrees", column = @Column(name = "latitude_degrees")),
                    @AttributeOverride(name = "minutes", column = @Column(name = "latitude_minutes")),
                    @AttributeOverride(name = "minuteShare", column = @Column(name = "latitude_minute_share")),
                    @AttributeOverride(name = "type", column = @Column(name = "latitude_type"))
            }
    )
    private Latitude latitude;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "degrees", column = @Column(name = "longitude_degrees")),
                    @AttributeOverride(name = "minutes", column = @Column(name = "longitude_minutes")),
                    @AttributeOverride(name = "minuteShare", column = @Column(name = "longitude_minute_share")),
                    @AttributeOverride(name = "type", column = @Column(name = "longitude_type"))
            }
    )
    private Longitude longitude;

    @Column(name = "speed")
    private int speed;

    @Column(name = "course")
    private int course;

    @Column(name = "altitude")
    private int altitude;

    @Column(name = "amount_of_satellites")
    private int amountOfSatellites;

    @Column(name = "reduction_precision")
    private double reductionPrecision;

    @Column(name = "inputs")
    private int inputs;

    @Column(name = "outputs")
    private int outputs;

    @Type(type = "double-array")
    @Column(name = "analog_inputs")
    private double[] analogInputs;

    @Column(name = "driver_key_code")
    private String driverKeyCode;

    @ToString.Exclude
    @OneToMany(mappedBy = "data", cascade = PERSIST)
    private List<ParameterEntity> parameters;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tracker_id")
    @ToString.Exclude
    private TrackerEntity tracker;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "address_id")
    @ToString.Exclude
    private AddressEntity address;

    public void setParameters(final List<ParameterEntity> parameters) {
        parameters.forEach(parameter -> parameter.setData(this));
        this.parameters = parameters;
    }

    public void addParameter(final ParameterEntity parameter) {
        parameter.setData(this);
        this.parameters.add(parameter);
    }

    @MappedSuperclass
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode
    @ToString
    public static abstract class GeographicCoordinate {
        private int degrees;
        private int minutes;
        private int minuteShare;

        public GeographicCoordinate(final GeographicCoordinate other) {
            this.degrees = other.degrees;
            this.minutes = other.minutes;
            this.minuteShare = other.minuteShare;
        }
    }

    //lat (5544.6025;N)
    //градусы (2 знака) минуты (2 знака).доли минуты (количество знаков кастомное);
    //N-северная широта, S-Южная широта
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Latitude extends GeographicCoordinate {

        @Convert(converter = LatitudeTypeConverter.class)
        private Type type;

        @Builder
        public Latitude(final int degrees, final int minutes, final int minuteShare, final Type type) {
            super(degrees, minutes, minuteShare);
            this.type = type;
        }

        public Latitude(final Latitude other) {
            super(other);
            this.type = other.type;
        }

        public enum Type {
            NOT_DEFINED('-'), NORTH('N'), SOUTH('S');

            private final char value;

            Type(final char value) {
                this.value = value;
            }

            public final char getValue() {
                return this.value;
            }

            public static Type findByValue(final char value) {
                return stream(Type.values())
                        .filter(type -> type.value == value)
                        .findAny()
                        .orElse(NOT_DEFINED);
            }
        }
    }

    //lon (03739.6834;E),
    //градусы (3 знака) минуты (2 знака).доли минуты (количество знаков кастомное);
    //E- восточная долгота, W-западная долгота
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Longitude extends GeographicCoordinate {

        @Convert(converter = LongitudeTypeConverter.class)
        private Type type;

        @Builder
        public Longitude(final int degrees, final int minutes, final int minuteShare, final Type type) {
            super(degrees, minutes, minuteShare);
            this.type = type;
        }

        public Longitude(final Longitude other) {
            super(other);
            this.type = other.type;
        }

        public enum Type {
            NOT_DEFINED('-'), EAST('E'), WESTERN('W');

            private final char value;

            Type(final char value) {
                this.value = value;
            }

            public final char getValue() {
                return this.value;
            }

            public static Type findByValue(final char value) {
                return stream(Type.values())
                        .filter(type -> type.value == value)
                        .findAny()
                        .orElse(NOT_DEFINED);
            }
        }
    }
}
