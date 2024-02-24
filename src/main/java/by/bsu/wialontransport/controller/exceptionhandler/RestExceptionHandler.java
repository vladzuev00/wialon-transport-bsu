package by.bsu.wialontransport.controller.exceptionhandler;

import by.bsu.wialontransport.controller.exception.CustomValidationException;
import by.bsu.wialontransport.controller.exception.NoSuchEntityException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public final class RestExceptionHandler {
    private static final String DELIMITER_ERROR_FIELD_NAME_AND_MESSAGE = " : ";
    private static final String UNKNOWN_ERROR_MESSAGE = "unknown error";
    private static final String MESSAGE_TEMPLATE_NOT_VALID_ENUM_PARAM = "'%s' should be replaced by one of: %s.";
    private static final String DELIMITER_ENUM_PARAM_ALLOWABLE_VALUE = ", ";

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(final ConstraintViolationException exception) {
        return handleValidationException(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(final MethodArgumentNotValidException exception) {
        return handleValidationException(findMessage(exception.getFieldError()));
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(final CustomValidationException exception) {
        return handleValidationException(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(final NoSuchEntityException exception) {
        return handleNotFoundException(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(final ConversionFailedException exception) {
        return handleValidationException(findMessage(exception));
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(final MissingServletRequestParameterException exception) {
        return handleValidationException(exception.getMessage());
    }

    private static ResponseEntity<RestErrorResponse> handleNotFoundException(final String message) {
        return handleException(NOT_FOUND, message);
    }

    private static ResponseEntity<RestErrorResponse> handleValidationException(final String message) {
        return handleException(NOT_ACCEPTABLE, message);
    }

    private static ResponseEntity<RestErrorResponse> handleException(final HttpStatus httpStatus,
                                                                     final String message) {
        final RestErrorResponse restErrorResponse = new RestErrorResponse(httpStatus, message, now());
        return new ResponseEntity<>(restErrorResponse, httpStatus);
    }

    private static String findMessage(final FieldError fieldError) {
        return fieldError != null
                ? fieldError.getField() + DELIMITER_ERROR_FIELD_NAME_AND_MESSAGE + fieldError.getDefaultMessage()
                : UNKNOWN_ERROR_MESSAGE;
    }

    @SuppressWarnings("unchecked")
    private static String findMessage(final ConversionFailedException exception) {
        final Class<?> targetType = exception.getTargetType().getType();
        return targetType.isEnum()
                ? findEnumParamConversionFailedMessage(exception.getValue(), (Class<? extends Enum<?>>) targetType)
                : exception.getMessage();
    }

    private static String findEnumParamConversionFailedMessage(final Object receivedValue,
                                                               final Class<? extends Enum<?>> type) {
        return format(MESSAGE_TEMPLATE_NOT_VALID_ENUM_PARAM, receivedValue, findSeparatedAllowableEnumValues(type));
    }

    private static String findSeparatedAllowableEnumValues(final Class<? extends Enum<?>> type) {
        return stream(type.getEnumConstants())
                .map(Enum::name)
                .collect(joining(DELIMITER_ENUM_PARAM_ALLOWABLE_VALUE));
    }

    @Value
    private static class RestErrorResponse {
        HttpStatus httpStatus;
        String message;

        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH-mm-ss")
        LocalDateTime dateTime;
    }
}
