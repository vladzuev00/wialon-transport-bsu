package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import io.netty.buffer.ByteBuf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static by.bsu.wialontransport.util.ReflectionUtil.getProperty;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public final class NewWingDataIteratorFactoryTest {
    private static final String FIELD_NAME_DECODER = "decoder";
    private static final String FIELD_NAME_BUFFER = "buffer";

    @Mock
    private NewWingDataDecoder mockedDecoder;

    private NewWingDataIteratorFactory factory;

    @Before
    public void initializeFactory() {
        factory = new NewWingDataIteratorFactory(mockedDecoder);
    }

    @Test
    public void iteratorShouldBeCreated() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final NewWingDataIterator actual = factory.create(givenBuffer);

        final NewWingDataDecoder actualDecoder = getDataDecoder(actual);
        assertSame(mockedDecoder, actualDecoder);

        final ByteBuf actualBuffer = getBuffer(actual);
        assertSame(givenBuffer, actualBuffer);
    }

    private NewWingDataDecoder getDataDecoder(final NewWingDataIterator iterator) {
        return getProperty(iterator, FIELD_NAME_DECODER, NewWingDataDecoder.class);
    }

    private ByteBuf getBuffer(final NewWingDataIterator iterator) {
        return getProperty(iterator, FIELD_NAME_BUFFER, ByteBuf.class);
    }
}