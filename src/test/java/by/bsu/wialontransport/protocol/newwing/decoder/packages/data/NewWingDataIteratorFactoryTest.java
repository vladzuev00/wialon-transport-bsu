package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import by.bsu.wialontransport.protocol.newwing.model.CurrentFrameEventCount;
import io.netty.buffer.ByteBuf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.OptionalInt;

import static by.bsu.wialontransport.util.ReflectionUtil.findProperty;
import static java.util.OptionalInt.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class NewWingDataIteratorFactoryTest {
    private static final String FIELD_NAME_DATA_DECODER = "dataDecoder";
    private static final String FIELD_NAME_BUFFER = "buffer";
    private static final String FIELD_NAME_DATA_COUNT = "dataCount";

    @Mock
    private CurrentFrameEventCount mockedCurrentFrameEventCount;

    @Mock
    private NewWingDataDecoder mockedDataDecoder;

    private NewWingDataIteratorFactory iteratorFactory;

    @Before
    public void initializeIteratorFactory() {
        this.iteratorFactory = new NewWingDataIteratorFactory(
                this.mockedCurrentFrameEventCount,
                this.mockedDataDecoder
        );
    }

    @Test
    public void iteratorShouldBeCreated() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        final int givenCurrentFrameEventCount = 5;
        when(this.mockedCurrentFrameEventCount.takeValue()).thenReturn(OptionalInt.of(givenCurrentFrameEventCount));

        final NewWingDataIterator actual = this.iteratorFactory.create(givenBuffer);

        final NewWingDataDecoder actualDataDecoder = findDataDecoder(actual);
        assertSame(this.mockedDataDecoder, actualDataDecoder);

        final ByteBuf actualBuffer = findBuffer(actual);
        assertSame(givenBuffer, actualBuffer);

        final int actualDataCount = findDataCount(actual);
        assertEquals(givenCurrentFrameEventCount, actualDataCount);
    }

    @Test(expected = RuntimeException.class)
    public void iteratorShouldNotBeCreatedBecauseOfNoCurrentFrameEventCount() {
        final ByteBuf givenBuffer = mock(ByteBuf.class);

        when(this.mockedCurrentFrameEventCount.takeValue()).thenReturn(empty());

        this.iteratorFactory.create(givenBuffer);
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

    public static int findDataCount(final NewWingDataIterator iterator) {
        return findProperty(
                iterator,
                FIELD_NAME_DATA_COUNT,
                Integer.class
        );
    }
}