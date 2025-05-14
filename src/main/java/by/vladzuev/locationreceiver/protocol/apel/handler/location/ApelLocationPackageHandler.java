package by.vladzuev.locationreceiver.protocol.apel.handler.location;

import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.kafka.producer.data.KafkaInboundLocationProducer;
import by.vladzuev.locationreceiver.protocol.apel.model.location.ApelLocation;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.location.LocationPackageHandler;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.LocationValidator;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.property.LocationDefaultProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;
import static java.util.Objects.nonNull;

public abstract class ApelLocationPackageHandler<REQUEST> extends LocationPackageHandler<ApelLocation, REQUEST> {

    public ApelLocationPackageHandler(final Class<REQUEST> requestType,
                                      final ContextAttributeManager contextAttributeManager,
                                      final LocationDefaultProperty locationDefaultProperty,
                                      final LocationValidator locationValidator,
                                      final KafkaInboundLocationProducer locationProducer) {
        super(requestType, contextAttributeManager, locationDefaultProperty, locationValidator, locationProducer);
    }

    @Override
    protected final Stream<ApelLocation> streamLocationSources(final REQUEST request) {
        return Stream.of(getLocation(request));
    }

    @Override
    protected final Optional<LocalDate> findDate(final ApelLocation location) {
        return Optional.of(getDateTime(location)).map(LocalDateTime::toLocalDate);
    }

    @Override
    protected final Optional<LocalTime> findTime(final ApelLocation location) {
        return Optional.of(getDateTime(location)).map(LocalDateTime::toLocalTime);
    }

    @Override
    protected final OptionalDouble findLatitude(final ApelLocation location) {
        return OptionalDouble.of(findGpsCoordinate(location.getLatitude()));
    }

    @Override
    protected final OptionalDouble findLongitude(final ApelLocation location) {
        return OptionalDouble.of(findGpsCoordinate(location.getLongitude()));
    }

    @Override
    protected final OptionalInt findCourse(final ApelLocation location) {
        return OptionalInt.of(location.getCourse());
    }

    @Override
    protected final OptionalDouble findSpeed(final ApelLocation location) {
        return OptionalDouble.of(location.getSpeed());
    }

    @Override
    protected final OptionalInt findAltitude(final ApelLocation location) {
        return OptionalInt.of(location.getAltitude());
    }

    @Override
    protected final OptionalInt findSatelliteCount(final ApelLocation location) {
        return nonNull(location.getSatelliteCount())
                ? OptionalInt.of(location.getSatelliteCount())
                : OptionalInt.empty();
    }

    @Override
    protected final OptionalDouble findHdop(final ApelLocation location) {
        return nonNull(location.getHdop())
                ? OptionalDouble.of(location.getHdop())
                : OptionalDouble.empty();
    }

    @Override
    protected final OptionalInt findInputs(final ApelLocation location) {
        return OptionalInt.empty();
    }

    @Override
    protected final OptionalInt findOutputs(final ApelLocation location) {
        return OptionalInt.empty();
    }

    @Override
    protected final double[] getAnalogInputs(final ApelLocation location) {
        return location.getAnalogInputs();
    }

    @Override
    protected final Optional<String> findDriverKeyCode(final ApelLocation location) {
        return Optional.empty();
    }

    @Override
    protected final Stream<Parameter> streamParameters(final ApelLocation location) {
        return Stream.empty();
    }

    @Override
    protected final void onSuccess() {

    }

    protected abstract ApelLocation getLocation(final REQUEST request);

    private LocalDateTime getDateTime(final ApelLocation location) {
        return ofEpochSecond(location.getEpochSeconds(), 0, UTC);
    }

    private double findGpsCoordinate(final int units) {
        return units / 180. * 0x7FFFFFFF;
    }
}
