package by.vladzuev.locationreceiver.crud.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.SEQUENCE;


@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations")
public class LocationEntity extends AbstractEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "location_id_seq")
    @SequenceGenerator(name = "location_id_seq", sequenceName = "location_id_seq")
    private Long id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Embedded
    private GpsCoordinate coordinate;

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

    @Column(name = "analog_inputs")
    private double[] analogInputs;

    @Column(name = "driver_key_code")
    private String driverKeyCode;

    @ToString.Exclude
    @OneToMany(mappedBy = "location")
    private List<ParameterEntity> parameters;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tracker_id")
    private TrackerEntity tracker;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @Setter
    @Getter
    @ToString
    @Embeddable
    @NoArgsConstructor
    @EqualsAndHashCode
    @AllArgsConstructor
    public static final class GpsCoordinate {
        private double latitude;
        private double longitude;
    }
}
