package by.bsu.wialontransport.service.geocoding.aspect;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.service.geocoding.component.GeocodingChainComponent;
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
public final class GeocodingChainComponentAspect {
    private static final String TEMPLATE_MESSAGE_ABOUT_SUCCESSFUL_RECEIVING
            = "Address '%s' was successfully received by '%s'";
    private static final String TEMPLATE_MESSAGE_ABOUT_FAILURE_RECEIVING
            = "Address wasn't received by '%s'";

    @AfterReturning(
            pointcut = "receiveMethod()",
            returning = "optionalAddress"
    )
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public void logReceivingResult(final JoinPoint joinPoint, final Optional<Address> optionalAddress) {
        final GeocodingChainComponent component = (GeocodingChainComponent) joinPoint.getTarget();
        final String message = optionalAddress
                .map(address -> format(TEMPLATE_MESSAGE_ABOUT_SUCCESSFUL_RECEIVING, address, component.findName()))
                .orElse(format(TEMPLATE_MESSAGE_ABOUT_FAILURE_RECEIVING, component.findName()));
        log.info(message);
    }

    @Pointcut("execution("
            + "public java.util.Optional<by.bsu.wialontransport.crud.dto.Address> "
            + "by.bsu.wialontransport.service.geocoding.component.GeocodingChainComponent.receive(..)"
            + ")")
    private void receiveMethod() {

    }
}
