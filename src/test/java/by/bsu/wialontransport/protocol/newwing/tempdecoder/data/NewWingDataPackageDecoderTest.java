package by.bsu.wialontransport.protocol.newwing.tempdecoder.data;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingLocationPackage;
import io.netty.buffer.ByteBuf;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.List;

import static org.apache.commons.collections4.IteratorUtils.toList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class NewWingDataPackageDecoderTest {
    private static final String PACKAGE_FACTORY_CREATE_METHOD_NAME = "create";

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
        try (final MockedStatic<IteratorUtils> mockedIteratorUtil = mockStatic(IteratorUtils.class)) {
            final ByteBuf givenBuffer = mock(ByteBuf.class);

            final NewWingDataIterator givenIterator = mock(NewWingDataIterator.class);
            when(mockedDataIteratorFactory.create(same(givenBuffer))).thenReturn(givenIterator);

            final List<Location> givenData = List.of(createData(255L), createData(256L), createData(257L));
            mockedIteratorUtil.when(() -> toList(same(givenIterator))).thenReturn(givenData);

            final Object packageFactory = decoder.decodeUntilChecksum(givenBuffer);

            final int givenChecksum = 53444546;
            final NewWingRequestPackage actual = createPackage(packageFactory, givenChecksum);
            final NewWingRequestPackage expected = new NewWingLocationPackage(givenChecksum, givenData);
            assertEquals(expected, actual);
        }
    }

    private Location createData(final Long id) {
        return Location.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private NewWingRequestPackage createPackage(final Object factory, final int checksum)
            throws Exception {
        final Method method = factory.getClass().getMethod(PACKAGE_FACTORY_CREATE_METHOD_NAME, int.class);
        return (NewWingRequestPackage) method.invoke(factory, checksum);
    }
}
