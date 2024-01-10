package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import io.netty.buffer.ByteBuf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static by.bsu.wialontransport.util.ReflectionUtil.findProperty;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public final class NewWingDataIteratorFactoryTest {
    private static final String FIELD_NAME_DATA_DECODER = "dataDecoder";
    private static final String FIELD_NAME_BUFFER = "buffer";

    @Mock
    private NewWingDataDecoder mockedDataDecoder;

    private NewWingDataIteratorFactory iteratorFactory;

    @Before
    public void initializeIteratorFactory() {
        iteratorFactory = new NewWingDataIteratorFactory(mockedDataDecoder);
    }

    @Test
    public void iteratorShouldBeCreated() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final NewWingDataIterator actual = iteratorFactory.create(givenBuffer);

        final NewWingDataDecoder actualDataDecoder = findDataDecoder(actual);
        assertSame(mockedDataDecoder, actualDataDecoder);

        final ByteBuf actualBuffer = findBuffer(actual);
        assertSame(givenBuffer, actualBuffer);
    }

    private static NewWingDataDecoder findDataDecoder(final NewWingDataIterator iterator) {
        return findProperty(
                iterator,
                FIELD_NAME_DATA_DECODER,
                NewWingDataDecoder.class
        );
    }

    public static ByteBuf findBuffer(final NewWingDataIterator iterator) {
        return findProperty(
                iterator,
                FIELD_NAME_BUFFER,
                ByteBuf.class
        );
    }
}