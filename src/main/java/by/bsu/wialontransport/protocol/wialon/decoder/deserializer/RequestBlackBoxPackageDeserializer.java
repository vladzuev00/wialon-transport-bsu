package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.DataParser;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidDataException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.RequestBlackBoxPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.stream;

@Component
@RequiredArgsConstructor
public final class RequestBlackBoxPackageDeserializer implements PackageDeserializer {
    private static final String REGEX_DATA_DELIMITER = "\\|";
    private static final String RESPONSE_FAILURE_HANDLING = "#AB#0";

    private final DataParser dataParser;

    @Override
    public RequestBlackBoxPackage deserialize(final String source) {
        try {
            final String message = PackageDeserializer.removePrefix(source);
            final String[] dataStrings = message.split(REGEX_DATA_DELIMITER);
            final List<Data> data = stream(dataStrings)
                    .map(this.dataParser::parse)
                    .toList();
            return new RequestBlackBoxPackage(data);
        } catch (final NotValidDataException cause) {
            throw new AnswerableException(RESPONSE_FAILURE_HANDLING, cause);
        }
    }
}
