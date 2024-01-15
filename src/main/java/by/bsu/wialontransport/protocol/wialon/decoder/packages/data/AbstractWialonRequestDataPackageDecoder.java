package by.bsu.wialontransport.protocol.wialon.decoder.packages.data;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.exception.AnsweredException;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.WialonPackageDecoder;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.WialonMessageParser;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception.NotValidSubMessageException;
import by.bsu.wialontransport.protocol.wialon.model.WialonData;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractWialonRequestDataPackage;

import java.util.List;
import java.util.stream.Stream;

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
        final List<WialonData> data = messageParser.parse(message);
        return createPackage(data);
    }

    protected abstract Stream<String> splitIntoSubMessages(final String message);

    protected abstract REQUEST createPackage(final List<WialonData> data);

    protected abstract RESPONSE createResponseNotValidDataPackage();

    private Data parseSubMessage(final String message) {
//        try {
//            return this.wialonMessageParser.parse(message);
//        } catch (final NotValidMessageException cause) {
//            return this.throwAnsweredException(cause);
//        }
        return null;
    }

    private Data throwAnsweredException(final NotValidSubMessageException cause) {
        final RESPONSE exceptionAnswer = this.createResponseNotValidDataPackage();
        throw new AnsweredException(exceptionAnswer, cause);
    }
}
