package by.bsu.wialontransport.validation.validator.existingid;

import by.bsu.wialontransport.crud.service.CRUDService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ExistingEntityIdValidatorTest {

    @Mock
    private CRUDService<Long, ?, ?, ?, ?> mockedService;

    private TestExistingEntityIdValidator validator;

    @Before
    public void initializeValidator() {
        validator = new TestExistingEntityIdValidator(mockedService);
    }

    @Test
    public void idShouldBeValid() {
        final Long givenId = 255L;
        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);

        when(mockedService.isExist(same(givenId))).thenReturn(true);

        final boolean actual = validator.isValid(givenId, givenContext);
        final boolean expected = true;
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    @Test
    public void idShouldNotBeValidBecauseOfIdIsNull() {
        final Long givenId = null;
        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);

        @SuppressWarnings("all") final boolean actual = validator.isValid(givenId, givenContext);
        final boolean expected = false;
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    @Test
    public void idShouldNotBeValidBecauseOfNotExist() {
        final Long givenId = 255L;
        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);

        when(mockedService.isExist(same(givenId))).thenReturn(false);

        final boolean actual = validator.isValid(givenId, givenContext);
        final boolean expected = false;
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    private static final class TestExistingEntityIdValidator extends ExistingEntityIdValidator<Annotation, Long> {

        public TestExistingEntityIdValidator(final CRUDService<Long, ?, ?, ?, ?> service) {
            super(service);
        }
    }
}
