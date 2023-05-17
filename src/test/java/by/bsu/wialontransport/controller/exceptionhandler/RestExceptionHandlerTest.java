package by.bsu.wialontransport.controller.exceptionhandler;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.controller.exception.CustomValidationException;
import by.bsu.wialontransport.controller.exception.NoSuchEntityException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.validation.ConstraintViolationException;

import static java.util.Collections.emptySet;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
    public void methodArgumentNotValidExceptionShouldBeHandled()
            throws Exception {
        final MethodArgumentNotValidException givenException = mock(MethodArgumentNotValidException.class);

        final FieldError givenFieldError = mock(FieldError.class);
        when(givenException.getFieldError()).thenReturn(givenFieldError);

        final String givenField = "field-name";
        when(givenFieldError.getField()).thenReturn(givenField);

        final String givenDefaultMessage = "message";
        when(givenFieldError.getDefaultMessage()).thenReturn(givenDefaultMessage);

        final ResponseEntity<?> actualResponseEntity = this.handler.handleException(givenException);
        assertSame(NOT_ACCEPTABLE, actualResponseEntity.getStatusCode());

        final Object actualBody = actualResponseEntity.getBody();
        final String actualBodyString = this.objectMapper.writeValueAsString(actualBody);
        final String expectedBodyStringRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\",\"message\":\"field-name : message\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertTrue(actualBodyString.matches(expectedBodyStringRegex));
    }

    @Test
    public void methodArgumentNotValidExceptionWithNullAsFieldErrorShouldBeHandled()
            throws Exception {
        final MethodArgumentNotValidException givenException = mock(MethodArgumentNotValidException.class);
        when(givenException.getFieldError()).thenReturn(null);

        final ResponseEntity<?> actualResponseEntity = this.handler.handleException(givenException);
        assertSame(NOT_ACCEPTABLE, actualResponseEntity.getStatusCode());

        final Object actualBody = actualResponseEntity.getBody();
        final String actualBodyString = this.objectMapper.writeValueAsString(actualBody);
        final String expectedBodyStringRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"unknown error\",\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertTrue(actualBodyString.matches(expectedBodyStringRegex));
    }

    @Test
    public void customValidationExceptionShouldBeHandled()
            throws Exception {
        final CustomValidationException givenException = new CustomValidationException("exception-message");

        final ResponseEntity<?> actualResponseEntity = this.handler.handleException(givenException);
        assertSame(NOT_ACCEPTABLE, actualResponseEntity.getStatusCode());

        final Object actualBody = actualResponseEntity.getBody();
        final String actualBodyString = this.objectMapper.writeValueAsString(actualBody);
        final String expectedBodyStringRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\",\"message\":\"exception-message\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertTrue(actualBodyString.matches(expectedBodyStringRegex));
    }

    @Test
    public void noSuchEntityExceptionShouldBeHandled()
            throws Exception {
        final NoSuchEntityException givenException = new NoSuchEntityException("exception-message");

        final ResponseEntity<?> actualResponseEntity = this.handler.handleException(givenException);
        assertSame(NOT_FOUND, actualResponseEntity.getStatusCode());

        final Object actualBody = actualResponseEntity.getBody();
        final String actualBodyString = this.objectMapper.writeValueAsString(actualBody);
        final String expectedBodyStringRegex = "\\{\"httpStatus\":\"NOT_FOUND\",\"message\":\"exception-message\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertTrue(actualBodyString.matches(expectedBodyStringRegex));
    }

    @Test
    public void conversionFailedExceptionShouldBeHandled() {
        throw new RuntimeException();
    }

    @Test
    public void conversionFailedExceptionWithEnumTargetTypeShouldBeHandled() {
        throw new RuntimeException();
    }

    @Test
    public void missingServletRequestParameterExceptionShouldBeHandled()
            throws Exception {
        final MissingServletRequestParameterException givenException = new MissingServletRequestParameterException(
                "parameter-name", "parameter-type"
        );

        final ResponseEntity<?> actualResponseEntity = this.handler.handleException(givenException);
        assertSame(NOT_ACCEPTABLE, actualResponseEntity.getStatusCode());

        final Object actualBody = actualResponseEntity.getBody();
        final String actualBodyString = this.objectMapper.writeValueAsString(actualBody);
        final String expectedBodyStringRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"Required request parameter 'parameter-name' for method parameter "
                + "type parameter-type is not present\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertTrue(actualBodyString.matches(expectedBodyStringRegex));
    }

}
