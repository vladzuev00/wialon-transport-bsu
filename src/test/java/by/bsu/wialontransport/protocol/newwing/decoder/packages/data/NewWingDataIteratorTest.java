package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static by.bsu.wialontransport.util.CollectionUtil.convertToList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class NewWingDataIteratorTest {

    @Test
    public void bufferShouldBeIterated() {
        final NewWingDataDecoder givenDataDecoder = mock(NewWingDataDecoder.class);
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        final NewWingDataIterator givenIterator = new NewWingDataIterator(givenDataDecoder, givenBuffer);

        when(givenBuffer.isReadable())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        final NewWingData firstGivenData = createData(2, 9, 20);
        final NewWingData secondGivenData = createData(3, 10, 21);
        final NewWingData thirdGivenData = createData(4, 11, 22);
        when(givenDataDecoder.decodeNext(same(givenBuffer)))
                .thenReturn(firstGivenData)
                .thenReturn(secondGivenData)
                .thenReturn(thirdGivenData);

        final List<NewWingData> actual = convertToList(givenIterator);
        final List<NewWingData> expected = List.of(firstGivenData, secondGivenData, thirdGivenData);
        assertEquals(expected, actual);
    }

    @Test(expected = NoSuchElementException.class)
    public void bufferShouldNotBeIteratedBecauseOfNoSuchElementException() {
        final NewWingDataDecoder givenDataDecoder = mock(NewWingDataDecoder.class);
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        final NewWingDataIterator givenIterator = new NewWingDataIterator(givenDataDecoder, givenBuffer);

        when(givenBuffer.isReadable()).thenReturn(false);

        givenIterator.next();
    }

    private static NewWingData createData(final int day, final int month, final int year) {
        return NewWingData.builder()
                .day((byte) day)
                .month((byte) month)
                .year((byte) year)
                .build();
    }
}
