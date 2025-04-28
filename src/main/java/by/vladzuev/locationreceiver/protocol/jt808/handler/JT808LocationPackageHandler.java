package by.vladzuev.locationreceiver.protocol.jt808.handler;

import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.kafka.producer.data.KafkaInboundLocationProducer;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.location.LocationPackageHandler;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.LocationValidator;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.property.LocationDefaultProperty;
import by.vladzuev.locationreceiver.protocol.jt808.model.JT808Location;
import by.vladzuev.locationreceiver.protocol.jt808.model.JT808LocationPackage;
import by.vladzuev.locationreceiver.protocol.jt808.model.JT808ResponsePackage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

@Component
public final class JT808LocationPackageHandler extends LocationPackageHandler<JT808Location, JT808LocationPackage> {

    public JT808LocationPackageHandler(final ContextAttributeManager contextAttributeManager,
                                       final LocationDefaultProperty locationDefaultProperty,
                                       final LocationValidator locationValidator,
                                       final KafkaInboundLocationProducer locationProducer) {
        super(
                JT808LocationPackage.class,
                contextAttributeManager,
                locationDefaultProperty,
                locationValidator,
                locationProducer
        );
    }

    @Override
    protected Stream<JT808Location> streamLocationSources(final JT808LocationPackage request) {
        return request.getLocations().stream();
    }

    @Override
    protected Optional<LocalDate> findDate(final JT808Location location) {
        return Optional.of(location.getDateTime().toLocalDate());
    }

    @Override
    protected Optional<LocalTime> findTime(final JT808Location location) {
        return Optional.of(location.getDateTime().toLocalTime());
    }

    @Override
    protected OptionalDouble findLatitude(final JT808Location location) {
        return OptionalDouble.of(location.getLatitude());
    }

    @Override
    protected OptionalDouble findLongitude(final JT808Location location) {
        return OptionalDouble.of(location.getLongitude());
    }

    @Override
    protected OptionalInt findCourse(final JT808Location location) {
        return OptionalInt.of(location.getCourse());
    }

    @Override
    protected OptionalDouble findSpeed(final JT808Location location) {
        return OptionalDouble.of(location.getSpeed());
    }

    @Override
    protected OptionalInt findAltitude(final JT808Location location) {
        return OptionalInt.of(location.getAltitude());
    }

    @Override
    protected OptionalInt findSatelliteCount(final JT808Location location) {
        return OptionalInt.empty();
    }

    @Override
    protected OptionalDouble findHdop(final JT808Location location) {
        return OptionalDouble.empty();
    }

    @Override
    protected OptionalInt findInputs(final JT808Location location) {
        return OptionalInt.empty();
    }

    @Override
    protected OptionalInt findOutputs(final JT808Location location) {
        return OptionalInt.empty();
    }

    @Override
    protected double[] getAnalogInputs(final JT808Location location) {
        return new double[0];
    }

    @Override
    protected Optional<String> findDriverKeyCode(final JT808Location location) {
        return Optional.empty();
    }

    @Override
    protected Stream<Parameter> streamParameters(final JT808Location location) {
        return Stream.empty();
    }

    @Override
    protected JT808ResponsePackage createResponse(final JT808LocationPackage request) {
        throw new UnsupportedOperationException();
    }
}
