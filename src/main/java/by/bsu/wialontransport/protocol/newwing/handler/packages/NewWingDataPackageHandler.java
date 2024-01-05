package by.bsu.wialontransport.protocol.newwing.handler.packages;

import by.bsu.wialontransport.configuration.property.ReceivedDataDefaultPropertyConfiguration;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.function.BiIntToDoubleFunction;
import by.bsu.wialontransport.function.ToShortFunction;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
import by.bsu.wialontransport.model.CoordinateRequest;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.ReceivingDataPackageHandler;
import by.bsu.wialontransport.protocol.core.service.receivingdata.filter.DataFilter;
import by.bsu.wialontransport.protocol.core.service.receivingdata.fixer.DataFixer;
import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingDataPackage;
import by.bsu.wialontransport.util.NewWingCoordinateUtil;
import by.bsu.wialontransport.util.NumberUtil;

import java.time.LocalDateTime;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static by.bsu.wialontransport.crud.dto.Parameter.createDoubleParameter;
import static by.bsu.wialontransport.util.DataParameterUtil.findAnyHdopParameterName;

public final class NewWingDataPackageHandler extends ReceivingDataPackageHandler<NewWingDataPackage, NewWingData> {
    private static final int YEAR_MARK_POINT = 2000;
    private static final int MILLI_VOLTS_IN_VOLT = 1000;

    public NewWingDataPackageHandler(final ReceivedDataDefaultPropertyConfiguration dataDefaultPropertyConfiguration,
                                     final ContextAttributeManager contextAttributeManager,
                                     final DataFilter dataFilter,
                                     final KafkaInboundDataProducer kafkaInboundDataProducer,
                                     final DataFixer dataFixer) {
        super(
                NewWingDataPackage.class,
                dataDefaultPropertyConfiguration,
                contextAttributeManager,
                dataFilter,
                kafkaInboundDataProducer,
                dataFixer
        );
    }

    @Override
    protected Stream<NewWingData> extractDataSources(final NewWingDataPackage requestPackage) {
        return requestPackage.getDataStream();
    }

    @Override
    protected void accumulateComponents(final ReceivedDataBuilder builder, final NewWingData data) {
        accumulateDateTime(builder, data);
        accumulateCoordinate(builder, data);
        accumulateHdop(builder, data);
        accumulateCourse(builder, data);
        accumulateSpeed(builder, data);
        accumulateAnalogInputs(builder, data);
    }

    private static void accumulateDateTime(final ReceivedDataBuilder builder, final NewWingData data) {
        builder.accumulateComponent(
                data,
                NewWingDataPackageHandler::extractDateTime,
                ReceivedDataBuilder::dateTime
        );
    }

    private static void accumulateCoordinate(final ReceivedDataBuilder builder, final NewWingData data) {
        builder.accumulateComponent(
                data,
                NewWingDataPackageHandler::extractCoordinate,
                ReceivedDataBuilder::coordinate
        );
    }

    private static void accumulateHdop(final ReceivedDataBuilder builder, final NewWingData data) {
        builder.accumulateComponent(
                data,
                NewWingDataPackageHandler::extractHdop,
                ReceivedDataBuilder::parameter
        );
    }

    private static void accumulateCourse(final ReceivedDataBuilder builder, final NewWingData data) {
        builder.accumulateIntComponent(
                data,
                NewWingDataPackageHandler::extractCourse,
                ReceivedDataBuilder::course
        );
    }

    private static void accumulateSpeed(final ReceivedDataBuilder builder, final NewWingData data) {
        builder.accumulateDoubleComponent(
                data,
                NewWingDataPackageHandler::extractSpeed,
                ReceivedDataBuilder::speed
        );
    }

    private static void accumulateAnalogInputs(final ReceivedDataBuilder builder, final NewWingData data) {
        builder.accumulateComponent(
                data,
                NewWingDataPackageHandler::extractAnalogInputs,
                ReceivedDataBuilder::analogInputs
        );
    }

    private static LocalDateTime extractDateTime(final NewWingData data) {
        final int year = YEAR_MARK_POINT + data.getYear();
        final int month = data.getMonth();
        final int day = data.getDay();
        final int hour = data.getHour();
        final int minute = data.getMinute();
        final int second = data.getSecond();
        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

    private static CoordinateRequest extractCoordinate(final NewWingData data) {
        final double latitude = extractLatitude(data);
        final double longitude = extractLongitude(data);
        return new CoordinateRequest(latitude, longitude);
    }

    private static double extractLatitude(final NewWingData data) {
        return extractDoubleComponentWithIntegerAndFractionalParts(
                data,
                NewWingData::getLatitudeIntegerPart,
                NewWingData::getLatitudeFractionalPart,
                NewWingCoordinateUtil::calculateLatitude
        );
    }

    private static double extractLongitude(final NewWingData data) {
        return extractDoubleComponentWithIntegerAndFractionalParts(
                data,
                NewWingData::getLongitudeIntegerPart,
                NewWingData::getLongitudeFractionalPart,
                NewWingCoordinateUtil::calculateLongitude
        );
    }

    private static Parameter extractHdop(final NewWingData data) {
        final String parameterName = findAnyHdopParameterName();
        final double value = extractHdopValue(data);
        return createDoubleParameter(parameterName, value);
    }

    private static double extractHdopValue(final NewWingData data) {
        return extractDoubleComponentWithIntegerAndFractionalParts(
                data,
                NewWingData::getHdopIntegerPart,
                NewWingData::getHdopFractionalPart,
                NumberUtil::createDoubleByParts
        );
    }

    private static int extractCourse(final NewWingData data) {
        return data.getCourse();
    }

    private static double extractSpeed(final NewWingData data) {
        return extractDoubleComponentWithIntegerAndFractionalParts(
                data,
                NewWingData::getSpeedIntegerPart,
                NewWingData::getSpeedFractionalPart,
                NumberUtil::createDoubleByParts
        );
    }

    private static double[] extractAnalogInputs(final NewWingData data) {
        final double firstAnalogInput = extractFirstAnalogInputLevel(data);
        final double secondAnalogInput = extractSecondAnalogInputLevel(data);
        final double thirdAnalogInput = extractThirdAnalogInputLevel(data);
        final double fourthAnalogInput = extractFourthAnalogInputLevel(data);
        return new double[]{firstAnalogInput, secondAnalogInput, thirdAnalogInput, fourthAnalogInput};
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
