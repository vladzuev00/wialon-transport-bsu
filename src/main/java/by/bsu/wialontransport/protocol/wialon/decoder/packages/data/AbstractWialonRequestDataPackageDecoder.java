package by.bsu.wialontransport.protocol.wialon.decoder.packages.data;

import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.WialonPackageDecoder;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.WialonMessageParser;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception.NotValidSubMessageException;
import by.bsu.wialontransport.protocol.wialon.model.WialonData;
import by.bsu.wialontransport.protocol.wialon.model.packages.data.request.AbstractWialonRequestDataPackage;

import java.util.List;

public abstract class AbstractWialonRequestDataPackageDecoder extends WialonPackageDecoder {
    private final WialonMessageParser messageParser;

    public AbstractWialonRequestDataPackageDecoder(final String prefix, final WialonMessageParser messageParser) {
        super(prefix);
        this.messageParser = messageParser;
    }

    @Override
    protected final AbstractWialonRequestDataPackage decodeMessage(final String message) {
        try {
            final List<WialonData> data = messageParser.parse(message);
            return createPackage(data);
        } catch (final NotValidSubMessageException cause) {
            final Package response = createNotValidSubMessageResponse();
            throw new AnswerableException(response, cause);
        }
    }

    protected abstract AbstractWialonRequestDataPackage createPackage(final List<WialonData> data);

    protected abstract Package createNotValidSubMessageResponse();
}
