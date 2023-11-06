package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import by.bsu.wialontransport.function.ByteConsumer;
import by.bsu.wialontransport.function.ShortConsumer;
import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import by.bsu.wialontransport.protocol.newwing.model.NewWingData.NewWingDataBuilder;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class NewWingDataDecoder {

    public NewWingData decodeNext(final ByteBuf buffer) {
        final NewWingDataBuilder dataBuilder = NewWingData.builder();
        decodeHour(buffer, dataBuilder);
        decodeMinute(buffer, dataBuilder);
        decodeSecond(buffer, dataBuilder);
        decodeLatitudeIntegerPart(buffer, dataBuilder);
        decodeLatitudeFractionalPart(buffer, dataBuilder);
        decodeLongitudeIntegerPart(buffer, dataBuilder);
        decodeLongitudeFractionalPart(buffer, dataBuilder);
        decodeHdopIntegerPart(buffer, dataBuilder);
        decodeHdopFractionalPart(buffer, dataBuilder);
        decodeCourse(buffer, dataBuilder);
        decodeSpeedIntegerPart(buffer, dataBuilder);
        decodeSpeedFractionalPart(buffer, dataBuilder);
        decodeDay(buffer, dataBuilder);
        decodeMonth(buffer, dataBuilder);
        decodeYear(buffer, dataBuilder);
        decodeFirstAnalogInputLevel(buffer, dataBuilder);
        decodeSecondAnalogInputLevel(buffer, dataBuilder);
        decodeThirdAnalogInputLevel(buffer, dataBuilder);
        decodeFourthAnalogInputLevel(buffer, dataBuilder);
        decodeFlagByte(buffer, dataBuilder);
        decodeDiscreteInputStateByte(buffer, dataBuilder);
        decodeChecksum(buffer, dataBuilder);
        return dataBuilder.build();
    }

    private static void decodeHour(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateByteValue(buffer, dataBuilder::hour);
    }

    private static void decodeMinute(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateByteValue(buffer, dataBuilder::minute);
    }

    private static void decodeSecond(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateByteValue(buffer, dataBuilder::second);
    }

    private static void decodeLatitudeIntegerPart(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateShortValue(buffer, dataBuilder::latitudeIntegerPart);
    }

    private static void decodeLatitudeFractionalPart(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateShortValue(buffer, dataBuilder::latitudeFractionalPart);
    }

    private static void decodeLongitudeIntegerPart(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateShortValue(buffer, dataBuilder::longitudeIntegerPart);
    }

    private static void decodeLongitudeFractionalPart(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateShortValue(buffer, dataBuilder::longitudeFractionalPart);
    }

    private static void decodeHdopIntegerPart(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateByteValue(buffer, dataBuilder::hdopIntegerPart);
    }

    private static void decodeHdopFractionalPart(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateByteValue(buffer, dataBuilder::hdopFractionalPart);
    }

    private static void decodeCourse(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateShortValue(buffer, dataBuilder::course);
    }

    private static void decodeSpeedIntegerPart(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateShortValue(buffer, dataBuilder::speedIntegerPart);
    }

    private static void decodeSpeedFractionalPart(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateByteValue(buffer, dataBuilder::speedFractionalPart);
    }

    private static void decodeDay(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateByteValue(buffer, dataBuilder::day);
    }

    private static void decodeMonth(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateByteValue(buffer, dataBuilder::month);
    }

    private static void decodeYear(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateByteValue(buffer, dataBuilder::year);
    }

    private static void decodeFirstAnalogInputLevel(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateShortValue(buffer, dataBuilder::firstAnalogInputLevel);
    }

    private static void decodeSecondAnalogInputLevel(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateShortValue(buffer, dataBuilder::secondAnalogInputLevel);
    }

    private static void decodeThirdAnalogInputLevel(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateShortValue(buffer, dataBuilder::thirdAnalogInputLevel);
    }

    private static void decodeFourthAnalogInputLevel(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateShortValue(buffer, dataBuilder::fourthAnalogInputLevel);
    }

    private static void decodeFlagByte(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateByteValue(buffer, dataBuilder::flagByte);
    }

    private static void decodeDiscreteInputStateByte(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateByteValue(buffer, dataBuilder::discreteInputStateByte);
    }

    private static void decodeChecksum(final ByteBuf buffer, final NewWingDataBuilder dataBuilder) {
        readAndAccumulateShortValue(buffer, dataBuilder::checksum);
    }

    private static void readAndAccumulateShortValue(final ByteBuf buffer, final ShortConsumer accumulator) {
        final short value = buffer.readShortLE();
        accumulator.accept(value);
    }

    private static void readAndAccumulateByteValue(final ByteBuf buffer, final ByteConsumer accumulator) {
        final byte value = buffer.readByte();
        accumulator.accept(value);
    }
}