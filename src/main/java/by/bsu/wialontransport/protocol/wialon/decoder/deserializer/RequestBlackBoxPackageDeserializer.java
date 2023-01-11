package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.ExtendedDataParser;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.blackbox.RequestBlackBoxPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class RequestBlackBoxPackageDeserializer implements PackageDeserializer {
    private static final String REGEX_DATA_DELIMITER = "\\|";
    private static final String RESPONSE_FAILURE_HANDLING = "#AB#0";

    private final ExtendedDataParser extendedDataParser;

    @Override
    public RequestBlackBoxPackage deserialize(final String source) {
        try {
            final String setOfDataString = PackageDeserializer.removePrefix(source);
            final String[] dataStrings = setOfDataString.split(REGEX_DATA_DELIMITER);
            final List<Message> messages = stream(serializedMessages)
                    .map(this.extendedDataParser::parse)
                    .collect(toList());
            return new BlackBoxPackage(messages);
        } catch (final NotValidMessageException cause) {
            throw new AnswerableException(RESPONSE_FAILURE_HANDLING, cause);
        }
    }
}
