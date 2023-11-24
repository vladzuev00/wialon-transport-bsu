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
        final List<Data> data = this.extractDataSources(requestPackage)
                .map(this::createReceivedData)
                .map(this::createData)
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
            this.amountOfSatellites = defaultPropertyConfiguration.getAmountOfSatellites();
            this.reductionPrecision = defaultPropertyConfiguration.getReductionPrecision();
            this.inputs = defaultPropertyConfiguration.getInputs();
            this.outputs = defaultPropertyConfiguration.getOutputs();
            this.analogInputs = new double[]{};
            this.driverKeyCode = defaultPropertyConfiguration.getDriverKeyCode();
            this.parametersByNames = new HashMap<>();
        }

        public ReceivedDataBuilder dateTime(final LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public ReceivedDataBuilder gpsCoordinate(final GpsCoordinate gpsCoordinate) {
            this.gpsCoordinate = gpsCoordinate;
            return this;
        }

        public ReceivedDataBuilder course(final int course) {
            this.course = course;
            return this;
        }

        public ReceivedDataBuilder altitude(final int altitude) {
            this.altitude = altitude;
            return this;
        }

        public ReceivedDataBuilder amountOfSatellites(final int amountOfSatellites) {
            this.amountOfSatellites = amountOfSatellites;
            return this;
        }

        public ReceivedDataBuilder reductionPrecision(final double reductionPrecision) {
            this.reductionPrecision = reductionPrecision;
            return this;
        }

        public ReceivedDataBuilder inputs(final int inputs) {
            this.inputs = inputs;
            return this;
        }

        public ReceivedDataBuilder outputs(final int outputs) {
            this.outputs = outputs;
            return this;
        }

        public ReceivedDataBuilder analogInputs(final double[] analogInputs) {
            this.analogInputs = analogInputs;
            return this;
        }

        public ReceivedDataBuilder driverKeyCode(final String driverKeyCode) {
            this.driverKeyCode = driverKeyCode;
            return this;
        }

        public ReceivedDataBuilder parameter(final Parameter parameter) {
            final String parameterName = parameter.getName();
            this.parametersByNames.put(parameterName, parameter);
            return this;
        }

        public ReceivedData build() {
            final LocalDateTime dateTime = this.getRequiredProperty(ReceivedDataBuilder::getDateTime);
            final GpsCoordinate gpsCoordinate = this.getRequiredProperty(ReceivedDataBuilder::getGpsCoordinate);
            return new ReceivedData(
                    dateTime,
                    gpsCoordinate,
                    this.course,
                    this.altitude,
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
    }

    @Value
    private static class ReceivedData {
        LocalDateTime dateTime;
        GpsCoordinate gpsCoordinate;
        int course;
        int altitude;
        int amountOfSatellites;
        double reductionPrecision;
        int inputs;
        int outputs;
        double[] analogInputs;
        String driverKeyCode;
        Map<String, Parameter> parametersByNames;
    }
}
