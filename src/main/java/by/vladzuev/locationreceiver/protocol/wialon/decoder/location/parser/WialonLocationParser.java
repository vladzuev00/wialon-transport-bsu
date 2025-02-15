package by.vladzuev.locationreceiver.protocol.wialon.decoder.location.parser;

import by.vladzuev.locationreceiver.protocol.wialon.model.WialonLocation;
import by.vladzuev.locationreceiver.protocol.wialon.model.WialonLocation.WialonLocationBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

@Component
public final class WialonLocationParser {

    public WialonLocation parse(final String source) {
        final WialonLocationComponentParser componentParser = new WialonLocationComponentParser(source);
        final WialonLocationBuilder builder = WialonLocation.builder();
        parseDate(componentParser, builder);
        parseTime(componentParser, builder);
        parseLatitude(componentParser, builder);
        parseLongitude(componentParser, builder);
        parseSpeed(componentParser, builder);
        parseCourse(componentParser, builder);
        parseAltitude(componentParser, builder);
        parseSatelliteCount(componentParser, builder);
        parseHdop(componentParser, builder);
        parseInputs(componentParser, builder);
        parseOutputs(componentParser, builder);
        parseAnalogInputs(componentParser, builder);
        parseDriverKeyCode(componentParser, builder);
        parseParameters(componentParser, builder);
        return builder.build();
    }

    private void parseDate(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        parseNotRequiredComponent(parser::parseDate, builder::date);
    }

    private void parseTime(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        parseNotRequiredComponent(parser::parseTime, builder::time);
    }

    private void parseLatitude(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        parseNotRequiredComponent(parser::parseLatitude, builder::latitude);
    }

    private void parseLongitude(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        parseNotRequiredComponent(parser::parseLongitude, builder::longitude);
    }

    private void parseSpeed(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        parseNotRequiredComponent(parser::parseSpeed, builder::speed);
    }

    private void parseCourse(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        parseNotRequiredComponent(parser::parseCourse, builder::course);
    }

    private void parseAltitude(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        parseNotRequiredComponent(parser::parseAltitude, builder::altitude);
    }

    private void parseSatelliteCount(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        parseNotRequiredComponent(parser::parseSatelliteCount, builder::satelliteCount);
    }

    private void parseHdop(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        parseNotRequiredComponent(parser::parseHdop, builder::hdop);
    }

    private void parseInputs(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        parseNotRequiredComponent(parser::parseInputs, builder::inputs);
    }

    private void parseOutputs(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        parseNotRequiredComponent(parser::parseOutputs, builder::outputs);
    }

    private void parseAnalogInputs(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        builder.analogInputs(parser.parseAnalogInputs());
    }

    private void parseDriverKeyCode(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        parseNotRequiredComponent(parser::parseDriverKeyCode, builder::driverKeyCode);
    }

    private void parseParameters(final WialonLocationComponentParser parser, final WialonLocationBuilder builder) {
        builder.parameters(parser.parseParameters());
    }

    private <T> void parseNotRequiredComponent(final Supplier<Optional<T>> parser, final Consumer<T> accumulator) {
        parser.get().ifPresent(accumulator);
    }

    private void parseNotRequiredComponent(final Supplier<OptionalDouble> parser, final DoubleConsumer accumulator) {
        parser.get().ifPresent(accumulator);
    }

    private void parseNotRequiredComponent(final Supplier<OptionalInt> parser, final IntConsumer accumulator) {
        parser.get().ifPresent(accumulator);
    }
}
