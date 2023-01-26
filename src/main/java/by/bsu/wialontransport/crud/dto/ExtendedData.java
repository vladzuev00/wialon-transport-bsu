package by.bsu.wialontransport.crud.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class ExtendedData extends Data {
    private final double reductionPrecision;
    private final int inputs;
    private final int outputs;
    private final double[] analogInputs;
    private final String driverKeyCode;

    /**
     * parameter's name to parameter
     */
    private final Map<String, Parameter> parameters;

    @Builder(builderMethodName = "extendedDataBuilder")
    public ExtendedData(final Long id, final LocalDate date, final LocalTime time, final Latitude latitude,
                        final Longitude longitude, final int speed, final int course, final int height,
                        final int amountOfSatellites, final double reductionPrecision, final int inputs,
                        final int outputs, final double[] analogInputs, final String driverKeyCode,
                        final Map<String, Parameter> parameters) {
        super(id, date, time, latitude, longitude, speed, course, height, amountOfSatellites);
        this.reductionPrecision = reductionPrecision;
        this.inputs = inputs;
        this.outputs = outputs;
        this.analogInputs = analogInputs;
        this.driverKeyCode = driverKeyCode;
        this.parameters = parameters;
    }
}
