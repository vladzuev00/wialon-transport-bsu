package by.bsu.wialontransport.protocol.wialon.decoder.packages.data;

import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.WialonPackageDecoder;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.WialonMessageParser;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception.NotValidSubMessageException;
import by.bsu.wialontransport.protocol.wialon.model.WialonData;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractWialonRequestDataPackage;

import java.util.List;

public abstract class AbstractWialonRequestDataPackageDecoder<
        REQUEST extends AbstractWialonRequestDataPackage,
        RESPONSE extends WialonPackage
        >
        extends WialonPackageDecoder<REQUEST> {
    private final WialonMessageParser messageParser;

    public AbstractWialonRequestDataPackageDecoder(final String packagePrefix, final WialonMessageParser messageParser) {
        super(packagePrefix);
        this.messageParser = messageParser;
    }

    @Override
    protected final REQUEST decodeMessage(final String message) {
        try {
            final List<WialonData> data = messageParser.parse(message);
            return createPackage(data);
        } catch (final NotValidSubMessageException cause) {
            final RESPONSE response = createNotValidSubMessageResponse();
            throw new AnswerableException(response, cause);
        }
    }

    protected abstract REQUEST createPackage(final List<WialonData> data);

    protected abstract RESPONSE createNotValidSubMessageResponse();
}
