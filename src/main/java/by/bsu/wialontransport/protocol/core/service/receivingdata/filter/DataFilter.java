package by.bsu.wialontransport.protocol.core.service.receivingdata.filter;

import by.bsu.wialontransport.crud.dto.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//TODO: refactor tests
@Component
public final class DataFilter {
    private final DataPropertyValidator propertyValidator;
    private final boolean filteringEnable;

    public DataFilter(final DataPropertyValidator propertyValidator,
                      @Value("${data-filtering.enable}") final boolean filteringEnable) {
        this.propertyValidator = propertyValidator;
        this.filteringEnable = filteringEnable;
    }

    public boolean isValid(final Data data) {
        return !this.filteringEnable
                || (
                this.propertyValidator.isValidDateTime(data)
                        && this.propertyValidator.isValidAmountOfSatellites(data)
                        && this.propertyValidator.isValidDOPParameters(data)
        );
    }

    public boolean isValid(final Data current, final Data previous) {
        return this.isValid(current) && isCorrectOrder(current, previous);
    }

    public boolean isNeedToBeFixed(final Data current, final Data previous) {
        return !this.filteringEnable
                || (
                this.propertyValidator.isValidDateTime(current)
                        && isCorrectOrder(current, previous)
                        && !(
                        this.propertyValidator.isValidAmountOfSatellites(current)
                                && this.propertyValidator.isValidDOPParameters(current)
                )
        );
    }

    private static boolean isCorrectOrder(final Data current, final Data previous) {
        final LocalDateTime dateTimeOfCurrentData = LocalDateTime.of(current.getDate(), current.getTime());
        final LocalDateTime dateTimeOfPreviousData = LocalDateTime.of(previous.getDate(), previous.getTime());
        return dateTimeOfCurrentData.isEqual(dateTimeOfPreviousData)
                || dateTimeOfCurrentData.isAfter(dateTimeOfPreviousData);
    }
}
