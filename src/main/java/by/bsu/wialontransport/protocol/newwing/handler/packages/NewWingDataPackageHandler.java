package by.bsu.wialontransport.protocol.newwing.handler.packages;

import by.bsu.wialontransport.configuration.property.DataDefaultPropertyConfiguration;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.function.ToShortFunction;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.DataPackageHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.ReceivedDataValidator;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingDataPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.response.NewWingSuccessResponsePackage;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.NewWingCoordinateUtil.calculateLatitude;
import static by.bsu.wialontransport.util.NewWingCoordinateUtil.calculateLongitude;
import static by.bsu.wialontransport.util.NumberUtil.createDoubleByParts;

public final class NewWingDataPackageHandler extends DataPackageHandler<NewWingDataPackage, NewWingData> {
    private static final int YEAR_MARK_POINT = 2000;
    private static final double MILLI_VOLTS_IN_VOLT = 1000;

    public NewWingDataPackageHandler(final DataDefaultPropertyConfiguration dataDefaultPropertyConfiguration,
                                     final ContextAttributeManager contextAttributeManager,
                                     final ReceivedDataValidator dataValidator,
                                     final KafkaInboundDataProducer kafkaInboundDataProducer) {
        super(
                NewWingDataPackage.class,
                dataDefaultPropertyConfiguration,
                contextAttributeManager,
                dataValidator,
                kafkaInboundDataProducer
        );
    }

    @Override
    protected Stream<NewWingData> getSources(final NewWingDataPackage requestPackage) {
        return requestPackage.getData().stream();
    }

    @Override
    protected LocalDateTime getDateTime(final NewWingData data) {
        return LocalDateTime.of(
                YEAR_MARK_POINT + data.getYear(),
                data.getMonth(),
                data.getDay(),
                data.getHour(),
                data.getMinute(),
                data.getSecond()
        );
    }

    @Override
    protected Coordinate getCoordinate(final NewWingData data) {
        final double latitude = calculateLatitude(data.getLatitudeIntegerPart(), data.getLatitudeFractionalPart());
        final double longitude = calculateLongitude(data.getLongitudeIntegerPart(), data.getLongitudeFractionalPart());
        return new Coordinate(latitude, longitude);
    }

    @Override
    protected OptionalInt findCourse(final NewWingData data) {
        return OptionalInt.of(data.getCourse());
    }

    @Override
    protected OptionalDouble findSpeed(final NewWingData data) {
        final double speed = createDoubleByParts(data.getSpeedIntegerPart(), data.getSpeedFractionalPart());
        return OptionalDouble.of(speed);
    }

    @Override
    protected OptionalInt findAltitude(final NewWingData data) {
        return OptionalInt.empty();
    }

    @Override
    protected OptionalInt findAmountOfSatellites(final NewWingData data) {
        return OptionalInt.empty();
    }

    @Override
    protected OptionalDouble findHdop(final NewWingData data) {
        final double value = createDoubleByParts(
                data.getHdopIntegerPart(),
                data.getHdopFractionalPart()
        );
        return OptionalDouble.of(value);
    }

    @Override
    protected OptionalInt findInputs(final NewWingData data) {
        return OptionalInt.empty();
    }

    @Override
    protected OptionalInt findOutputs(final NewWingData data) {
        return OptionalInt.empty();
    }

    @Override
    protected Optional<double[]> findAnalogInputs(final NewWingData data) {
        return Optional.of(
                new double[]{
                        getFirstAnalogInput(data),
                        getSecondAnalogInput(data),
                        getThirdAnalogInput(data),
                        getFourthAnalogInput(data)
                }
        );
    }

    @Override
    protected Optional<String> findDriverKeyCode(final NewWingData data) {
        return Optional.empty();
    }

    @Override
    protected Stream<Parameter> getParameters(final NewWingData data) {
        return Stream.empty();
    }

    @Override
    protected Package createResponse(final NewWingDataPackage request) {
        return new NewWingSuccessResponsePackage();
    }

    private static double getFirstAnalogInput(final NewWingData data) {
        return getAnalogInput(data, NewWingData::getFirstAnalogInputLevel);
    }

    private static double getSecondAnalogInput(final NewWingData data) {
        return getAnalogInput(data, NewWingData::getSecondAnalogInputLevel);
    }

    private static double getThirdAnalogInput(final NewWingData data) {
        return getAnalogInput(data, NewWingData::getThirdAnalogInputLevel);
    }

    private static double getFourthAnalogInput(final NewWingData data) {
        return getAnalogInput(data, NewWingData::getFourthAnalogInputLevel);
    }

    private static double getAnalogInput(final NewWingData data, final ToShortFunction<NewWingData> getter) {
        return getter.apply(data) / MILLI_VOLTS_IN_VOLT;
    }
}
