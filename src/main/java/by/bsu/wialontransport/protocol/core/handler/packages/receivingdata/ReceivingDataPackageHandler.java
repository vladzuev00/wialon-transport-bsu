package by.bsu.wialontransport.protocol.core.handler.packages.receivingdata;

import by.bsu.wialontransport.configuration.property.DataDefaultPropertyConfiguration;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.ReceivedData;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.core.service.receivingdata.filter.ReceivedDataValidator;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.time.LocalDateTime.MIN;
import static java.util.Comparator.comparing;
import static lombok.AccessLevel.PRIVATE;

public abstract class ReceivingDataPackageHandler<PACKAGE extends Package, SOURCE> extends PackageHandler<PACKAGE> {
    private static final Comparator<ReceivedData> DATE_TIME_COMPARATOR = comparing(ReceivedData::getDateTime);

    private final DataDefaultPropertyConfiguration dataDefaultPropertyConfiguration;
    private final ContextAttributeManager contextAttributeManager;
    private final ReceivedDataValidator receivedDataValidator;
    private final KafkaInboundDataProducer kafkaInboundDataProducer;

    public ReceivingDataPackageHandler(final Class<PACKAGE> handledPackageType,
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
    protected final void handleConcretePackage(final PACKAGE requestPackage, final ChannelHandlerContext context) {
        final Tracker tracker = extractTracker(context);
        final LocalDateTime lastReceivingDateTime = findLastDataDateTime(context);
        extractSources(requestPackage)
                .map(source -> createReceivedData(source, tracker))
                .filter(receivedDataValidator::isValid)
                .sorted(DATE_TIME_COMPARATOR)
                .dropWhile(receivedData -> receivedData.getDateTime().isBefore(lastReceivingDateTime))
                .map(ReceivingDataPackageHandler::mapToData)
                .forEach(kafkaInboundDataProducer::send);
    }

    protected abstract Stream<SOURCE> extractSources(final PACKAGE requestPackage);

    protected abstract void accumulateComponents(final ReceivedDataBuilder builder, final SOURCE source);

    private Tracker extractTracker(final ChannelHandlerContext context) {
        return contextAttributeManager.findTracker(context)
                .orElseThrow(
                        () -> new ReceivingDataException("There is no tracker in context")
                );
    }

    private ReceivedData createReceivedData(final SOURCE source, final Tracker tracker) {
        final ReceivedDataBuilder builder = new ReceivedDataBuilder(dataDefaultPropertyConfiguration);
        accumulateComponents(builder, source);
        return builder.build(tracker);
    }

    private LocalDateTime findLastDataDateTime(final ChannelHandlerContext context) {
        return contextAttributeManager.findLastData(context)
                .map(Data::getDateTime)
                .orElse(MIN);
    }

    private static Data mapToData(final ReceivedData receivedData) {
        return Data.builder()
                .dateTime(receivedData.getDateTime())
                .coordinate(receivedData.getCoordinate())
                .
                .build();
    }

    @Setter
    protected static final class ReceivedDataBuilder {
        private static final String PROPERTY_NAME_DATE_TIME = "date time";
        private static final String PROPERTY_NAME_COORDINATE = "coordinate";

        @Getter(PRIVATE)
        private LocalDateTime dateTime;

        @Getter(PRIVATE)
        private Coordinate coordinate;

        private int course;
        private int altitude;
        private double speed;
        private int amountOfSatellites;
        private double reductionPrecision;
        private int inputs;
        private int outputs;
        private double[] analogInputs;
        private String driverKeyCode;
        private final Map<String, Parameter> parametersByNames;

        public ReceivedDataBuilder(final DataDefaultPropertyConfiguration defaultPropertyConfiguration) {
            course = defaultPropertyConfiguration.getCourse();
            altitude = defaultPropertyConfiguration.getAltitude();
            speed = defaultPropertyConfiguration.getSpeed();
            amountOfSatellites = defaultPropertyConfiguration.getAmountOfSatellites();
            reductionPrecision = defaultPropertyConfiguration.getReductionPrecision();
            inputs = defaultPropertyConfiguration.getInputs();
            outputs = defaultPropertyConfiguration.getOutputs();
            analogInputs = new double[]{};
            driverKeyCode = defaultPropertyConfiguration.getDriverKeyCode();
            parametersByNames = new HashMap<>();
        }

        public void addParameter(final Parameter parameter) {
            parametersByNames.put(parameter.getName(), parameter);
        }

        ReceivedData build(final Tracker tracker) {
            return new ReceivedData(
                    getRequiredProperty(ReceivedDataBuilder::getDateTime, PROPERTY_NAME_DATE_TIME),
                    getRequiredProperty(ReceivedDataBuilder::getCoordinate, PROPERTY_NAME_COORDINATE),
                    course,
                    speed,
                    altitude,
                    amountOfSatellites,
                    reductionPrecision,
                    inputs,
                    outputs,
                    analogInputs,
                    driverKeyCode,
                    parametersByNames,
                    tracker
            );
        }

        private <T> T getRequiredProperty(final Function<ReceivedDataBuilder, T> getter, final String propertyName) {
            final T value = getter.apply(this);
            if (value == null) {
                throw new IllegalStateException(
                        "Required property '%s' is not defined".formatted(propertyName)
                );
            }
            return value;
        }
    }

    private static final class ReceivingDataException extends RuntimeException {

        @SuppressWarnings("unused")
        public ReceivingDataException() {

        }

        public ReceivingDataException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public ReceivingDataException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ReceivingDataException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
