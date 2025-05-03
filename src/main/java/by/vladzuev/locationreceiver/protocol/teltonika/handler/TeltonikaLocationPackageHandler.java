package by.vladzuev.locationreceiver.protocol.teltonika.handler;

import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.kafka.producer.data.KafkaInboundLocationProducer;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.location.LocationPackageHandler;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.LocationValidator;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.property.LocationDefaultProperty;
import by.vladzuev.locationreceiver.protocol.teltonika.holder.TeltonikaLoginSuccessHolder;
import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaLocation;
import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaRequestLocationPackage;
import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaResponseLocationPackage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

@Component
public final class TeltonikaLocationPackageHandler extends LocationPackageHandler<TeltonikaLocation, TeltonikaRequestLocationPackage> {
    private final TeltonikaLoginSuccessHolder loginSuccessHolder;

    public TeltonikaLocationPackageHandler(final ContextAttributeManager contextAttributeManager,
                                           final LocationDefaultProperty locationDefaultProperty,
                                           final LocationValidator locationValidator,
                                           final KafkaInboundLocationProducer locationProducer,
                                           final TeltonikaLoginSuccessHolder loginSuccessHolder) {
        super(
                TeltonikaRequestLocationPackage.class,
                contextAttributeManager,
                locationDefaultProperty,
                locationValidator,
                locationProducer
        );
        this.loginSuccessHolder = loginSuccessHolder;
    }

    @Override
    protected Stream<TeltonikaLocation> streamLocationSources(final TeltonikaRequestLocationPackage request) {
        return request.getLocations().stream();
    }

    @Override
    protected Optional<LocalDate> findDate(final TeltonikaLocation location) {
        return Optional.of(location.getDateTime().toLocalDate());
    }

    @Override
    protected Optional<LocalTime> findTime(final TeltonikaLocation location) {
        return Optional.of(location.getDateTime().toLocalTime());
    }

    @Override
    protected OptionalDouble findLatitude(final TeltonikaLocation location) {
        return OptionalDouble.of(location.getLatitude());
    }

    @Override
    protected OptionalDouble findLongitude(final TeltonikaLocation location) {
        return OptionalDouble.of(location.getLongitude());
    }

    @Override
    protected OptionalInt findCourse(final TeltonikaLocation location) {
        return OptionalInt.of(location.getAngle());
    }

    @Override
    protected OptionalDouble findSpeed(final TeltonikaLocation location) {
        return OptionalDouble.of(location.getSpeed());
    }

    @Override
    protected OptionalInt findAltitude(final TeltonikaLocation location) {
        return OptionalInt.of(location.getAltitude());
    }

    @Override
    protected OptionalInt findSatelliteCount(final TeltonikaLocation location) {
        return OptionalInt.of(location.getSatelliteCount());
    }

    @Override
    protected OptionalDouble findHdop(final TeltonikaLocation location) {
        return OptionalDouble.empty();
    }

    @Override
    protected OptionalInt findInputs(final TeltonikaLocation location) {
        return OptionalInt.empty();
    }

    @Override
    protected OptionalInt findOutputs(final TeltonikaLocation location) {
        return OptionalInt.empty();
    }

    @Override
    protected double[] getAnalogInputs(final TeltonikaLocation location) {
        return new double[]{};
    }

    @Override
    protected Optional<String> findDriverKeyCode(final TeltonikaLocation location) {
        return Optional.empty();
    }

    @Override
    protected Stream<Parameter> streamParameters(final TeltonikaLocation location) {
        return Stream.empty();
    }

    @Override
    protected void onSuccess() {
        loginSuccessHolder.setSuccess(false);
    }

    @Override
    protected TeltonikaResponseLocationPackage createSuccessResponse(final TeltonikaRequestLocationPackage request) {
        return new TeltonikaResponseLocationPackage(request.getLocations().size());
    }
}
