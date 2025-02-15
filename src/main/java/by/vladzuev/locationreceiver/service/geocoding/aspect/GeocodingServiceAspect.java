package by.vladzuev.locationreceiver.service.geocoding.aspect;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.service.geocoding.service.GeocodingService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Aspect
@Component
public final class GeocodingServiceAspect {
    private static final String TEMPLATE_MESSAGE_SUCCESS_RECEIVING = "Address '%s' was successfully received by '%s'";
    private static final String TEMPLATE_MESSAGE_FAILED_RECEIVING = "Address wasn't received by '%s'";

    @AfterReturning(
            pointcut = "receiveMethod()",
            returning = "optionalAddress"
    )
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public void logReceivingResult(final JoinPoint joinPoint, final Optional<Address> optionalAddress) {
        final String serviceName = findServiceName(joinPoint);
        final String message = optionalAddress
                .map(address -> createSuccessReceivingMessage(address, serviceName))
                .orElseGet(() -> createFailedReceivingMessage(serviceName));
        log.info(message);
    }

    @Pointcut(
            "execution("
                    + "public java.util.Optional<by.vladzuev.locationreceiver.crud.dto.Address> "
                    + "by.vladzuev.locationreceiver.service.geocoding.service.GeocodingService.receive("
                    + "by.vladzuev.locationreceiver.model.GpsCoordinate))"
    )
    private void receiveMethod() {

    }

    private static String findServiceName(final JoinPoint joinPoint) {
        final GeocodingService service = (GeocodingService) joinPoint.getTarget();
        return service.findName();
    }

    private static String createSuccessReceivingMessage(final Address address, final String serviceName) {
        return format(TEMPLATE_MESSAGE_SUCCESS_RECEIVING, address, serviceName);
    }

    private static String createFailedReceivingMessage(final String serviceName) {
        return format(TEMPLATE_MESSAGE_FAILED_RECEIVING, serviceName);
    }
}
