package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.components.DataComponentsParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class DataParser {
    private final DataComponentsParser dataComponentsParser;

    public Data parse(final String source) {

    }
}
