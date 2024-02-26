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
import static java.lang.String.join;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public final class RestExceptionHandler {
    private static final String DELIMITER_ERROR_FIELD_NAME_AND_MESSAGE = " : ";
    private static final String UNKNOWN_ERROR_MESSAGE = "unknown error";
    private static final String MESSAGE_TEMPLATE_NOT_VALID_ENUM_PARAM = "%s should be replaced by one of: %s";
    private static final String DELIMITER_ENUM_PARAM_ALLOWABLE_VALUE = ", ";

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handle(final ConstraintViolationException exception) {
        return createResponse(exception, NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handle(final MethodArgumentNotValidException exception) {
        return createResponse(findMessage(exception.getFieldError()), NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handle(final CustomValidationException exception) {
        return createResponse(exception, NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handle(final NoSuchEntityException exception) {
        return createResponse(exception, NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handle(final ConversionFailedException exception) {
        return createResponse(findMessage(exception), NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handle(final MissingServletRequestParameterException exception) {
        return createResponse(exception, NOT_ACCEPTABLE);
    }

    private static ResponseEntity<RestErrorResponse> createResponse(final Exception exception, final HttpStatus status) {
        return createResponse(exception.getMessage(), status);
    }

    private static ResponseEntity<RestErrorResponse> createResponse(final String message, final HttpStatus status) {
        final RestErrorResponse errorResponse = new RestErrorResponse(status, message, now());
        return new ResponseEntity<>(errorResponse, status);
    }

    private static String findMessage(final FieldError error) {
        return error != null
                ? join(DELIMITER_ERROR_FIELD_NAME_AND_MESSAGE, error.getField(), error.getDefaultMessage())
                : UNKNOWN_ERROR_MESSAGE;
    }

    private static String findMessage(final ConversionFailedException exception) {
        return exception.getTargetType().getType().isEnum()
                ? findEnumParamConversionFailedMessage(exception)
                : exception.getMessage();
    }

    @SuppressWarnings("unchecked")
    private static String findEnumParamConversionFailedMessage(final ConversionFailedException exception) {
        final Class<? extends Enum<?>> type = (Class<? extends Enum<?>>) exception.getTargetType().getType();
        return format(
                MESSAGE_TEMPLATE_NOT_VALID_ENUM_PARAM,
                exception.getValue(),
                findSeparatedAllowableEnumValues(type)
        );
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
