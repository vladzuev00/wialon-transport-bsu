package by.bsu.wialontransport.util;

import by.bsu.wialontransport.crud.dto.Parameter;
import lombok.experimental.UtilityClass;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;

@UtilityClass
public final class DataParameterUtil {
    private static final String HDOP_NAME = "HDOP";

    public static Parameter createHDOP(final double value) {
        return Parameter.builder()
                .name(HDOP_NAME)
                .type(DOUBLE)
                .value(Double.toString(value))
                .build();
    }
}
