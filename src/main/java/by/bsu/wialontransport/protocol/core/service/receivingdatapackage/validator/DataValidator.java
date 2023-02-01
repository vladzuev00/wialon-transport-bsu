package by.bsu.wialontransport.protocol.core.service.receivingdatapackage.validator;

import by.bsu.wialontransport.crud.dto.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public final class DataValidator {
    private final DataPropertyValidator propertyValidator;

    public boolean isValid(final Data data) {
        return this.propertyValidator.isValidDateTime(data)
                && this.propertyValidator.isValidAmountOfSatellites(data)
                && this.propertyValidator.isContainDOPParameters(data);
    }

    public boolean isValid(final Data current, final Data previous) {
        return this.isValid(current) && isCorrectOrder(current, previous);
    }

    public boolean isNeededToBeFixed(final Data current, final Data previous) {
        return this.propertyValidator.isValidDateTime(current)
                && !(this.propertyValidator.isValidAmountOfSatellites(current)
                && this.propertyValidator.isContainDOPParameters(research));
    }

    private static boolean isCorrectOrder(final Data current, final Data previous) {
        final LocalDateTime dateTimeOfCurrentData = LocalDateTime.of(current.getDate(), current.getTime());
        final LocalDateTime dateTimeOfPreviousData = LocalDateTime.of(previous.getDate(), previous.getTime());
        return dateTimeOfCurrentData.isAfter(dateTimeOfPreviousData);
    }
}
