package by.bsu.wialontransport.protocol.core.service.receivingdatapackage;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.DataCalculations;
import org.springframework.stereotype.Component;

@Component
public final class DataCalculationsFactory {

    public DataCalculations create(final Data current) {
        return DataCalculations.builder().build();
    }

    //previous data must have calculations: throw IllegalStateException if not(maybe check when put in context)
    public DataCalculations create(final Data current, final Data previous) {
        return DataCalculations.builder().build();
    }
}
