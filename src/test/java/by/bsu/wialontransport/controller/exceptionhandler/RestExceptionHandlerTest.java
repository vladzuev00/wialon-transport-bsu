package by.bsu.wialontransport.controller.exceptionhandler;

import by.bsu.wialontransport.base.AbstractContextTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;

import static java.util.Collections.emptySet;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

public final class RestExceptionHandlerTest extends AbstractContextTest {

    @Autowired
    private RestExceptionHandler handler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void constraintViolationExceptionShouldBeHandled()
            throws Exception {
        final ConstraintViolationException givenException = new ConstraintViolationException(
                "exception-message", emptySet()
        );

        final ResponseEntity<?> actualResponseEntity = this.handler.handleException(givenException);
        assertSame(NOT_ACCEPTABLE, actualResponseEntity.getStatusCode());

        final Object actualBody = actualResponseEntity.getBody();
        final String actualBodyString = this.objectMapper.writeValueAsString(actualBody);
        final String expectedBodyStringRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"exception-message\",\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertTrue(actualBodyString.matches(expectedBodyStringRegex));
    }

    @Test
    public void methodArgumentNotValidExceptionShouldBeHandled() {
        throw new RuntimeException();
    }
}
