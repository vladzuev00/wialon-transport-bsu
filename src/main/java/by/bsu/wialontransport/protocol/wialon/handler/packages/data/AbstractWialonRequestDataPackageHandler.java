package by.bsu.wialontransport.protocol.wialon.handler.packages.data;

import by.bsu.wialontransport.configuration.property.DataDefaultPropertyConfiguration;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.DataPackageHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.ReceivedDataValidator;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.wialon.model.WialonData;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractWialonRequestDataPackage;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

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
        return null;
    }

    @Override
    protected OptionalInt findCourse(WialonData wialonData) {
        return null;
    }

    @Override
    protected OptionalDouble findSpeed(WialonData wialonData) {
        return null;
    }

    @Override
    protected OptionalInt findAltitude(WialonData wialonData) {
        return null;
    }

    @Override
    protected OptionalInt findAmountOfSatellites(WialonData wialonData) {
        return null;
    }

    @Override
    protected OptionalDouble findHdop(WialonData wialonData) {
        return null;
    }

    @Override
    protected OptionalInt findInputs(WialonData wialonData) {
        return null;
    }

    @Override
    protected OptionalInt findOutputs(WialonData wialonData) {
        return null;
    }

    @Override
    protected Optional<double[]> findAnalogInputs(WialonData wialonData) {
        return Optional.empty();
    }

    @Override
    protected Optional<String> findDriverKeyCode(WialonData wialonData) {
        return Optional.empty();
    }

    @Override
    protected Stream<Parameter> getParameters(WialonData wialonData) {
        return null;
    }

    @Override
    protected Package createResponse(PACKAGE request) {
        return null;
    }


}
