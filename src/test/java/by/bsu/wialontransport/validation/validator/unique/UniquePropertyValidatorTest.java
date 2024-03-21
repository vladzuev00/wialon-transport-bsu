package by.bsu.wialontransport.validation.validator.unique;

import by.bsu.wialontransport.crud.service.CRUDService;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class UniquePropertyValidatorTest {
    private final TestUniquePropertyValidator validator = new TestUniquePropertyValidator();

    @Test
    public void valueShouldBeValidated() {
        final String givenValue = "value";
        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);

        final boolean actual = validator.isValid(givenValue, givenContext);
        final boolean expected = false;
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    private static final class TestUniquePropertyValidator extends UniquePropertyValidator<Annotation, String, CRUDService<?, ?, ?, ?, ?>> {

        public TestUniquePropertyValidator() {
            super(null);
        }

        @Override
        protected boolean isExist(final CRUDService<?, ?, ?, ?, ?> service, final String value) {
            return true;
        }
    }
}
