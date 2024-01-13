package by.bsu.wialontransport.protocol.wialon.decoder.packages.data;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.exception.AnsweredException;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.WialonPackageDecoder;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.DataParser;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception.NotValidMessageException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractWialonRequestDataPackage;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public abstract class AbstractWialonRequestDataPackageDecoder<
        REQUEST_PACKAGE extends AbstractWialonRequestDataPackage,
        RESPONSE_PACKAGE extends WialonPackage
        >
        extends WialonPackageDecoder<REQUEST_PACKAGE> {
    private final DataParser dataParser;

    public AbstractWialonRequestDataPackageDecoder(final String packagePrefix, final DataParser dataParser) {
        super(packagePrefix);
        this.dataParser = dataParser;
    }

    @Override
    protected final REQUEST_PACKAGE decodeMessage(final String message) {
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

    protected abstract REQUEST_PACKAGE createPackage(final List<Data> data);

    protected abstract RESPONSE_PACKAGE createResponseNotValidDataPackage();

    private Data parseSubMessage(final String message) {
        try {
            return this.dataParser.parse(message);
        } catch (final NotValidMessageException cause) {
            return this.throwAnsweredException(cause);
        }
    }

    private Data throwAnsweredException(final NotValidMessageException cause) {
        final RESPONSE_PACKAGE exceptionAnswer = this.createResponseNotValidDataPackage();
        throw new AnsweredException(exceptionAnswer, cause);
    }
}
