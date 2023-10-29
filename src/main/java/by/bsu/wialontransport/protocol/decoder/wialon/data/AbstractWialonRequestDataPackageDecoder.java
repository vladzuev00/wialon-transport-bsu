package by.bsu.wialontransport.protocol.decoder.wialon.data;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.exception.AnsweredException;
import by.bsu.wialontransport.protocol.decoder.wialon.WialonPackageDecoder;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.DataParser;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidDataException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractWialonRequestDataPackage;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public abstract class AbstractWialonRequestDataPackageDecoder<
        P extends AbstractWialonRequestDataPackage,
        R extends WialonPackage>
        extends WialonPackageDecoder<P> {
    private final DataParser dataParser;

    public AbstractWialonRequestDataPackageDecoder(final String packagePrefix, final DataParser dataParser) {
        super(packagePrefix);
        this.dataParser = dataParser;
    }

    @Override
    protected final P decodeMessage(final String message) {
        return this.splitIntoSubMessages(message)
                .map(this::parseSubMessage)
                .collect(
                        collectingAndThen(
                                toList(),
                                this::createPackage
                        )
                );
    }

    protected abstract Stream<String> splitIntoSubMessages(final String message);

    protected abstract P createPackage(final List<Data> data);

    protected abstract R createResponseNotValidDataPackage();

    private Data parseSubMessage(final String message) {
        try {
            return this.dataParser.parse(message);
        } catch (final NotValidDataException cause) {
            return this.throwAnsweredException(cause);
        }
    }

    private Data throwAnsweredException(final NotValidDataException cause) {
        final R exceptionAnswer = this.createResponseNotValidDataPackage();
        throw new AnsweredException(exceptionAnswer, cause);
    }
}
