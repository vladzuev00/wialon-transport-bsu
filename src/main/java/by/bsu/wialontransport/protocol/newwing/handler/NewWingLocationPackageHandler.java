package by.bsu.wialontransport.protocol.newwing.handler;

import by.bsu.wialontransport.config.property.LocationDefaultProperty;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundLocationProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.location.LocationPackageHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.location.validator.LocationValidator;
import by.bsu.wialontransport.protocol.newwing.model.NewWingLocation;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingLocationPackage;
import by.bsu.wialontransport.protocol.newwing.model.response.NewWingSuccessResponsePackage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

@Component
public final class NewWingLocationPackageHandler extends LocationPackageHandler<NewWingLocation, NewWingLocationPackage> {

    public NewWingLocationPackageHandler(final ContextAttributeManager contextAttributeManager,
                                         final LocationDefaultProperty locationDefaultProperty,
                                         final LocationValidator locationValidator,
                                         final KafkaInboundLocationProducer locationProducer) {
        super(
                NewWingLocationPackage.class,
                contextAttributeManager,
                locationDefaultProperty,
                locationValidator,
                locationProducer
        );
    }

    @Override
    protected List<NewWingLocation> getLocationSources(final NewWingLocationPackage request) {
        return request.getLocations();
    }

    @Override
    protected Optional<LocalDate> findDate(final NewWingLocation location) {
        return Optional.of(location.getDate());
    }

    @Override
    protected Optional<LocalTime> findTime(final NewWingLocation location) {
        return Optional.of(location.getTime());
    }

    @Override
    protected OptionalDouble findLatitude(final NewWingLocation location) {
        return OptionalDouble.of(location.getLatitude());
    }

    @Override
    protected OptionalDouble findLongitude(final NewWingLocation location) {
        return OptionalDouble.of(location.getLongitude());
    }

    @Override
    protected OptionalInt findCourse(final NewWingLocation location) {
        return OptionalInt.of(location.getCourse());
    }

    @Override
    protected OptionalDouble findSpeed(final NewWingLocation location) {
        return OptionalDouble.of(location.getSpeed());
    }

    @Override
    protected OptionalInt findAltitude(final NewWingLocation location) {
        return OptionalInt.empty();
    }

    @Override
    protected OptionalInt findSatelliteCount(final NewWingLocation location) {
        return OptionalInt.empty();
    }

    @Override
    protected OptionalDouble findHdop(final NewWingLocation location) {
        return OptionalDouble.of(location.getHdop());
    }

    @Override
    protected OptionalInt findInputs(final NewWingLocation location) {
        return OptionalInt.empty();
    }

    @Override
    protected OptionalInt findOutputs(final NewWingLocation location) {
        return OptionalInt.empty();
    }

    @Override
    protected double[] getAnalogInputs(final NewWingLocation location) {
        return location.getAnalogInputs();
    }

    @Override
    protected Optional<String> findDriverKeyCode(final NewWingLocation location) {
        return Optional.empty();
    }

    @Override
    protected Stream<Parameter> getParameters(final NewWingLocation location) {
        return Stream.empty();
    }

    @Override
    protected NewWingSuccessResponsePackage createResponse(final int locationCount) {
        return new NewWingSuccessResponsePackage();
    }
}