package by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.exception.AnsweredException;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.DataParser;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception.NotValidDataException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestDataPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.Status.ERROR_PACKAGE_STRUCTURE;

@Component
public final class RequestDataPackageDeserializer extends AbstractPackageDeserializer {
    private final DataParser dataParser;

    public RequestDataPackageDeserializer(final DataParser dataParser) {
        super(WialonRequestDataPackage.PREFIX);
        this.dataParser = dataParser;
    }

    @Override
    protected WialonRequestDataPackage deserializeByMessage(final String message) {
        try {
            final Data data = this.dataParser.parse(message);
            return new WialonRequestDataPackage(data);
        } catch (final NotValidDataException cause) {
            throw createAnswerableException(cause);
        }
    }

    private static AnsweredException createAnswerableException(final NotValidDataException cause) {
        final WialonResponseDataPackage answer = new WialonResponseDataPackage(ERROR_PACKAGE_STRUCTURE);
        return new AnsweredException(answer, cause);
    }
}