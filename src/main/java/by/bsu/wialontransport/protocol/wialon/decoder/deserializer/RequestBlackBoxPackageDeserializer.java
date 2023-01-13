package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.ExtendedDataParser;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidMessageException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.blackbox.RequestBlackBoxPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public final class RequestBlackBoxPackageDeserializer implements PackageDeserializer {
    private static final String REGEX_DATA_DELIMITER = "\\|";
    private static final String RESPONSE_FAILURE_HANDLING = "#AB#0";

    private final ExtendedDataParser extendedDataParser;

    @Override
    public RequestBlackBoxPackage deserialize(final String source) {
        try {
            final String message = PackageDeserializer.removePrefix(source);
            final String[] dataStrings = message.split(REGEX_DATA_DELIMITER);
            final List<Data> data = stream(dataStrings)
                    .map(this.extendedDataParser::parse)
                    .collect(toList());
            return new RequestBlackBoxPackage(data);
        } catch (final NotValidMessageException cause) {
            throw new AnswerableException(RESPONSE_FAILURE_HANDLING, cause);
        }
    }
}
