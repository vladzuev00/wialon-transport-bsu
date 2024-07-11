//package by.bsu.wialontransport.protocol.newwing.decoder.data;
//
//import io.netty.buffer.ByteBuf;
//import org.junit.Test;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//
//import static org.apache.commons.collections4.IteratorUtils.toList;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public final class NewWingDataIteratorTest {
//
//    @Test
//    public void bufferShouldBeIterated() {
//        final NewWingDataDecoder givenDecoder = mock(NewWingDataDecoder.class);
//        final ByteBuf givenBuffer = mock(ByteBuf.class);
//        final NewWingDataIterator givenIterator = new NewWingDataIterator(givenDecoder, givenBuffer);
//
//        when(givenBuffer.readableBytes())
//                .thenReturn(116)
//                .thenReturn(116)
//                .thenReturn(79)
//                .thenReturn(79)
//                .thenReturn(42)
//                .thenReturn(42)
//                .thenReturn(5);
//
//        final NewWingData firstGivenData = createData(2, 9, 20);
//        final NewWingData secondGivenData = createData(3, 10, 21);
//        final NewWingData thirdGivenData = createData(4, 11, 22);
//        when(givenDecoder.decodeNext(same(givenBuffer)))
//                .thenReturn(firstGivenData)
//                .thenReturn(secondGivenData)
//                .thenReturn(thirdGivenData);
//
//        final List<NewWingData> actual = toList(givenIterator);
//        final List<NewWingData> expected = List.of(firstGivenData, secondGivenData, thirdGivenData);
//        assertEquals(expected, actual);
//    }
//
//    @Test(expected = NoSuchElementException.class)
//    public void bufferShouldNotBeIteratedBecauseOfNoSuchElementException() {
//        final NewWingDataDecoder givenDataDecoder = mock(NewWingDataDecoder.class);
//        final ByteBuf givenBuffer = mock(ByteBuf.class);
//        final NewWingDataIterator givenIterator = new NewWingDataIterator(givenDataDecoder, givenBuffer);
//
//        when(givenBuffer.isReadable()).thenReturn(false);
//
//        givenIterator.next();
//    }
//
//    private NewWingData createData(final int day, final int month, final int year) {
//        return NewWingData.builder()
//                .day((byte) day)
//                .month((byte) month)
//                .year((byte) year)
//                .build();
//    }
//}
