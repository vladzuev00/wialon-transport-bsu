package by.bsu.wialontransport.protocol.core.handler.packages.location.validator.property;

import by.bsu.wialontransport.config.property.LocationValidationProperty;
import by.bsu.wialontransport.crud.dto.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class DateTimePropertyValidatorTest {

    @Mock
    private LocationValidationProperty mockedProperty;

    private DateTimePropertyValidator validator;

    @BeforeEach
    public void initializeValidator() {
        validator = new DateTimePropertyValidator(mockedProperty);
    }

    @Test
    public void valueShouldBeGot() {
        final LocalDateTime givenDateTime = LocalDateTime.of(2024, 11, 11, 10, 12, 13);
        final Location givenLocation = Location.builder().dateTime(givenDateTime).build();

        final LocalDateTime actual = validator.getValue(givenLocation);
        assertSame(givenDateTime, actual);
    }

    @Test
    public void minAllowableShouldBeGot() {
        final LocalDateTime givenDateTime = LocalDateTime.of(2024, 11, 11, 10, 12, 13);
        when(mockedProperty.getMinDateTime()).thenReturn(givenDateTime);

        final LocalDateTime actual = validator.getMin();
        assertSame(givenDateTime, actual);
    }

    @Test
    public void maxAllowableShouldBeGot() {
        final Duration givenMaxDateTimeDeltaFromNow = Duration.of(5, SECONDS);
        when(mockedProperty.getMaxDateTimeDeltaFromNow()).thenReturn(givenMaxDateTimeDeltaFromNow);

        final LocalDateTime actual = validator.getMax();
        assertNotNull(actual);
    }

    @Test
    public void firstShouldBeLessOrEqualThanSecond() {
        final LocalDateTime givenFirst = LocalDateTime.of(2024, 11, 11, 10, 12, 13);
        final LocalDateTime givenSecond = LocalDateTime.of(2024, 11, 11, 10, 12, 14);

        assertTrue(validator.isLessOrEqual(givenFirst, givenSecond));
    }

    @Test
    public void firstShouldNotBeLessOrEqualThanSecond() {
        final LocalDateTime givenFirst = LocalDateTime.of(2024, 11, 11, 10, 12, 13);
        final LocalDateTime givenSecond = LocalDateTime.of(2024, 11, 11, 10, 12, 12);

        assertFalse(validator.isLessOrEqual(givenFirst, givenSecond));
    }

    @Test
    public void firstShouldBeBiggerOrEqualThanSecond() {
        final LocalDateTime givenFirst = LocalDateTime.of(2024, 11, 11, 10, 12, 15);
        final LocalDateTime givenSecond = LocalDateTime.of(2024, 11, 11, 10, 12, 14);

        assertTrue(validator.isBiggerOrEqual(givenFirst, givenSecond));
    }

    @Test
    public void firstShouldNotBeBiggerOrEqualThanSecond() {
        final LocalDateTime givenFirst = LocalDateTime.of(2024, 11, 11, 10, 12, 13);
        final LocalDateTime givenSecond = LocalDateTime.of(2024, 11, 11, 10, 12, 14);

        assertFalse(validator.isBiggerOrEqual(givenFirst, givenSecond));
    }
}
