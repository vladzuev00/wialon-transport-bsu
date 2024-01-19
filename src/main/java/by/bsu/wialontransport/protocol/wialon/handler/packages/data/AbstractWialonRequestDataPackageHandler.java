package by.bsu.wialontransport.protocol.wialon.handler.packages.data;

import by.bsu.wialontransport.configuration.property.DataDefaultPropertyConfiguration;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.DataPackageHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.ReceivedDataValidator;
import by.bsu.wialontransport.protocol.wialon.model.WialonData;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractWialonRequestDataPackage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.OptionalUtil.ofNullableDouble;
import static by.bsu.wialontransport.util.OptionalUtil.ofNullableInt;
import static by.bsu.wialontransport.util.coordinate.WialonCoordinateUtil.toDouble;
import static java.util.Objects.requireNonNullElse;
import static java.util.Optional.ofNullable;

public abstract class AbstractWialonRequestDataPackageHandler<PACKAGE extends AbstractWialonRequestDataPackage>
        extends DataPackageHandler<PACKAGE, WialonData> {

    public AbstractWialonRequestDataPackageHandler(final Class<PACKAGE> handledPackageType,
                                                   final DataDefaultPropertyConfiguration dataDefaultPropertyConfiguration,
                                                   final ContextAttributeManager contextAttributeManager,
                                                   final ReceivedDataValidator receivedDataValidator,
                                                   final KafkaInboundDataProducer kafkaInboundDataProducer) {
        super(
                handledPackageType,
                dataDefaultPropertyConfiguration,
                contextAttributeManager,
                receivedDataValidator,
                kafkaInboundDataProducer
        );
    }

    @Override
    protected final Stream<WialonData> getSources(final PACKAGE request) {
        return request.getData().stream();
    }

    @Override
    protected final LocalDateTime getDateTime(final WialonData source) {
        return LocalDateTime.of(source.getDate(), source.getTime());
    }

    @Override
    protected final Coordinate getCoordinate(final WialonData source) {
        final double latitude = toDouble(source.getLatitude());
        final double longitude = toDouble(source.getLongitude());
        return new Coordinate(latitude, longitude);
    }

    @Override
    protected final OptionalInt findCourse(final WialonData source) {
        return ofNullableInt(source.getCourse());
    }

    @Override
    protected final OptionalDouble findSpeed(final WialonData source) {
        return ofNullableDouble(source.getSpeed());
    }

    @Override
    protected final OptionalInt findAltitude(final WialonData source) {
        return ofNullableInt(source.getAltitude());
    }

    @Override
    protected final OptionalInt findAmountOfSatellites(final WialonData source) {
        return ofNullableInt(source.getAmountOfSatellites());
    }

    @Override
    protected final OptionalDouble findHdop(final WialonData source) {
        return ofNullableDouble(source.getHdop());
    }

    @Override
    protected final OptionalInt findInputs(final WialonData source) {
        return ofNullableInt(source.getInputs());
    }

    @Override
    protected final OptionalInt findOutputs(final WialonData source) {
        return ofNullableInt(source.getOutputs());
    }

    @Override
    protected final Optional<double[]> findAnalogInputs(final WialonData source) {
        return ofNullable(source.getAnalogInputs());
    }

    @Override
    protected final Optional<String> findDriverKeyCode(final WialonData source) {
        return ofNullable(source.getDriverKeyCode());
    }

    @Override
    protected final Stream<Parameter> getParameters(final WialonData source) {
        return requireNonNullElse(source.getParameters(), Collections.<Parameter>emptySet()).stream();
    }
}
