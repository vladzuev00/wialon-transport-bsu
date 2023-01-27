package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.components.DataComponentsParser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public final class DataParser extends AbstractDataParser<Data, DataComponentsParser> {

    @Override
    protected DataComponentsParser createParser() {
        return new DataComponentsParser();
    }

    @Override
    protected Data create(final DataComponentsParser parser) {
        final LocalDateTime dateTime = parser.parseDateTime();
//        return Data.dataBuilder()
//                .date(dateTime.toLocalDate())
//                .time(dateTime.toLocalTime())
//                .latitude(parser.parseLatitude())
//                .longitude(parser.parseLongitude())
//                .speed(parser.parseSpeed())
//                .course(parser.parseCourse())
//                .height(parser.parseAltitude())
//                .amountOfSatellites(parser.parseAmountSatellites())
//                .build();
        return null;
    }
}
