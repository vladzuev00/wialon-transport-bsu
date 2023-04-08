package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.DataParser;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidDataException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.RequestDataPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseDataPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseDataPackage.Status.ERROR_PACKAGE_STRUCTURE;

@Component
public final class RequestDataPackageDeserializer extends AbstractPackageDeserializer {
    private final DataParser dataParser;

    public RequestDataPackageDeserializer(final DataParser dataParser) {
        super(RequestDataPackage.PREFIX);
        this.dataParser = dataParser;
    }

    @Override
    protected RequestDataPackage deserializeByMessage(final String message) {
        try {
            final Data data = this.dataParser.parse(message);
            return new RequestDataPackage(data);
        } catch (final NotValidDataException cause) {
            throw createAnswerableException(cause);
        }
    }

    private static AnswerableException createAnswerableException(final NotValidDataException cause) {
        final ResponseDataPackage answer = new ResponseDataPackage(ERROR_PACKAGE_STRUCTURE);
        return new AnswerableException(answer, cause);
    }
}