package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.components;

import by.bsu.wialontransport.util.DataRegexUtil;

public final class DataComponentsParser extends AbstractDataComponentsParser {

    public DataComponentsParser(final String source) {
        super(DataRegexUtil.PATTERN_DATA, source);
    }
}
