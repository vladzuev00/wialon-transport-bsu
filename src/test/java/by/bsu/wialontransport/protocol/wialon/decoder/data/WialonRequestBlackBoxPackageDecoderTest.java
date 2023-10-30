package by.bsu.wialontransport.protocol.wialon.decoder.data;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestBlackBoxPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseBlackBoxPackage;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public final class WialonRequestBlackBoxPackageDecoderTest {
    private final WialonRequestBlackBoxPackageDecoder decoder = new WialonRequestBlackBoxPackageDecoder(null);

    @Test
    public void messageShouldBeSplittedIntoSubMessages() {
        final String givenMessage = "first|second|third";

        final Stream<String> actual = this.decoder.splitIntoSubMessages(givenMessage);
        final List<String> actualAsList = actual.toList();
        final List<String> expectedAsList = List.of("first", "second", "third");
        assertEquals(expectedAsList, actualAsList);
    }

    @Test
    public void messageShouldBeSplittedToOneSubMessage() {
        final String givenMessage = "first";

        final Stream<String> actual = this.decoder.splitIntoSubMessages(givenMessage);
        final List<String> actualAsList = actual.toList();
        final List<String> expectedAsList = List.of("first");
        assertEquals(expectedAsList, actualAsList);
    }

    @Test
    public void packageShouldBeCreated() {
        final List<Data> givenData = List.of(
                createData(1L),
                createData(2L),
                createData(3L)
        );

        final WialonRequestBlackBoxPackage actual = this.decoder.createPackage(givenData);
        final WialonRequestBlackBoxPackage expected = new WialonRequestBlackBoxPackage(givenData);
        assertEquals(expected, actual);
    }

    @Test
    public void responseNotValidDataPackageShouldBeCreated() {
        final WialonResponseBlackBoxPackage actual = this.decoder.createResponseNotValidDataPackage();
        final WialonResponseBlackBoxPackage expected = new WialonResponseBlackBoxPackage(0);
        assertEquals(expected, actual);
    }

    private static Data createData(final Long id) {
        return Data.builder()
                .id(id)
                .build();
    }
}
