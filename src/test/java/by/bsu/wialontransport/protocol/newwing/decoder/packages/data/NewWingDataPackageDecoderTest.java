package by.bsu.wialontransport.protocol.newwing.decoder.packages.data;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingDataPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingRequestPackage;
import by.bsu.wialontransport.util.CollectionUtil;
import io.netty.buffer.ByteBuf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.List;

import static by.bsu.wialontransport.util.CollectionUtil.collectToList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class NewWingDataPackageDecoderTest {
    private static final String REQUEST_FACTORY_CREATE_METHOD_NAME = "create";

    @Mock
    private NewWingDataIteratorFactory mockedDataIteratorFactory;

    private NewWingDataPackageDecoder decoder;

    @Before
    public void initializeDecoder() {
        decoder = new NewWingDataPackageDecoder(mockedDataIteratorFactory);
    }

    @Test
    public void bufferShouldBeDecodedUntilChecksum()
            throws Exception {
        try (final MockedStatic<CollectionUtil> mockedCollectionUtil = mockStatic(CollectionUtil.class)) {
            final ByteBuf givenBuffer = mock(ByteBuf.class);

            final NewWingDataIterator givenIterator = mock(NewWingDataIterator.class);
            when(mockedDataIteratorFactory.create(same(givenBuffer))).thenReturn(givenIterator);

            final List<NewWingData> givenData = List.of(
                    createData(23, 11, 2),
                    createData(23, 11, 3),
                    createData(23, 11, 4)
            );
            mockedCollectionUtil.when(() -> collectToList(same(givenIterator))).thenReturn(givenData);

            final Object requestFactory = decoder.decodeUntilChecksum(givenBuffer);

            final int givenChecksum = 53444546;
            final NewWingRequestPackage actual = createRequest(requestFactory, givenChecksum);
            final NewWingRequestPackage expected = new NewWingDataPackage(givenChecksum, givenData);
            assertEquals(expected, actual);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private NewWingData createData(final int year, final int month, final int day) {
        return NewWingData.builder()
                .year((byte) year)
                .month((byte) month)
                .day((byte) day)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private NewWingRequestPackage createRequest(final Object factory, final int checksum)
            throws Exception {
        final Method method = factory.getClass().getMethod(REQUEST_FACTORY_CREATE_METHOD_NAME, int.class);
        return (NewWingRequestPackage) method.invoke(factory, checksum);
    }
}
