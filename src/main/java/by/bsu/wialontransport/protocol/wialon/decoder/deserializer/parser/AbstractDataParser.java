package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.components.DataComponentsParser;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidDataException;

import static java.lang.String.format;

public abstract class AbstractDataParser<DataType extends Data, ParserType extends DataComponentsParser> {
    private static final String MESSAGE_TEMPLATE_NOT_VALID_MESSAGE_EXCEPTION = "Message isn't valid. Message: %s.";

    public final DataType parse(final String source) {
        final ParserType parser = this.createParser();
        if (!parser.match(source)) {
            throw new NotValidDataException(format(MESSAGE_TEMPLATE_NOT_VALID_MESSAGE_EXCEPTION, source));
        }
        return this.create(parser);
    }

    protected abstract ParserType createParser();

    protected abstract DataType create(final ParserType parser);
}
