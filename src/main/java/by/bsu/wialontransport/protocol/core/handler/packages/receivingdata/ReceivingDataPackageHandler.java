package by.bsu.wialontransport.protocol.core.handler.packages.receivingdata;

import by.bsu.wialontransport.configuration.property.ReceivedDataDefaultPropertyConfiguration;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Data.GpsCoordinate;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.kafka.producer.KafkaInboundDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.core.service.receivingdata.filter.DataFilter;
import by.bsu.wialontransport.protocol.core.service.receivingdata.fixer.DataFixer;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static lombok.AccessLevel.PRIVATE;

public abstract class ReceivingDataPackageHandler<PACKAGE extends Package, DATA_SOURCE> extends PackageHandler<PACKAGE> {
    private final ReceivedDataDefaultPropertyConfiguration receivedDataDefaultPropertyConfiguration;
    private final ContextAttributeManager contextAttributeManager;
    private final DataFilter dataFilter;
    private final KafkaInboundDataProducer kafkaInboundDataProducer;
    private final DataFixer dataFixer;

    public ReceivingDataPackageHandler(final Class<PACKAGE> handledPackageType,
                                       final ReceivedDataDefaultPropertyConfiguration receivedDataDefaultPropertyConfiguration,
                                       final ContextAttributeManager contextAttributeManager,
                                       final DataFilter dataFilter,
                                       final KafkaInboundDataProducer kafkaInboundDataProducer,
                                       final DataFixer dataFixer) {
        super(handledPackageType);
        this.receivedDataDefaultPropertyConfiguration = receivedDataDefaultPropertyConfiguration;
        this.contextAttributeManager = contextAttributeManager;
        this.dataFilter = dataFilter;
        this.kafkaInboundDataProducer = kafkaInboundDataProducer;
        this.dataFixer = dataFixer;
    }

    @Override
    protected final void handleConcretePackage(final PACKAGE requestPackage, final ChannelHandlerContext context) {
        final List<ReceivedData> receivedData = this.extractDataSources(requestPackage)
                .map(this::createReceivedData)
                .toList();

    }

    protected abstract Stream<DATA_SOURCE> extractDataSources(final PACKAGE requestPackage);

    protected abstract void accumulateComponents(final ReceivedDataBuilder builder, final DATA_SOURCE source);

    private ReceivedData createReceivedData(final DATA_SOURCE source) {
        final ReceivedDataBuilder builder = new ReceivedDataBuilder(this.receivedDataDefaultPropertyConfiguration);
        this.accumulateComponents(builder, source);
        return builder.build();
    }

    private Data createData(final ReceivedData data) {

    }

    protected static final class ReceivedDataBuilder {

        @Getter(PRIVATE)
        private LocalDateTime dateTime;

        @Getter(PRIVATE)
        private GpsCoordinate gpsCoordinate;

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

        public ReceivedDataBuilder(final ReceivedDataDefaultPropertyConfiguration defaultPropertyConfiguration) {
            this.course = defaultPropertyConfiguration.getCourse();
            this.altitude = defaultPropertyConfiguration.getAltitude();
            this.speed = defaultPropertyConfiguration.getSpeed();
            this.amountOfSatellites = defaultPropertyConfiguration.getAmountOfSatellites();
            this.reductionPrecision = defaultPropertyConfiguration.getReductionPrecision();
            this.inputs = defaultPropertyConfiguration.getInputs();
            this.outputs = defaultPropertyConfiguration.getOutputs();
            this.analogInputs = new double[]{};
            this.driverKeyCode = defaultPropertyConfiguration.getDriverKeyCode();
            this.parametersByNames = new HashMap<>();
        }

        public void dateTime(final LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public void gpsCoordinate(final GpsCoordinate gpsCoordinate) {
            this.gpsCoordinate = gpsCoordinate;
        }

        public void course(final int course) {
            this.course = course;
        }

        public void altitude(final int altitude) {
            this.altitude = altitude;
        }

        public void speed(final double speed) {
            this.speed = speed;
        }

        public void amountOfSatellites(final int amountOfSatellites) {
            this.amountOfSatellites = amountOfSatellites;
        }

        public void reductionPrecision(final double reductionPrecision) {
            this.reductionPrecision = reductionPrecision;
        }

        public void inputs(final int inputs) {
            this.inputs = inputs;
        }

        public void outputs(final int outputs) {
            this.outputs = outputs;
        }

        public void analogInputs(final double[] analogInputs) {
            this.analogInputs = analogInputs;
        }

        public void driverKeyCode(final String driverKeyCode) {
            this.driverKeyCode = driverKeyCode;
        }

        public void parameter(final Parameter parameter) {
            final String parameterName = parameter.getName();
            this.parametersByNames.put(parameterName, parameter);
        }

        public <DATA_SOURCE, COMPONENT> void accumulateComponent(
                final DATA_SOURCE source,
                final ComponentExtractor<DATA_SOURCE, COMPONENT> componentExtractor,
                final ComponentAccumulator<COMPONENT> componentAccumulator
        ) {
            final COMPONENT component = componentExtractor.extract(source);
            componentAccumulator.accumulate(this, component);
        }

        public <DATA_SOURCE> void accumulateDoubleComponent(
                final DATA_SOURCE source,
                final DoubleComponentExtractor<DATA_SOURCE> componentExtractor,
                final DoubleComponentAccumulator componentAccumulator
        ) {
            final double component = componentExtractor.extract(source);
            componentAccumulator.accumulate(this, component);
        }

        public <DATA_SOURCE> void accumulateIntComponent(final DATA_SOURCE source,
                                                         final IntComponentExtractor<DATA_SOURCE> componentExtractor,
                                                         final IntComponentAccumulator componentAccumulator) {
            final int component = componentExtractor.extract(source);
            componentAccumulator.accumulate(this, component);
        }

        ReceivedData build() {
            final LocalDateTime dateTime = this.getRequiredProperty(ReceivedDataBuilder::getDateTime);
            final GpsCoordinate gpsCoordinate = this.getRequiredProperty(ReceivedDataBuilder::getGpsCoordinate);
            return new ReceivedData(
                    dateTime,
                    gpsCoordinate,
                    this.course,
                    this.altitude,
                    this.speed,
                    this.amountOfSatellites,
                    this.reductionPrecision,
                    this.inputs,
                    this.outputs,
                    this.analogInputs,
                    this.driverKeyCode,
                    this.parametersByNames
            );
        }

        private <T> T getRequiredProperty(final Function<ReceivedDataBuilder, T> getter) {
            final T value = getter.apply(this);
            if (value == null) {
                throw new IllegalStateException("Required data property is not defined");
            }
            return value;
        }

        @FunctionalInterface
        public interface ComponentExtractor<DATA_SOURCE, COMPONENT> {
            COMPONENT extract(final DATA_SOURCE source);
        }

        @FunctionalInterface
        public interface DoubleComponentExtractor<DATA_SOURCE> {
            double extract(final DATA_SOURCE data);
        }

        @FunctionalInterface
        public interface IntComponentExtractor<DATA_SOURCE> {
            int extract(final DATA_SOURCE data);
        }

        @FunctionalInterface
        public interface ComponentAccumulator<T> {
            void accumulate(final ReceivedDataBuilder builder, final T component);
        }

        @FunctionalInterface
        public interface DoubleComponentAccumulator {
            void accumulate(final ReceivedDataBuilder builder, final double component);
        }

        @FunctionalInterface
        public interface IntComponentAccumulator {
            void accumulate(final ReceivedDataBuilder builder, final int component);
        }
    }

    @Value
    private static class ReceivedData {
        LocalDateTime dateTime;
        GpsCoordinate gpsCoordinate;
        int course;
        int altitude;
        double speed;
        int amountOfSatellites;
        double reductionPrecision;
        int inputs;
        int outputs;
        double[] analogInputs;
        String driverKeyCode;
        Map<String, Parameter> parametersByNames;
    }
}
