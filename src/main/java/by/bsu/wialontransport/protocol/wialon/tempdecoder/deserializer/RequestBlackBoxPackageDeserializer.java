package by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer;

import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.WialonMessageParser;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception.NotValidSubMessageException;
import by.bsu.wialontransport.protocol.wialon.model.WialonData;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestBlackBoxPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseBlackBoxPackage;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.stream;

//TODO: do super class for this and DataRequestDataPackageDeserializer for throwing AnswerableException
@Component
public final class RequestBlackBoxPackageDeserializer extends AbstractPackageDeserializer {
    private static final String REGEX_DATA_DELIMITER = "\\|";

    private final WialonMessageParser wialonMessageParser;

    public RequestBlackBoxPackageDeserializer(final WialonMessageParser wialonMessageParser) {
        super(WialonRequestBlackBoxPackage.PREFIX);
        this.wialonMessageParser = wialonMessageParser;
    }

    @Override
    protected WialonPackage deserializeByMessage(final String message) {
        try {
            final List<WialonData> data = this.parseData(message);
            return new WialonRequestBlackBoxPackage(data);
        } catch (final NotValidSubMessageException cause) {
            throw createAnswerableException(cause);
        }
    }

    private List<WialonData> parseData(final String message) {
//        final String[] dataStrings = message.split(REGEX_DATA_DELIMITER);
//        return stream(dataStrings)
//                .map(this.wialonMessageParser::parse)
//                .toList();
        return null;
    }

    private static AnswerableException createAnswerableException(final NotValidSubMessageException cause) {
        final WialonResponseBlackBoxPackage answer = new WialonResponseBlackBoxPackage(0);
        return new AnswerableException(answer, cause);
    }
}
