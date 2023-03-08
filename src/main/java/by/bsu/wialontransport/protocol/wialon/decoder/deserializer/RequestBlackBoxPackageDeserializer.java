package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.DataParser;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidDataException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.RequestBlackBoxPackage;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseBlackBoxPackage;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.stream;

@Component
public final class RequestBlackBoxPackageDeserializer extends AbstractPackageDeserializer {
    private static final String REGEX_DATA_DELIMITER = "\\|";
    private static final String RESPONSE_FAILURE_HANDLING = ResponseBlackBoxPackage.PREFIX + "0";

    private final DataParser dataParser;

    public RequestBlackBoxPackageDeserializer(final DataParser dataParser) {
        super(RequestBlackBoxPackage.PREFIX);
        this.dataParser = dataParser;
    }

    @Override
    protected Package deserializeByMessage(final String message) {
        try {
            final List<Data> data = this.parseData(message);
            return new RequestBlackBoxPackage(data);
        } catch (final NotValidDataException cause) {
            throw new AnswerableException(RESPONSE_FAILURE_HANDLING, cause);
        }
    }

    private List<Data> parseData(final String message) {
        final String[] dataStrings = message.split(REGEX_DATA_DELIMITER);
        return stream(dataStrings)
                .map(this.dataParser::parse)
                .toList();
    }
}
