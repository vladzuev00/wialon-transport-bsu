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
        final NewWingDataBuilder builder = NewWingData.builder();
        decodeHour(buffer, builder);
        decodeMinute(buffer, builder);
        decodeSecond(buffer, builder);
        decodeLatitudeIntegerPart(buffer, builder);
        decodeLatitudeFractionalPart(buffer, builder);
        decodeLongitudeIntegerPart(buffer, builder);
        decodeLongitudeFractionalPart(buffer, builder);
        decodeHdopIntegerPart(buffer, builder);
        decodeHdopFractionalPart(buffer, builder);
        decodeCourse(buffer, builder);
        decodeSpeedIntegerPart(buffer, builder);
        decodeSpeedFractionalPart(buffer, builder);
        decodeDay(buffer, builder);
        decodeMonth(buffer, builder);
        decodeYear(buffer, builder);
        decodeFirstAnalogInputLevel(buffer, builder);
        decodeSecondAnalogInputLevel(buffer, builder);
        decodeThirdAnalogInputLevel(buffer, builder);
        decodeFourthAnalogInputLevel(buffer, builder);
        decodeFlagByte(buffer, builder);
        decodeDiscreteInputStateByte(buffer, builder);
        decodeChecksum(buffer, builder);
        return builder.build();
    }

    private static void decodeHour(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeByteValue(buffer, builder::hour);
    }

    private static void decodeMinute(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeByteValue(buffer, builder::minute);
    }

    private static void decodeSecond(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeByteValue(buffer, builder::second);
    }

    private static void decodeLatitudeIntegerPart(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeShortValue(buffer, builder::latitudeIntegerPart);
    }

    private static void decodeLatitudeFractionalPart(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeShortValue(buffer, builder::latitudeFractionalPart);
    }

    private static void decodeLongitudeIntegerPart(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeShortValue(buffer, builder::longitudeIntegerPart);
    }

    private static void decodeLongitudeFractionalPart(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeShortValue(buffer, builder::longitudeFractionalPart);
    }

    private static void decodeHdopIntegerPart(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeByteValue(buffer, builder::hdopIntegerPart);
    }

    private static void decodeHdopFractionalPart(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeByteValue(buffer, builder::hdopFractionalPart);
    }

    private static void decodeCourse(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeShortValue(buffer, builder::course);
    }

    private static void decodeSpeedIntegerPart(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeShortValue(buffer, builder::speedIntegerPart);
    }

    private static void decodeSpeedFractionalPart(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeByteValue(buffer, builder::speedFractionalPart);
    }

    private static void decodeDay(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeByteValue(buffer, builder::day);
    }

    private static void decodeMonth(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeByteValue(buffer, builder::month);
    }

    private static void decodeYear(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeByteValue(buffer, builder::year);
    }

    private static void decodeFirstAnalogInputLevel(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeShortValue(buffer, builder::firstAnalogInputLevel);
    }

    private static void decodeSecondAnalogInputLevel(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeShortValue(buffer, builder::secondAnalogInputLevel);
    }

    private static void decodeThirdAnalogInputLevel(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeShortValue(buffer, builder::thirdAnalogInputLevel);
    }

    private static void decodeFourthAnalogInputLevel(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeShortValue(buffer, builder::fourthAnalogInputLevel);
    }

    private static void decodeFlagByte(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeByteValue(buffer, builder::flagByte);
    }

    private static void decodeDiscreteInputStateByte(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeByteValue(buffer, builder::discreteInputStateByte);
    }

    private static void decodeChecksum(final ByteBuf buffer, final NewWingDataBuilder builder) {
        decodeShortValue(buffer, builder::checksum);
    }

    private static void decodeShortValue(final ByteBuf buffer, final ShortConsumer accumulator) {
        final short value = buffer.readShortLE();
        accumulator.accept(value);
    }

    private static void decodeByteValue(final ByteBuf buffer, final ByteConsumer accumulator) {
        final byte value = buffer.readByte();
        accumulator.accept(value);
    }
}