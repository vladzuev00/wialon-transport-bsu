package by.vladzuev.locationreceiver.protocol.newwing.decoder.location;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static by.vladzuev.locationreceiver.util.ReflectionUtil.getProperty;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public final class NewWingLocationIteratorFactoryTest {
    private static final String FIELD_NAME_ITERATOR_DECODER = "decoder";
    private static final String FIELD_NAME_ITERATOR_BUFFER = "buffer";

    @Mock
    private NewWingLocationDecoder mockedDecoder;

    private NewWingLocationIteratorFactory factory;

    @BeforeEach
    public void initializeFactory() {
        factory = new NewWingLocationIteratorFactory(mockedDecoder);
    }

    @Test
    public void iteratorShouldBeCreated() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final NewWingLocationIterator actual = factory.create(givenBuffer);
        assertNotNull(actual);

        final NewWingLocationDecoder actualDecoder = getDecoder(actual);
        assertSame(mockedDecoder, actualDecoder);

        final ByteBuf actualBuffer = getBuffer(actual);
        assertSame(givenBuffer, actualBuffer);
    }

    private NewWingLocationDecoder getDecoder(final NewWingLocationIterator iterator) {
        return getProperty(iterator, FIELD_NAME_ITERATOR_DECODER, NewWingLocationDecoder.class);
    }

    private ByteBuf getBuffer(final NewWingLocationIterator iterator) {
        return getProperty(iterator, FIELD_NAME_ITERATOR_BUFFER, ByteBuf.class);
    }
}
