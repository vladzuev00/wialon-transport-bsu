package by.vladzuev.locationreceiver.service.geocoding.aspect;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Aspect
@Component
public final class GeocodingLoggingAspect {

    @AfterReturning(pointcut = "geocoding()", returning = "optionalAddress")
    public void log(final JoinPoint joinPoint, final Optional<Address> optionalAddress) {
        optionalAddress.ifPresentOrElse(address -> logSuccess(joinPoint, address), () -> logFailure(joinPoint));
    }

    @Pointcut(
            "execution("
                    + "public java.util.Optional<by.vladzuev.locationreceiver.crud.dto.Address> "
                    + "by.vladzuev.locationreceiver.service.geocoding.geocoder.Geocoder.geocode("
                    + "by.vladzuev.locationreceiver.model.GpsCoordinate)"
                    + ")"
    )
    private void geocoding() {

    }

    private void logSuccess(final JoinPoint joinPoint, final Address address) {
        log.info(
                "Address in '{}' was received by '{}'. Address: {}",
                getCoordinate(joinPoint),
                getGeocoderName(joinPoint),
                address
        );
    }

    private void logFailure(final JoinPoint joinPoint) {
        log.info(
                "Address in '{}' wasn't received by '{}'",
                getCoordinate(joinPoint),
                getGeocoderName(joinPoint)
        );
    }

    private String getGeocoderName(final JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getSimpleName();
    }

    private GpsCoordinate getCoordinate(final JoinPoint joinPoint) {
        return (GpsCoordinate) joinPoint.getArgs()[0];
    }
}
