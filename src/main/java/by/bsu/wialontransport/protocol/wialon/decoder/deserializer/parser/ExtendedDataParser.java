package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser;

import by.bsu.wialontransport.crud.dto.ExtendedData;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.components.ExtendedDataComponentsParser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public final class ExtendedDataParser extends AbstractDataParser<ExtendedData, ExtendedDataComponentsParser> {

    @Override
    protected ExtendedDataComponentsParser createParser() {
        return new ExtendedDataComponentsParser();
    }

    @Override
    protected ExtendedData create(final ExtendedDataComponentsParser parser) {
        final LocalDateTime dateTime = parser.parseDateTime();
        return ExtendedData.extendedDataBuilder()
                .date(dateTime.toLocalDate())
                .time(dateTime.toLocalTime())
                .latitude(parser.parseLatitude())
                .longitude(parser.parseLongitude())
                .speed(parser.parseSpeed())
                .course(parser.parseCourse())
                .height(parser.parseAltitude())
                .amountOfSatellites(parser.parseAmountSatellites())
                .reductionPrecision(parser.parseReductionPrecision())
                .inputs(parser.parseInputs())
                .outputs(parser.parseOutputs())
                .analogInputs(parser.parseAnalogInputs())
                .driverKeyCode(parser.parseDriverKeyCode())
                //.parameters(parser.parseParameters())
                .build();
    }
}
