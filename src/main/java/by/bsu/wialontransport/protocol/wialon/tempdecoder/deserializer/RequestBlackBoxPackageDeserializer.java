package by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.exception.AnsweredException;
import by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer.parser.DataParser;
import by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer.parser.exception.NotValidDataException;
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

    private final DataParser dataParser;

    public RequestBlackBoxPackageDeserializer(final DataParser dataParser) {
        super(WialonRequestBlackBoxPackage.PREFIX);
        this.dataParser = dataParser;
    }

    @Override
    protected WialonPackage deserializeByMessage(final String message) {
        try {
            final List<Data> data = this.parseData(message);
            return new WialonRequestBlackBoxPackage(data);
        } catch (final NotValidDataException cause) {
            throw createAnswerableException(cause);
        }
    }

    private List<Data> parseData(final String message) {
        final String[] dataStrings = message.split(REGEX_DATA_DELIMITER);
        return stream(dataStrings)
                .map(this.dataParser::parse)
                .toList();
    }

    private static AnsweredException createAnswerableException(final NotValidDataException cause) {
        final WialonResponseBlackBoxPackage answer = new WialonResponseBlackBoxPackage(0);
        return new AnsweredException(answer, cause);
    }
}
