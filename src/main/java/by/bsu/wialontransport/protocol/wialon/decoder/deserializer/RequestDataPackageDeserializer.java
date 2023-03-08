package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.DataParser;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidDataException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.RequestDataPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.RequestDataPackage.PREFIX;

@Component
@RequiredArgsConstructor
public final class RequestDataPackageDeserializer implements PackageDeserializer {
    private static final String RESPONSE_FAILURE_HANDLING = "#AD#-1";

    private final DataParser dataParser;

    @Override
    public RequestDataPackage deserialize(final String source) {
        try {
            final String dataString = PackageDeserializer.removePrefix(source, PREFIX);
            final Data data = this.dataParser.parse(dataString);
            return new RequestDataPackage(data);
        } catch (final NotValidDataException cause) {
            throw new AnswerableException(RESPONSE_FAILURE_HANDLING, cause);
        }
    }
}