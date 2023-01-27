package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.factory.DataFactory;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidDataException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.blackbox.RequestBlackBoxPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Arrays.stream;

@Component
@RequiredArgsConstructor
public final class RequestBlackBoxPackageDeserializer implements PackageDeserializer {
    private static final String REGEX_DATA_DELIMITER = "\\|";
    private static final String RESPONSE_FAILURE_HANDLING = "#AB#0";

    private final DataFactory dataFactory;

    @Override
    public RequestBlackBoxPackage deserialize(final String source) {
        try {
            final String message = PackageDeserializer.removePrefix(source);
            final String[] dataStrings = message.split(REGEX_DATA_DELIMITER);
//            final List<Data> data = stream(dataStrings)
//                    //.map(this.dataFactory::create)
//                    .collect(toList());
            return new RequestBlackBoxPackage(null);
        } catch (final NotValidDataException cause) {
            throw new AnswerableException(RESPONSE_FAILURE_HANDLING, cause);
        }
    }
}
