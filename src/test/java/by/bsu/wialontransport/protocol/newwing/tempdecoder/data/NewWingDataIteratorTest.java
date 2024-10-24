package by.bsu.wialontransport.protocol.newwing.tempdecoder.data;

import by.bsu.wialontransport.crud.dto.Location;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.apache.commons.collections4.IteratorUtils.toList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class NewWingDataIteratorTest {

    @Test
    public void bufferShouldBeIterated() {
        final NewWingDataDecoder givenDecoder = mock(NewWingDataDecoder.class);
        final ByteBuf givenBuffer = mock(ByteBuf.class);
        final NewWingDataIterator givenIterator = new NewWingDataIterator(givenDecoder, givenBuffer);

        when(givenBuffer.readableBytes())
                .thenReturn(116)
                .thenReturn(116)
                .thenReturn(79)
                .thenReturn(79)
                .thenReturn(42)
                .thenReturn(42)
                .thenReturn(5);

        final Location firstGivenData = createData(255L);
        final Location secondGivenData = createData(256L);
        final Location thirdGivenData = createData(257L);
        when(givenDecoder.decodeNext(same(givenBuffer)))
                .thenReturn(firstGivenData)
                .thenReturn(secondGivenData)
                .thenReturn(thirdGivenData);

        final List<Location> actual = toList(givenIterator);
        final List<Location> expected = List.of(firstGivenData, secondGivenData, thirdGivenData);
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

    private Location createData(final Long id) {
        return Location.builder()
                .id(id)
                .build();
    }
}
