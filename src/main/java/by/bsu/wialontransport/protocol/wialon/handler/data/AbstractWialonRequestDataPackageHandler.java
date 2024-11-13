//package by.bsu.wialontransport.protocol.wialon.handler.packages.data;
//
//import by.bsu.wialontransport.config.property.DataDefaultPropertyConfig;
//import by.bsu.wialontransport.crud.dto.Parameter;
//import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
//import by.bsu.wialontransport.model.Coordinate;
//import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
//import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.DataPackageHandler;
//import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.ReceivedDataValidator;
//import by.bsu.wialontransport.protocol.core.model.packages.Package;
//import by.bsu.wialontransport.protocol.wialon.model.WialonData;
//import by.bsu.wialontransport.protocol.wialon.model.packages.data.request.AbstractWialonRequestDataPackage;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static by.bsu.wialontransport.util.OptionalUtil.ofNullableDouble;
//import static by.bsu.wialontransport.util.OptionalUtil.ofNullableInt;
//import static by.bsu.wialontransport.util.coordinate.WialonCoordinateUtil.toDouble;
//import static java.util.Optional.ofNullable;
//
//public abstract class AbstractWialonRequestDataPackageHandler<PACKAGE extends AbstractWialonRequestDataPackage>
//        extends DataPackageHandler<PACKAGE, WialonData> {
//
//    public AbstractWialonRequestDataPackageHandler(final Class<PACKAGE> handledPackageType,
//                                                   final DataDefaultPropertyConfig dataDefaultPropertyConfig,
//                                                   final ContextAttributeManager contextAttributeManager,
//                                                   final ReceivedDataValidator receivedDataValidator,
//                                                   final KafkaInboundDataProducer kafkaInboundDataProducer) {
//        super(
//                handledPackageType,
//                dataDefaultPropertyConfig,
//                contextAttributeManager,
//                receivedDataValidator,
//                kafkaInboundDataProducer
//        );
//    }
//
//    @Override
//    protected final List<WialonData> getSources(final PACKAGE request) {
//        return request.getData();
//    }
//
//    @Override
//    protected final LocalDateTime getDateTime(final WialonData source) {
//        return LocalDateTime.of(source.getDate(), source.getTime());
//    }
//
//    @Override
//    protected final Coordinate getCoordinate(final WialonData source) {
//        final double latitude = toDouble(source.getLatitude());
//        final double longitude = toDouble(source.getLongitude());
//        return new Coordinate(latitude, longitude);
//    }
//
//    @Override
//    protected final OptionalInt findCourse(final WialonData source) {
//        return ofNullableInt(source.getCourse());
//    }
//
//    @Override
//    protected final OptionalDouble findSpeed(final WialonData source) {
//        return ofNullableDouble(source.getSpeed());
//    }
//
//    @Override
//    protected final OptionalInt findAltitude(final WialonData source) {
//        return ofNullableInt(source.getAltitude());
//    }
//
//    @Override
//    protected final OptionalInt findAmountOfSatellites(final WialonData source) {
//        return ofNullableInt(source.getAmountOfSatellites());
//    }
//
//    @Override
//    protected final OptionalDouble findHdop(final WialonData source) {
//        return ofNullableDouble(source.getHdop());
//    }
//
//    @Override
//    protected final OptionalInt findInputs(final WialonData source) {
//        return ofNullableInt(source.getInputs());
//    }
//
//    @Override
//    protected final OptionalInt findOutputs(final WialonData source) {
//        return ofNullableInt(source.getOutputs());
//    }
//
//    @Override
//    protected final Optional<double[]> findAnalogInputs(final WialonData source) {
//        return ofNullable(source.getAnalogInputs());
//    }
//
//    @Override
//    protected final Optional<String> findDriverKeyCode(final WialonData source) {
//        return ofNullable(source.getDriverKeyCode());
//    }
//
//    @Override
//    protected final Optional<Set<Parameter>> findParameters(final WialonData source) {
//        return ofNullable(source.getParameters());
//    }
//
//    @Override
//    protected final Package createResponse(final PACKAGE request) {
//        final int receivedDataCount = request.getData().size();
//        return createResponse(receivedDataCount);
//    }
//
//    protected abstract Package createResponse(final int receivedDataCount);
//}
