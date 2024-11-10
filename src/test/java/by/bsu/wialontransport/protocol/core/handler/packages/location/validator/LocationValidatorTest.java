package by.bsu.wialontransport.protocol.core.handler.packages.location.validator;

import by.bsu.wialontransport.crud.dto.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class LocationValidatorTest {

    @Mock
    private LocationPropertyValidator<?> firstMockedPropertyValidator;

    @Mock
    private LocationPropertyValidator<?> secondMockedPropertyValidator;

    @Mock
    private LocationPropertyValidator<?> thirdMockedPropertyValidator;

    private LocationValidator validator;

    @BeforeEach
    public void initializeValidator() {
        validator = new LocationValidator(
                List.of(
                        firstMockedPropertyValidator,
                        secondMockedPropertyValidator,
                        thirdMockedPropertyValidator
                )
        );
    }

    @Test
    public void locationShouldBeValid() {
        final Location givenLocation = Location.builder().build();

        when(firstMockedPropertyValidator.isValid(same(givenLocation))).thenReturn(true);
        when(secondMockedPropertyValidator.isValid(same(givenLocation))).thenReturn(true);
        when(thirdMockedPropertyValidator.isValid(same(givenLocation))).thenReturn(true);

        assertTrue(validator.isValid(givenLocation));
    }

    @Test
    public void locationShouldNotBeValid() {
        final Location givenLocation = Location.builder().build();

        when(firstMockedPropertyValidator.isValid(same(givenLocation))).thenReturn(true);
        when(secondMockedPropertyValidator.isValid(same(givenLocation))).thenReturn(false);

        assertFalse(validator.isValid(givenLocation));

        verifyNoInteractions(thirdMockedPropertyValidator);
    }
}
