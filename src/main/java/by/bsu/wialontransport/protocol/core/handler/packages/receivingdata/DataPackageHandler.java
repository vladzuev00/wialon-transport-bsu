package by.bsu.wialontransport.protocol.core.handler.packages.receivingdata;

import by.bsu.wialontransport.configuration.property.DataDefaultPropertyConfiguration;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.ReceivedData;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.channel.ChannelHandlerContext;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.time.LocalDateTime.MIN;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public abstract class DataPackageHandler<PACKAGE extends Package, SOURCE> extends PackageHandler<PACKAGE> {
    private static final Comparator<ReceivedData> DATE_TIME_COMPARATOR = comparing(ReceivedData::getDateTime);

    private final DataDefaultPropertyConfiguration dataDefaultPropertyConfiguration;
    private final ContextAttributeManager contextAttributeManager;
    private final ReceivedDataValidator receivedDataValidator;
    private final KafkaInboundDataProducer kafkaInboundDataProducer;

    public DataPackageHandler(final Class<PACKAGE> handledPackageType,
                              final DataDefaultPropertyConfiguration dataDefaultPropertyConfiguration,
                              final ContextAttributeManager contextAttributeManager,
                              final ReceivedDataValidator receivedDataValidator,
                              final KafkaInboundDataProducer kafkaInboundDataProducer) {
        super(handledPackageType);
        this.dataDefaultPropertyConfiguration = dataDefaultPropertyConfiguration;
        this.contextAttributeManager = contextAttributeManager;
        this.receivedDataValidator = receivedDataValidator;
        this.kafkaInboundDataProducer = kafkaInboundDataProducer;
    }

    @Override
    protected final Package handleInternal(final PACKAGE request, final ChannelHandlerContext context) {
        final Tracker tracker = extractTracker(context);
        final LocalDateTime lastReceivingDateTime = findLastReceivingDateTime(context);
        getSources(request)
                .map(source -> createReceivedData(source, tracker))
                .filter(receivedDataValidator::isValid)
                .sorted(DATE_TIME_COMPARATOR)
                .dropWhile(receivedData -> receivedData.getDateTime().isBefore(lastReceivingDateTime))
                .map(this::mapToSentData)
                .reduce((first, second) -> second)
                .ifPresent(lastData -> contextAttributeManager.putLastData(context, lastData));
        return createResponse(request);
    }

    protected abstract Stream<SOURCE> getSources(final PACKAGE requestPackage);

    protected abstract LocalDateTime getDateTime(final SOURCE source);

    protected abstract Coordinate getCoordinate(final SOURCE source);

    protected abstract OptionalInt findCourse(final SOURCE source);

    protected abstract OptionalDouble findSpeed(final SOURCE source);

    protected abstract OptionalInt findAltitude(final SOURCE source);

    protected abstract OptionalInt findAmountOfSatellites(final SOURCE source);

    protected abstract OptionalDouble findHdop(final SOURCE source);

    protected abstract OptionalInt findInputs(final SOURCE source);

    protected abstract OptionalInt findOutputs(final SOURCE source);

    protected abstract Optional<double[]> findAnalogInputs(final SOURCE source);

    protected abstract Optional<String> findDriverKeyCode(final SOURCE source);

    protected abstract Stream<Parameter> getParameters(final SOURCE source);

    protected abstract Package createResponse(final PACKAGE request);

    private Tracker extractTracker(final ChannelHandlerContext context) {
        return contextAttributeManager.findTracker(context)
                .orElseThrow(
                        () -> new NoTrackerInContextException("There is no tracker in context")
                );
    }

    private LocalDateTime findLastReceivingDateTime(final ChannelHandlerContext context) {
        return contextAttributeManager.findLastData(context)
                .map(Data::getDateTime)
                .orElse(MIN);
    }

    private ReceivedData createReceivedData(final SOURCE source, final Tracker tracker) {
        return ReceivedData.builder()
                .dateTime(getDateTime(source))
                .coordinate(getCoordinate(source))
                .course(getCourse(source))
                .speed(getSpeed(source))
                .altitude(getAltitude(source))
                .amountOfSatellites(getAmountOfSatellites(source))
                .hdop(getHdop(source))
                .inputs(getInputs(source))
                .outputs(getOutputs(source))
                .analogInputs(getAnalogInputs(source))
                .driverKeyCode(getDriverKeyCode(source))
                .parametersByNames(getParametersByNames(source))
                .tracker(tracker)
                .build();
    }

    private int getCourse(final SOURCE source) {
        return findCourse(source).orElse(dataDefaultPropertyConfiguration.getCourse());
    }

    private double getSpeed(final SOURCE source) {
        return findSpeed(source).orElse(dataDefaultPropertyConfiguration.getSpeed());
    }

    private int getAltitude(final SOURCE source) {
        return findAltitude(source).orElse(dataDefaultPropertyConfiguration.getAltitude());
    }

    private int getAmountOfSatellites(final SOURCE source) {
        return findAmountOfSatellites(source).orElse(dataDefaultPropertyConfiguration.getAmountOfSatellites());
    }

    private double getHdop(final SOURCE source) {
        return findHdop(source).orElse(dataDefaultPropertyConfiguration.getHdop());
    }

    private int getInputs(final SOURCE source) {
        return findInputs(source).orElse(dataDefaultPropertyConfiguration.getInputs());
    }

    private int getOutputs(final SOURCE source) {
        return findOutputs(source).orElse(dataDefaultPropertyConfiguration.getOutputs());
    }

    private double[] getAnalogInputs(final SOURCE source) {
        return findAnalogInputs(source).orElseGet(() -> new double[]{});
    }

    private String getDriverKeyCode(final SOURCE source) {
        return findDriverKeyCode(source).orElse(dataDefaultPropertyConfiguration.getDriverKeyCode());
    }

    private Map<String, Parameter> getParametersByNames(final SOURCE source) {
        return getParameters(source)
                .collect(
                        toMap(
                                Parameter::getName,
                                identity()
                        )
                );
    }

    private Data mapToSentData(final ReceivedData receivedData) {
        final Data data = mapToData(receivedData);
        kafkaInboundDataProducer.send(data);
        return data;
    }

    private static Data mapToData(final ReceivedData receivedData) {
        return Data.builder()
                .dateTime(receivedData.getDateTime())
                .coordinate(receivedData.getCoordinate())
                .course(receivedData.getCourse())
                .speed(receivedData.getSpeed())
                .altitude(receivedData.getAltitude())
                .amountOfSatellites(receivedData.getAmountOfSatellites())
                .hdop(receivedData.getHdop())
                .inputs(receivedData.getInputs())
                .outputs(receivedData.getOutputs())
                .analogInputs(receivedData.getAnalogInputs())
                .driverKeyCode(receivedData.getDriverKeyCode())
                .parametersByNames(receivedData.getParametersByNames())
                .tracker(receivedData.getTracker())
                .build();
    }

    static final class NoTrackerInContextException extends RuntimeException {

        @SuppressWarnings("unused")
        public NoTrackerInContextException() {

        }

        public NoTrackerInContextException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public NoTrackerInContextException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public NoTrackerInContextException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
