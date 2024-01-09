package by.bsu.wialontransport.protocol.core.handler.packages.receivingdata;

import by.bsu.wialontransport.configuration.property.DataValidationConfiguration;
import by.bsu.wialontransport.model.ReceivedData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Component
@RequiredArgsConstructor
public final class ReceivedDataValidator {
    private final DataValidationConfiguration validationProperty;

    public boolean isValid(final ReceivedData data) {
        return isValidAmountOfSatellites(data) && isValidDateTime(data);
    }

    private boolean isValidAmountOfSatellites(final ReceivedData data) {
        final int research = data.getAmountOfSatellites();
        return validationProperty.getMinValidAmountOfSatellites() <= research
                && research <= validationProperty.getMaxValidAmountSatellites();
    }

    private boolean isValidDateTime(final ReceivedData data) {
        final LocalDateTime research = data.getDateTime();
        final LocalDateTime minAllowableDateTime = validationProperty.getMinValidDateTime();
        final LocalDateTime maxAllowableDateTime = findMaxAllowableDateTime();
        return research.isAfter(minAllowableDateTime) && research.isBefore(maxAllowableDateTime);
    }

    private LocalDateTime findMaxAllowableDateTime() {
        return now().plusSeconds(validationProperty.getDeltaSecondsFromNowMaxAllowableValidDateTime());
    }
}
