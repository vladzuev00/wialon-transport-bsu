package by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser;

import by.bsu.wialontransport.crud.dto.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public final class DataParser {

    public Data parse(final String source) {
        final WialonMessageComponentParser componentsParser = new WialonMessageComponentParser(source);
        final LocalDateTime dateTime = componentsParser.parseDateTime();
        return Data.builder()
//                .date(dateTime.toLocalDate())
//                .time(dateTime.toLocalTime())
//                .latitude(componentsParser.parseLatitude())
//                .longitude(componentsParser.parseLongitude())
                .speed(componentsParser.parseSpeed())
                .course(componentsParser.parseCourse())
                .altitude(componentsParser.parseAltitude())
                .amountOfSatellites(componentsParser.parseAmountSatellites())
                .hdop(componentsParser.parseHdop())
                .inputs(componentsParser.parseInputs())
                .outputs(componentsParser.parseOutputs())
                .analogInputs(componentsParser.parseAnalogInputs())
                .driverKeyCode(componentsParser.parseDriverKeyCode())
                .parametersByNames(componentsParser.parseParameters())
                .build();
    }
}
