package by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer;

import by.bsu.wialontransport.protocol.core.exception.AnsweredException;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.WialonMessageParser;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception.NotValidSubMessageException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestDataPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.Status.ERROR_PACKAGE_STRUCTURE;

@Component
public final class RequestDataPackageDeserializer extends AbstractPackageDeserializer {
    private final WialonMessageParser wialonMessageParser;

    public RequestDataPackageDeserializer(final WialonMessageParser wialonMessageParser) {
        super(WialonRequestDataPackage.PREFIX);
        this.wialonMessageParser = wialonMessageParser;
    }

    @Override
    protected WialonRequestDataPackage deserializeByMessage(final String message) {
//        try {
//            final Data data = this.wialonMessageParser.parse(message);
//            return new WialonRequestDataPackage(data);
//        } catch (final NotValidMessageException cause) {
//            throw createAnswerableException(cause);
//        }
        return null;
    }

    private static AnsweredException createAnswerableException(final NotValidSubMessageException cause) {
        final WialonResponseDataPackage answer = new WialonResponseDataPackage(ERROR_PACKAGE_STRUCTURE);
        return new AnsweredException(answer, cause);
    }
}