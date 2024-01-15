package by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser;

import by.bsu.wialontransport.protocol.wialon.model.WialonData;
import by.bsu.wialontransport.protocol.wialon.model.WialonData.WialonDataBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.Arrays.stream;

@Component
public final class WialonMessageParser {
    private static final String MESSAGE_DELIMITER_REGEX = "\\|";

    public List<WialonData> parse(final String messages) {
        return stream(messages.split(MESSAGE_DELIMITER_REGEX))
                .map(WialonMessageParser::parseMessage)
                .toList();
    }

    private static WialonData parseMessage(final String message) {
        final WialonSubMessageComponentParser parser = new WialonSubMessageComponentParser(message);
        final WialonDataBuilder builder = WialonData.builder();
        parseDate(builder, parser);
        parseTime(builder, parser);
        parseLatitude(builder, parser);
        parseLongitude(builder, parser);
        parseSpeed(builder, parser);
        parseCourse(builder, parser);
        parseAltitude(builder, parser);
        parseAmountOfSatellites(builder, parser);
        parseHdop(builder, parser);
        parseInputs(builder, parser);
        parseOutputs(builder, parser);
        parseAnalogInputs(builder, parser);
        parseDriverKeyCode(builder, parser);
        parseParameters(builder, parser);
        return builder.build();
    }

    private static void parseDate(final WialonDataBuilder builder, final WialonSubMessageComponentParser parser) {
        parseRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseDate,
                WialonDataBuilder::date
        );
    }

    private static void parseTime(final WialonDataBuilder builder, final WialonSubMessageComponentParser parser) {
        parseRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseTime,
                WialonDataBuilder::time
        );
    }

    private static void parseLatitude(final WialonDataBuilder builder, final WialonSubMessageComponentParser parser) {
        parseRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseLatitude,
                WialonDataBuilder::latitude
        );
    }

    private static void parseLongitude(final WialonDataBuilder builder, final WialonSubMessageComponentParser parser) {
        parseRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseLongitude,
                WialonDataBuilder::longitude
        );
    }

    private static <T> void parseRequiredComponent(
            final WialonDataBuilder builder,
            final WialonSubMessageComponentParser parser,
            final Function<WialonSubMessageComponentParser, T> parsingFunction,
            final BiConsumer<WialonDataBuilder, T> setter
    ) {
        setter.accept(builder, parsingFunction.apply(parser));
    }

    private static void parseSpeed(final WialonDataBuilder builder, final WialonSubMessageComponentParser parser) {
        parseNotRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseSpeed,
                WialonDataBuilder::speed
        );
    }

    private static void parseCourse(final WialonDataBuilder builder, final WialonSubMessageComponentParser parser) {
        parseNotRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseCourse,
                WialonDataBuilder::course
        );
    }

    private static void parseAltitude(final WialonDataBuilder builder, final WialonSubMessageComponentParser parser) {
        parseNotRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseAltitude,
                WialonDataBuilder::altitude
        );
    }

    private static void parseAmountOfSatellites(final WialonDataBuilder builder,
                                                final WialonSubMessageComponentParser parser) {
        parseNotRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseAmountOfSatellites,
                WialonDataBuilder::amountOfSatellites
        );
    }

    private static void parseHdop(final WialonDataBuilder builder, final WialonSubMessageComponentParser parser) {
        parseNotRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseHdop,
                WialonDataBuilder::hdop
        );
    }

    private static void parseInputs(final WialonDataBuilder builder, final WialonSubMessageComponentParser parser) {
        parseNotRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseInputs,
                WialonDataBuilder::inputs
        );
    }

    private static void parseOutputs(final WialonDataBuilder builder, final WialonSubMessageComponentParser parser) {
        parseNotRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseOutputs,
                WialonDataBuilder::outputs
        );
    }

    private static void parseAnalogInputs(final WialonDataBuilder builder, final WialonSubMessageComponentParser parser) {
        parseNotRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseAnalogInputs,
                WialonDataBuilder::analogInputs
        );
    }

    private static void parseDriverKeyCode(final WialonDataBuilder builder, final WialonSubMessageComponentParser parser) {
        parseNotRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseDriverKeyCode,
                WialonDataBuilder::driverKeyCode
        );
    }

    private static void parseParameters(final WialonDataBuilder builder, final WialonSubMessageComponentParser parser) {
        parseNotRequiredComponent(
                builder,
                parser,
                WialonSubMessageComponentParser::parseParameters,
                WialonDataBuilder::parameters
        );
    }

    private static <T> void parseNotRequiredComponent(
            final WialonDataBuilder builder,
            final WialonSubMessageComponentParser parser,
            final Function<WialonSubMessageComponentParser, Optional<T>> parsingFunction,
            final BiConsumer<WialonDataBuilder, T> setter
    ) {
        parsingFunction.apply(parser).ifPresent(component -> setter.accept(builder, component));
    }
}
