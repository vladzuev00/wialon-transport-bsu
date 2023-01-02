package by.bsu.wialontransport.crud.entity;

import io.hypersistence.utils.hibernate.type.array.DoubleArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tracker_last_extended_data")
@TypeDef(
        name = "double-array",
        typeClass = DoubleArrayType.class
)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class ExtendedDataEntity extends DataEntity {

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

    @Builder(builderMethodName = "extendedDataEntityBuilder")
    public ExtendedDataEntity(final Long id, final LocalDate date, final LocalTime time, final Latitude latitude,
                              final Longitude longitude, final int speed, final int course, final int height,
                              final int amountOfSatellites, final TrackerEntity tracker,
                              final double reductionPrecision, final int inputs, final int outputs,
                              final double[] analogInputs, final String driverKeyCode) {
        super(id, date, time, latitude, longitude, speed, course, height, amountOfSatellites, tracker);
        this.reductionPrecision = reductionPrecision;
        this.inputs = inputs;
        this.outputs = outputs;
        this.analogInputs = analogInputs;
        this.driverKeyCode = driverKeyCode;
    }
}
