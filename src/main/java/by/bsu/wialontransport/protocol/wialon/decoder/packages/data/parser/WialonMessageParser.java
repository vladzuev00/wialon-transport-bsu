package by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser;

import by.bsu.wialontransport.protocol.wialon.model.WialonData;
import by.bsu.wialontransport.protocol.wialon.model.WialonData.WialonDataBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Arrays.stream;

@Component
public final class WialonMessageParser {
    private static final String SUB_MESSAGE_DELIMITER_REGEX = "\\|";

    public List<WialonData> parse(final String message) {
        return stream(message.split(SUB_MESSAGE_DELIMITER_REGEX))
                .map(this::parseSubMessage)
                .toList();
    }

    private WialonData parseSubMessage(final String subMessage) {
        final WialonSubMessageComponentParser parser = new WialonSubMessageComponentParser(subMessage);
        final WialonDataBuilder builder = WialonData.builder();
        parseDate(parser, builder);
        parseTime(parser, builder);
        parseLatitude(parser, builder);
        parseLongitude(parser, builder);
        parseSpeed(parser, builder);
        parseCourse(parser, builder);
        parseAltitude(parser, builder);
        parseAmountOfSatellites(parser, builder);
        parseHdop(parser, builder);
        parseInputs(parser, builder);
        parseOutputs(parser, builder);
        parseAnalogInputs(parser, builder);
        parseDriverKeyCode(parser, builder);
        parseParameters(parser, builder);
        return builder.build();
    }

    private void parseDate(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseRequiredComponent(parser::parseDate, builder::date);
    }

    private void parseTime(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseRequiredComponent(parser::parseTime, builder::time);
    }

    private void parseLatitude(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseRequiredComponent(parser::parseLatitude, builder::latitude);
    }

    private void parseLongitude(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseRequiredComponent(parser::parseLongitude, builder::longitude);
    }

    private <T> void parseRequiredComponent(final Supplier<T> parser, final Consumer<T> accumulator) {
        accumulator.accept(parser.get());
    }

    private void parseSpeed(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseNotRequiredComponent(parser::parseSpeed, builder::speed);
    }

    private void parseCourse(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseNotRequiredComponent(parser::parseCourse, builder::course);
    }

    private void parseAltitude(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseNotRequiredComponent(parser::parseAltitude, builder::altitude);
    }

    private void parseAmountOfSatellites(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseNotRequiredComponent(parser::parseAmountOfSatellites, builder::amountOfSatellites);
    }

    private void parseHdop(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseNotRequiredComponent(parser::parseHdop, builder::hdop);
    }

    private void parseInputs(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseNotRequiredComponent(parser::parseInputs, builder::inputs);
    }

    private void parseOutputs(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseNotRequiredComponent(parser::parseOutputs, builder::outputs);
    }

    private void parseAnalogInputs(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseNotRequiredComponent(parser::parseAnalogInputs, builder::analogInputs);
    }

    private void parseDriverKeyCode(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseNotRequiredComponent(parser::parseDriverKeyCode, builder::driverKeyCode);
    }

    private void parseParameters(final WialonSubMessageComponentParser parser, final WialonDataBuilder builder) {
        parseNotRequiredComponent(parser::parseParameters, builder::parameters);
    }

    private <T> void parseNotRequiredComponent(final Supplier<Optional<T>> parser, final Consumer<T> accumulator) {
        parser.get().ifPresent(accumulator);
    }
}
