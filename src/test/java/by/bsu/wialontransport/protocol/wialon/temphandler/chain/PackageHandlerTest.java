package by.bsu.wialontransport.protocol.wialon.temphandler.chain;

import by.bsu.wialontransport.protocol.wialon.temphandler.chain.exception.NoSuitablePackageHandlerException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class PackageHandlerTest {
    private static final String FIELD_NAME_PACKAGE_TYPE = "packageType";
    private static final String FIELD_NAME_NEXT_HANDLER = "nextHandler";

    @Mock
    private PackageHandler mockedNextHandler;

    @Mock
    private Marker mockedMarker;

    @Captor
    private ArgumentCaptor<WialonPackage> packageArgumentCaptor;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    private PackageHandler packageHandler;

    @Before
    public void initializePackageHandler() {
        this.packageHandler = new PackageHandler(HandledPackage.class, this.mockedNextHandler) {
            private final Marker marker = mockedMarker;

            @Override
            protected void handleIndependently(final WialonPackage requestPackage, final ChannelHandlerContext context) {
                this.marker.mark();
            }
        };
    }

    @Test
    public void handlerShouldHandlePackageIndependently() {
        final HandledPackage givenPackage = new HandledPackage();
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.packageHandler.handle(givenPackage, givenContext);

        verify(this.mockedMarker, times(1)).mark();
        verify(this.mockedNextHandler, times(0))
                .handle(any(WialonPackage.class), any(ChannelHandlerContext.class));
    }

    @Test
    public void handlerShouldDelegateHandlingPackageToNextHandlerBecauseOfNotSuitableType() {
        final WialonPackage givenPackage = new WialonPackage() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.packageHandler.handle(givenPackage, givenContext);

        verify(this.mockedMarker, times(0)).mark();
        verify(this.mockedNextHandler, times(1))
                .handle(this.packageArgumentCaptor.capture(), this.contextArgumentCaptor.capture());
        assertSame(givenPackage, this.packageArgumentCaptor.getValue());
        assertSame(givenContext, this.contextArgumentCaptor.getValue());
    }

    @Test
    public void handlerShouldDelegateHandlingPackageToNextHandlerBecauseOfHandledPackageTypeIsNull()
            throws Exception {
        setNullInField(this.packageHandler, FIELD_NAME_PACKAGE_TYPE);

        final HandledPackage givenPackage = new HandledPackage();
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.packageHandler.handle(givenPackage, givenContext);

        verify(this.mockedMarker, times(0)).mark();
        verify(this.mockedNextHandler, times(1))
                .handle(this.packageArgumentCaptor.capture(), this.contextArgumentCaptor.capture());
        assertSame(givenPackage, this.packageArgumentCaptor.getValue());
        assertSame(givenContext, this.contextArgumentCaptor.getValue());
    }

    @Test(expected = NoSuitablePackageHandlerException.class)
    public void packageShouldNotBeHandledBecauseOfNotSuitableTypeAndNextHandlerIsNull()
            throws Exception {
        setNullInField(this.packageHandler, FIELD_NAME_NEXT_HANDLER);

        final WialonPackage givenPackage = new WialonPackage() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.packageHandler.handle(givenPackage, givenContext);
    }

    @SuppressWarnings("all")
    private static final class HandledPackage implements WialonPackage {

    }

    @SuppressWarnings("all")
    private static final class Marker {
        public void mark() {

        }
    }

    private static void setNullInField(PackageHandler packageHandler, String fieldName)
            throws Exception {
        final Field field = PackageHandler.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        try {
            field.set(packageHandler, null);
        } finally {
            field.setAccessible(false);
        }
    }
}
