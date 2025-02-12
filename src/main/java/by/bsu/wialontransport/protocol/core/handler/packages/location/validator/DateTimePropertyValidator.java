package by.bsu.wialontransport.protocol.core.handler.packages.location.validator;

import by.bsu.wialontransport.config.property.LocationValidationProperty;
import by.bsu.wialontransport.crud.dto.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.time.LocalDateTime.now;

@Component
@RequiredArgsConstructor
public final class DateTimePropertyValidator extends LocationPropertyValidator<LocalDateTime> {
    private final LocationValidationProperty validationProperty;

    @Override
    protected LocalDateTime getValue(final Location location) {
        return location.getDateTime();
    }

    @Override
    protected LocalDateTime getMin() {
        return validationProperty.getMinDateTime();
    }

    @Override
    protected LocalDateTime getMax() {
        return now().plus(validationProperty.getMaxDateTimeDeltaFromNow());
    }

    @Override
    protected boolean isLessOrEqual(final LocalDateTime first, final LocalDateTime second) {
        return Objects.equals(first, second) || first.isBefore(second);
    }

    @Override
    protected boolean isBiggerOrEqual(final LocalDateTime first, final LocalDateTime second) {
        return Objects.equals(first, second) || first.isAfter(second);
    }
}
