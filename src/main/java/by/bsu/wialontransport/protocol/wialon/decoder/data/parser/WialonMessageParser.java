package by.bsu.wialontransport.protocol.wialon.decoder.data.parser;

import by.bsu.wialontransport.protocol.wialon.model.WialonLocation;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.stream;

@Component
public final class WialonMessageParser {
    private static final String SUB_MESSAGE_DELIMITER_REGEX = "\\|";

    public List<WialonLocation> parse(final String message) {
        return null;
//        return stream(message.split(SUB_MESSAGE_DELIMITER_REGEX))
//                .map(this::parseSubMessage)
//                .toList();
    }

//    private WialonData parseSubMessage(final String subMessage) {
//        final WialonDataComponentsParser parser = new WialonDataComponentsParser(subMessage);
//        final WialonDataBuilder builder = WialonData.builder();
////        parseDate(parser, builder);
////        parseTime(parser, builder);
////        parseLatitude(parser, builder);
////        parseLongitude(parser, builder);
////        parseSpeed(parser, builder);
////        parseCourse(parser, builder);
////        parseAltitude(parser, builder);
////        parseAmountOfSatellites(parser, builder);
////        parseHdop(parser, builder);
////        parseInputs(parser, builder);
////        parseOutputs(parser, builder);
////        parseAnalogInputs(parser, builder);
////        parseDriverKeyCode(parser, builder);
////        parseParameters(parser, builder);
//        return builder.build();
//    }

//    private void parseDate(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        builder.date(parser.parseDate());
//    }
//
//    private void parseTime(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        builder.time(parser.parseTime());
//    }
//
//    private void parseLatitude(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        builder.latitude(parser.parseLatitude());
//    }
//
//    private void parseLongitude(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        builder.longitude(parser.parseLongitude());
//    }
//
//    private void parseSpeed(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        parseNotRequiredComponent(parser::parseSpeed, builder::speed);
//    }
//
//    private void parseCourse(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        parseNotRequiredComponent(parser::parseCourse, builder::course);
//    }
//
//    private void parseAltitude(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        parseNotRequiredComponent(parser::parseAltitude, builder::altitude);
//    }
//
//    private void parseAmountOfSatellites(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        parseNotRequiredComponent(parser::parseAmountOfSatellites, builder::amountOfSatellites);
//    }
//
//    private void parseHdop(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        parseNotRequiredComponent(parser::parseHdop, builder::hdop);
//    }
//
//    private void parseInputs(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        parseNotRequiredComponent(parser::parseInputs, builder::inputs);
//    }
//
//    private void parseOutputs(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        parseNotRequiredComponent(parser::parseOutputs, builder::outputs);
//    }
//
//    private void parseAnalogInputs(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        parseNotRequiredComponent(parser::parseAnalogInputs, builder::analogInputs);
//    }
//
//    private void parseDriverKeyCode(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        parseNotRequiredComponent(parser::parseDriverKeyCode, builder::driverKeyCode);
//    }
//
//    private void parseParameters(final WialonDataComponentsParser parser, final WialonDataBuilder builder) {
//        parseNotRequiredComponent(parser::parseParameters, builder::parameters);
//    }
//
//    private <T> void parseNotRequiredComponent(final Supplier<Optional<T>> parser, final Consumer<T> accumulator) {
//        parser.get().ifPresent(accumulator);
//    }
}
