package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.crud.dto.ExtendedData;
import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.ExtendedDataParser;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidMessageException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.RequestDataPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public final class RequestDataPackageDeserializer implements PackageDeserializer {
    private static final String RESPONSE_FAILURE_HANDLING = "#AD#0";
    private final ExtendedDataParser extendedDataParser;

    @Override
    public RequestDataPackage deserialize(final String source) {
        try {
            final String extendedDataString = PackageDeserializer.removePrefix(source);
            final ExtendedData extendedData = this.extendedDataParser.parse(extendedDataString);
            return new RequestDataPackage(extendedData);
        } catch (final NotValidMessageException cause) {
            throw new AnswerableException(RESPONSE_FAILURE_HANDLING, cause);
        }
    }
}