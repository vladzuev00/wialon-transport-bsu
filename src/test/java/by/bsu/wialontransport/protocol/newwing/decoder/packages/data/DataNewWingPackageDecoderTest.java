package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.builder.DataNewWingPackageBuilder;
import by.bsu.wialontransport.util.CollectionUtil;
import io.netty.buffer.ByteBuf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static by.bsu.wialontransport.util.CollectionUtil.convertToList;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class DataNewWingPackageDecoderTest {

    @Mock
    private NewWingDataIteratorFactory mockedDataIteratorFactory;

    private DataNewWingPackageDecoder decoder;

    @Before
    public void initializeDecoder() {
        this.decoder = new DataNewWingPackageDecoder(this.mockedDataIteratorFactory);
    }

    @Test
    public void bufferShouldBeDecodedUntilChecksum() {
        try (final MockedStatic<CollectionUtil> mockedCollectionUtil = mockStatic(CollectionUtil.class)) {
            final ByteBuf givenBuffer = mock(ByteBuf.class);
            final DataNewWingPackageBuilder givenPackageBuilder = mock(DataNewWingPackageBuilder.class);

            final NewWingDataIterator givenDataIterator = mock(NewWingDataIterator.class);
            when(this.mockedDataIteratorFactory.create(same(givenBuffer))).thenReturn(givenDataIterator);

            final List<NewWingData> givenData = List.of(
                    createData(23, 11, 2),
                    createData(23, 11, 3),
                    createData(23, 11, 4)
            );
            mockedCollectionUtil.when(() -> convertToList(same(givenDataIterator))).thenReturn(givenData);

            this.decoder.decodeUntilChecksum(givenBuffer, givenPackageBuilder);

            verify(givenPackageBuilder, times(1)).setData(eq(givenData));
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static NewWingData createData(final int year, final int month, final int day) {
        return NewWingData.builder()
                .year((byte) year)
                .month((byte) month)
                .day((byte) day)
                .build();
    }
}
