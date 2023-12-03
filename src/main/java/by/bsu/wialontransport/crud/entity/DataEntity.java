package by.bsu.wialontransport.crud.entity;

import io.hypersistence.utils.hibernate.type.array.DoubleArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@javax.persistence.Entity
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
public class DataEntity extends Entity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "data_id_seq")
    @SequenceGenerator(name = "data_id_seq", sequenceName = "data_id_seq")
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
