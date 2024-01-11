package by.bsu.wialontransport.protocol.newwing.handler.packages;

import by.bsu.wialontransport.configuration.property.DataDefaultPropertyConfiguration;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.function.BiIntToDoubleFunction;
import by.bsu.wialontransport.function.ToShortFunction;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.CoordinateRequest;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.DataPackageHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.ReceivedDataValidator;
import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingDataPackage;
import by.bsu.wialontransport.util.NewWingCoordinateUtil;
import by.bsu.wialontransport.util.NumberUtil;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static by.bsu.wialontransport.crud.dto.Parameter.createDoubleParameter;
import static by.bsu.wialontransport.util.DataParameterUtil.findAnyHdopParameterName;

public final class NewWingDataPackageHandler extends DataPackageHandler<NewWingDataPackage, NewWingData> {
    private static final int YEAR_MARK_POINT = 2000;
    private static final int MILLI_VOLTS_IN_VOLT = 1000;

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
    protected Stream<NewWingData> findSources(final NewWingDataPackage requestPackage) {
        return requestPackage.getDataStream();
    }

    @Override
    protected LocalDateTime getDateTime(NewWingData data) {
        return null;
    }

    @Override
    protected Coordinate getCoordinate(NewWingData data) {
        return null;
    }

    @Override
    protected OptionalInt findCourse(NewWingData data) {
        return null;
    }

    @Override
    protected OptionalDouble findSpeed(NewWingData data) {
        return null;
    }

//    @Override
//    protected void accumulateComponents(final ReceivedDataBuilder builder, final NewWingData data) {
//        accumulateDateTime(builder, data);
//        accumulateCoordinate(builder, data);
//        accumulateHdop(builder, data);
//        accumulateCourse(builder, data);
//        accumulateSpeed(builder, data);
//        accumulateAnalogInputs(builder, data);
//    }

//    private static void accumulateDateTime(final ReceivedDataBuilder builder, final NewWingData data) {
//        builder.setDateTime(findDateTime(data));
//    }
//
//    private static void accumulateCoordinate(final ReceivedDataBuilder builder, final NewWingData data) {
//        builder.setCoordinate(findCoordinate(data));
//    }
//
//    private static void accumulateHdop(final ReceivedDataBuilder builder, final NewWingData data) {
//        builder.addParameter(findHdop(data));
//    }
//
//    private static void accumulateCourse(final ReceivedDataBuilder builder, final NewWingData data) {
//        builder.setCourse(findCourse(data));
//    }
//
//    private static void accumulateSpeed(final ReceivedDataBuilder builder, final NewWingData data) {
//        builder.setSpeed(findSpeed(data));
//    }
//
//    private static void accumulateAnalogInputs(final ReceivedDataBuilder builder, final NewWingData data) {
//        builder.setAnalogInputs(findAnalogInputs(data));
//    }
//
//    private static LocalDateTime findDateTime(final NewWingData data) {
//        final int year = YEAR_MARK_POINT + data.getYear();
//        final int month = data.getMonth();
//        final int day = data.getDay();
//        final int hour = data.getHour();
//        final int minute = data.getMinute();
//        final int second = data.getSecond();
//        return LocalDateTime.of(year, month, day, hour, minute, second);
//    }
//
//    private static Coordinate findCoordinate(final NewWingData data) {
//        final double latitude = extractLatitude(data);
//        final double longitude = extractLongitude(data);
//        return new Coordinate(latitude, longitude);
//    }
//
//    private static double extractLatitude(final NewWingData data) {
//        return extractDoubleComponentWithIntegerAndFractionalParts(
//                data,
//                NewWingData::getLatitudeIntegerPart,
//                NewWingData::getLatitudeFractionalPart,
//                NewWingCoordinateUtil::calculateLatitude
//        );
//    }
//
//    private static double extractLongitude(final NewWingData data) {
//        return extractDoubleComponentWithIntegerAndFractionalParts(
//                data,
//                NewWingData::getLongitudeIntegerPart,
//                NewWingData::getLongitudeFractionalPart,
//                NewWingCoordinateUtil::calculateLongitude
//        );
//    }
//
//    private static Parameter findHdop(final NewWingData data) {
//        final String parameterName = findAnyHdopParameterName();
//        final double value = extractHdopValue(data);
//        return createDoubleParameter(parameterName, value);
//    }
//
//    private static double extractHdopValue(final NewWingData data) {
//        return extractDoubleComponentWithIntegerAndFractionalParts(
//                data,
//                NewWingData::getHdopIntegerPart,
//                NewWingData::getHdopFractionalPart,
//                NumberUtil::createDoubleByParts
//        );
//    }
//
//    private static int findCourse(final NewWingData data) {
//        return data.getCourse();
//    }
//
//    private static double findSpeed(final NewWingData data) {
//        return extractDoubleComponentWithIntegerAndFractionalParts(
//                data,
//                NewWingData::getSpeedIntegerPart,
//                NewWingData::getSpeedFractionalPart,
//                NumberUtil::createDoubleByParts
//        );
//    }

    @Override
    protected OptionalInt findAltitude(NewWingData data) {
        return null;
    }

    @Override
    protected OptionalInt findAmountOfSatellites(NewWingData data) {
        return null;
    }

    @Override
    protected OptionalDouble findReductionPrecision(NewWingData data) {
        return null;
    }

    @Override
    protected OptionalInt findInputs(NewWingData data) {
        return null;
    }

    @Override
    protected OptionalInt findOutputs(NewWingData data) {
        return null;
    }

    @Override
    protected Optional<double[]> findAnalogInputs(NewWingData data) {
        return Optional.empty();
    }

//    private static double[] findAnalogInputs(final NewWingData data) {
//        final double firstAnalogInput = extractFirstAnalogInputLevel(data);
//        final double secondAnalogInput = extractSecondAnalogInputLevel(data);
//        final double thirdAnalogInput = extractThirdAnalogInputLevel(data);
//        final double fourthAnalogInput = extractFourthAnalogInputLevel(data);
//        return new double[]{firstAnalogInput, secondAnalogInput, thirdAnalogInput, fourthAnalogInput};
//    }

    @Override
    protected Optional<String> findDriverKeyCode(NewWingData data) {
        return Optional.empty();
    }

    @Override
    protected Stream<Parameter> findParameters(NewWingData data) {
        return null;
    }

    private static double extractFirstAnalogInputLevel(final NewWingData data) {
        return extractAnalogInputLevel(data, NewWingData::getFirstAnalogInputLevel);
    }

    private static double extractSecondAnalogInputLevel(final NewWingData data) {
        return extractAnalogInputLevel(data, NewWingData::getSecondAnalogInputLevel);
    }

    private static double extractThirdAnalogInputLevel(final NewWingData data) {
        return extractAnalogInputLevel(data, NewWingData::getThirdAnalogInputLevel);
    }

    private static double extractFourthAnalogInputLevel(final NewWingData data) {
        return extractAnalogInputLevel(data, NewWingData::getFourthAnalogInputLevel);
    }

    private static double extractAnalogInputLevel(final NewWingData data, final ToShortFunction<NewWingData> getter) {
        final double analogInput = getter.apply(data);
        return analogInput / MILLI_VOLTS_IN_VOLT;
    }

    private static double extractDoubleComponentWithIntegerAndFractionalParts(
            final NewWingData data,
            final ToIntFunction<NewWingData> integerPartGetter,
            final ToIntFunction<NewWingData> fractionalPartGetter,
            final BiIntToDoubleFunction partCombiner
    ) {
        final int integerPart = integerPartGetter.applyAsInt(data);
        final int fractionalPart = fractionalPartGetter.applyAsInt(data);
        return partCombiner.apply(integerPart, fractionalPart);
    }
}
