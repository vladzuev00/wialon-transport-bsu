package by.vladzuev.locationreceiver.protocol.wialon.handler.location;

import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.kafka.producer.data.KafkaInboundLocationProducer;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.location.LocationPackageHandler;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.LocationValidator;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.property.LocationDefaultProperty;
import by.vladzuev.locationreceiver.protocol.wialon.model.location.request.WialonLocation;
import by.vladzuev.locationreceiver.protocol.wialon.model.location.request.WialonRequestLocationPackage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static by.vladzuev.locationreceiver.util.OptionalUtil.ofNullableDouble;
import static by.vladzuev.locationreceiver.util.OptionalUtil.ofNullableInt;
import static java.util.Optional.ofNullable;

public abstract class WialonRequestLocationPackageHandler<REQUEST extends WialonRequestLocationPackage>
        extends LocationPackageHandler<WialonLocation, REQUEST> {

    public WialonRequestLocationPackageHandler(final Class<REQUEST> requestType,
                                               final ContextAttributeManager contextAttributeManager,
                                               final LocationDefaultProperty locationDefaultProperty,
                                               final LocationValidator locationValidator,
                                               final KafkaInboundLocationProducer locationProducer) {
        super(requestType, contextAttributeManager, locationDefaultProperty, locationValidator, locationProducer);
    }

    @Override
    protected final Stream<WialonLocation> streamLocationSources(final REQUEST request) {
        return request.getLocations().stream();
    }

    @Override
    protected final Optional<LocalDate> findDate(final WialonLocation location) {
        return ofNullable(location.getDate());
    }

    @Override
    protected final Optional<LocalTime> findTime(final WialonLocation location) {
        return ofNullable(location.getTime());
    }

    @Override
    protected final OptionalDouble findLatitude(final WialonLocation location) {
        return ofNullableDouble(location.getLatitude());
    }

    @Override
    protected final OptionalDouble findLongitude(final WialonLocation location) {
        return ofNullableDouble(location.getLongitude());
    }

    @Override
    protected final OptionalInt findCourse(final WialonLocation location) {
        return ofNullableInt(location.getCourse());
    }

    @Override
    protected final OptionalDouble findSpeed(final WialonLocation location) {
        return ofNullableDouble(location.getSpeed());
    }

    @Override
    protected final OptionalInt findAltitude(final WialonLocation location) {
        return ofNullableInt(location.getAltitude());
    }

    @Override
    protected final OptionalInt findSatelliteCount(final WialonLocation location) {
        return ofNullableInt(location.getSatelliteCount());
    }

    @Override
    protected final OptionalDouble findHdop(final WialonLocation location) {
        return ofNullableDouble(location.getHdop());
    }

    @Override
    protected final OptionalInt findInputs(final WialonLocation location) {
        return ofNullableInt(location.getInputs());
    }

    @Override
    protected final OptionalInt findOutputs(final WialonLocation location) {
        return ofNullableInt(location.getOutputs());
    }

    @Override
    protected final double[] getAnalogInputs(final WialonLocation location) {
        return location.getAnalogInputs();
    }

    @Override
    protected final Optional<String> findDriverKeyCode(final WialonLocation location) {
        return ofNullable(location.getDriverKeyCode());
    }

    @Override
    protected final Stream<Parameter> streamParameters(final WialonLocation location) {
        return location.getParameters().stream();
    }

    @Override
    protected final void onSuccess() {

    }
}
