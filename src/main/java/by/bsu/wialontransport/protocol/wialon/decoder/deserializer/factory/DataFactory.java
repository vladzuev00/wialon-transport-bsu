package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.factory;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.DataParser;
import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.exception.NotValidMessageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class DataFactory {
    private final DataParser dataParser;
    //private final ExtendedDataParser extendedDataParser;

//    public Data createAsData(final String source) {
//        return this.dataParser.parse(source);
//    }
//
//    //public ExtendedData createAsExtendedData(final String source) {
//       // return this.extendedDataParser.parse(source);
//    }

//    public Data create(final String source) {
//        try {
//            return this.dataParser.parse(source);
//        } catch (final NotValidMessageException exception) {
//           // return this.extendedDataParser.parse(source);
//        }
//    }
}
