package by.bsu.wialontransport.protocol.core.service.receivingdatapackage.validator;

import by.bsu.wialontransport.config.property.DataValidationProperty;
import by.bsu.wialontransport.crud.dto.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Component
@RequiredArgsConstructor
public final class DataPropertyValidator {
    private final DataValidationProperty validationProperty;

    public boolean isValidAmountOfSatellites(final Data data) {
        return this.validationProperty.getMinValidAmountOfSatellites() <= data.getAmountOfSatellites()
                && data.getAmountOfSatellites() <= this.validationProperty.getMaxValidAmountSatellite();
    }

    public boolean isValidDateTime(final Data data) {
        final LocalDateTime research = LocalDateTime.of(data.getDate(), data.getTime());
        final LocalDateTime maxAllowableDateTime = now()
                .plusSeconds(this.validationProperty.getDeltaSecondsFromNowMaxAllowableValidDateTime());
        return research.isAfter(this.validationProperty.getMinValidDateTime())
                && research.isBefore(maxAllowableDateTime);
    }
}
