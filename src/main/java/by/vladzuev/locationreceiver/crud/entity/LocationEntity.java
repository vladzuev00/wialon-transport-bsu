package by.vladzuev.locationreceiver.crud.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.SEQUENCE;


@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "location")
public class LocationEntity extends AbstractEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "location_id_seq")
    @SequenceGenerator(name = "location_id_seq", sequenceName = "location_id_seq")
    private Long id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Embedded
    private Coordinate coordinate;

    @Column(name = "speed")
    private double speed;

    @Column(name = "course")
    private int course;

    @Column(name = "altitude")
    private int altitude;

    @Column(name = "satellite_count")
    private int satelliteCount;

    @Column(name = "hdop")
    private double hdop;

    @Column(name = "inputs")
    private int inputs;

    @Column(name = "outputs")
    private int outputs;

    @JdbcTypeCode(SqlTypes.ARRAY)
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
        if (parameters != null) {
            parameters.forEach(parameter -> parameter.setData(this));
        }
        this.parameters = parameters;
    }

    public void addParameter(final ParameterEntity parameter) {
        parameter.setData(this);
        parameters.add(parameter);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode
    @ToString
    @Embeddable
    public static final class Coordinate {
        private double latitude;
        private double longitude;
    }
}
