//package by.vladzuev.locationreceiver.controller.exceptionhandler;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import by.vladzuev.locationreceiver.controller.exception.CustomValidationException;
//import by.vladzuev.locationreceiver.controller.exception.NoSuchEntityException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//import org.skyscreamer.jsonassert.comparator.CustomComparator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.convert.ConversionFailedException;
//import org.springframework.core.convert.TypeDescriptor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.MissingServletRequestParameterException;
//
//import javax.validation.ConstraintViolationException;
//
//import static java.util.Collections.emptySet;
//import static org.junit.Assert.assertSame;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.skyscreamer.jsonassert.Customization.customization;
//import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
//import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
//import static org.springframework.core.convert.TypeDescriptor.valueOf;
//import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
//import static org.springframework.http.HttpStatus.NOT_FOUND;
//
//public final class RestExceptionHandlerTest extends AbstractSpringBootTest {
//    private static final String JSON_PROPERTY_NAME_DATE_TIME = "dateTime";
//    private static final CustomComparator JSON_COMPARATOR_IGNORING_DATE_TIME = new CustomComparator(
//            STRICT,
//            customization(JSON_PROPERTY_NAME_DATE_TIME, (first, second) -> true)
//    );
//
//    @Autowired
//    private RestExceptionHandler handler;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void constraintViolationExceptionShouldBeHandled()
//            throws Exception {
//        final ConstraintViolationException givenException = new ConstraintViolationException(
//                "exception-message",
//                emptySet()
//        );
//
//        final ResponseEntity<?> actualResponseEntity = handler.handle(givenException);
//        assertSame(NOT_ACCEPTABLE, actualResponseEntity.getStatusCode());
//
//        final String actualBody = objectMapper.writeValueAsString(actualResponseEntity.getBody());
//        final String expectedBody = """
//                {
//                  "httpStatus": "NOT_ACCEPTABLE",
//                  "message": "exception-message",
//                  "dateTime": "2024-02-24 15-29-32"
//                }""";
//        assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void methodArgumentNotValidExceptionShouldBeHandled()
//            throws Exception {
//        final MethodArgumentNotValidException givenException = mock(MethodArgumentNotValidException.class);
//
//        final FieldError givenFieldError = mock(FieldError.class);
//        when(givenException.getFieldError()).thenReturn(givenFieldError);
//
//        final String givenField = "field-name";
//        when(givenFieldError.getField()).thenReturn(givenField);
//
//        final String givenDefaultMessage = "message";
//        when(givenFieldError.getDefaultMessage()).thenReturn(givenDefaultMessage);
//
//        final ResponseEntity<?> actualResponseEntity = handler.handle(givenException);
//        assertSame(NOT_ACCEPTABLE, actualResponseEntity.getStatusCode());
//
//        final String actualBody = objectMapper.writeValueAsString(actualResponseEntity.getBody());
//        final String expectedBody = """
//                {
//                   "httpStatus": "NOT_ACCEPTABLE",
//                   "message": "field-name : message",
//                   "dateTime": "2024-02-24 15-34-09"
//                 }""";
//        assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void methodArgumentNotValidExceptionWithNullAsFieldErrorShouldBeHandled()
//            throws Exception {
//        final MethodArgumentNotValidException givenException = mock(MethodArgumentNotValidException.class);
//        when(givenException.getFieldError()).thenReturn(null);
//
//        final ResponseEntity<?> actualResponseEntity = handler.handle(givenException);
//        assertSame(NOT_ACCEPTABLE, actualResponseEntity.getStatusCode());
//
//        final String actualBody = objectMapper.writeValueAsString(actualResponseEntity.getBody());
//        final String expectedBody = """
//                {
//                    "httpStatus": "NOT_ACCEPTABLE",
//                    "message": "unknown error",
//                    "dateTime": "2024-02-24 15-37-38"
//                }""";
//        assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void customValidationExceptionShouldBeHandled()
//            throws Exception {
//        final CustomValidationException givenException = new CustomValidationException("exception-message");
//
//        final ResponseEntity<?> actualResponseEntity = handler.handle(givenException);
//        assertSame(NOT_ACCEPTABLE, actualResponseEntity.getStatusCode());
//
//        final String actualBody = objectMapper.writeValueAsString(actualResponseEntity.getBody());
//        final String expectedBody = """
//                {
//                   "httpStatus": "NOT_ACCEPTABLE",
//                   "message": "exception-message",
//                   "dateTime": "2024-02-24 15-41-45"
//                }""";
//        assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void noSuchEntityExceptionShouldBeHandled()
//            throws Exception {
//        final NoSuchEntityException givenException = new NoSuchEntityException("exception-message");
//
//        final ResponseEntity<?> actualResponseEntity = handler.handle(givenException);
//        assertSame(NOT_FOUND, actualResponseEntity.getStatusCode());
//
//        final String actualBody = objectMapper.writeValueAsString(actualResponseEntity.getBody());
//        final String expectedBody = """
//                {
//                   "httpStatus": "NOT_FOUND",
//                   "message": "exception-message",
//                   "dateTime": "2024-02-24 15-44-23"
//                }""";
//        assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void conversionFailedExceptionShouldBeHandled()
//            throws Exception {
//        final ConversionFailedException givenException = mock(ConversionFailedException.class);
//
//        final TypeDescriptor givenTypeDescriptor = valueOf(TestObject.class);
//        when(givenException.getTargetType()).thenReturn(givenTypeDescriptor);
//
//        final String givenMessage = "exception-message";
//        when(givenException.getMessage()).thenReturn(givenMessage);
//
//        final ResponseEntity<?> actualResponseEntity = handler.handle(givenException);
//        assertSame(NOT_ACCEPTABLE, actualResponseEntity.getStatusCode());
//
//        final String actualBody = objectMapper.writeValueAsString(actualResponseEntity.getBody());
//        final String expectedBody = """
//                {
//                   "httpStatus": "NOT_ACCEPTABLE",
//                   "message": "exception-message",
//                   "dateTime": "2024-02-24 16-45-22"
//                 }""";
//        assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void conversionFailedExceptionWithEnumTargetTypeShouldBeHandled()
//            throws Exception {
//        final ConversionFailedException givenException = mock(ConversionFailedException.class);
//
//        final TypeDescriptor givenTypeDescriptor = valueOf(TestEnum.class);
//        when(givenException.getTargetType()).thenReturn(givenTypeDescriptor);
//
//        final String givenValue = "exception-value";
//        when(givenException.getValue()).thenReturn(givenValue);
//
//        final ResponseEntity<?> actualResponseEntity = handler.handle(givenException);
//        assertSame(NOT_ACCEPTABLE, actualResponseEntity.getStatusCode());
//
//        final String actualBody = objectMapper.writeValueAsString(actualResponseEntity.getBody());
//        final String expectedBody = """
//                {
//                    "httpStatus": "NOT_ACCEPTABLE",
//                    "message": "exception-value should be replaced by one of: FIRST, SECOND, THIRD",
//                    "dateTime": "2024-02-24 16-48-52"
//                }""";
//        assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void missingServletRequestParameterExceptionShouldBeHandled()
//            throws Exception {
//        final MissingServletRequestParameterException givenException = new MissingServletRequestParameterException(
//                "parameter-name",
//                "parameter-type"
//        );
//
//        final ResponseEntity<?> actualResponseEntity = handler.handle(givenException);
//        assertSame(NOT_ACCEPTABLE, actualResponseEntity.getStatusCode());
//
//        final String actualBody = objectMapper.writeValueAsString(actualResponseEntity.getBody());
//        final String expectedBody = """
//                {
//                   "httpStatus": "NOT_ACCEPTABLE",
//                   "message": "Required request parameter 'parameter-name' for method parameter type parameter-type is not present",
//                   "dateTime": "2024-02-24 16-52-32"
//                }""";
//        assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    private static final class TestObject {
//
//    }
//
//    private enum TestEnum {
//        FIRST, SECOND, THIRD
//    }
//}
