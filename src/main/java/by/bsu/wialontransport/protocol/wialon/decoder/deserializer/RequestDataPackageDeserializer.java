package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.DataParser;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidDataException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.RequestDataPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseDataPackage;
import org.springframework.stereotype.Component;

@Component
public final class RequestDataPackageDeserializer extends AbstractPackageDeserializer {
    private static final String RESPONSE_FAILURE_HANDLING = ResponseDataPackage.PREFIX + "-1";

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
            throw new AnswerableException(RESPONSE_FAILURE_HANDLING, cause);
        }
    }
}