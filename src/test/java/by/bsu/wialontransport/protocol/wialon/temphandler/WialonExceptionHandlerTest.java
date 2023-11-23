package by.bsu.wialontransport.protocol.wialon.temphandler;

import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class WialonExceptionHandlerTest {
    private static final String FIELD_NAME_LOGGER = "log";
    private static final String FIELD_NAME_THE_UNSAFE = "theUnsafe";
    private static final String IMEI_NOT_DEFINED_TRACKER = "not defined imei";

    @Mock
    private Logger mockedLogger;

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    private WialonExceptionHandler handler;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Before
    public void initializeHandler() {
        this.handler = new WialonExceptionHandler(this.mockedContextAttributeManager);
    }

    @Before
    public void injectMockedLogger()
            throws Exception {
        final Field unsafeField = Unsafe.class.getDeclaredField(FIELD_NAME_THE_UNSAFE);
        unsafeField.setAccessible(true);
        try {
            final Unsafe unsafe = (Unsafe) unsafeField.get(null);
            final Field ourField = WialonExceptionHandler.class.getDeclaredField(FIELD_NAME_LOGGER);
            final Object staticFieldBase = unsafe.staticFieldBase(ourField);
            final long staticFieldOffset = unsafe.staticFieldOffset(ourField);
            unsafe.putObject(staticFieldBase, staticFieldOffset, this.mockedLogger);
        } finally {
            unsafeField.setAccessible(false);
        }
    }

    @Test
    public void exceptionShouldBeCaughtAndLogMessageWithDefinedTrackerImei() {
        final String givenTrackerImei = "11111222223333344444";
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Throwable givenException = mock(Throwable.class);
        final String givenMessageException = "exception-message";
        when(givenException.getMessage()).thenReturn(givenMessageException);

        when(this.mockedContextAttributeManager.findTrackerImei(any(ChannelHandlerContext.class)))
                .thenReturn(Optional.of(givenTrackerImei));

        this.handler.exceptionCaught(givenContext, givenException);

        verify(this.mockedContextAttributeManager, times(1))
                .findTrackerImei(this.contextArgumentCaptor.capture());
        verify(this.mockedLogger, times(1))
                .error(anyString(), this.stringArgumentCaptor.capture(), this.stringArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
        assertEquals(List.of(givenTrackerImei, givenMessageException), this.stringArgumentCaptor.getAllValues());
    }

    @Test
    public void exceptionShouldBeCaughtAndLogMessageWithNotDefinedTrackerImei() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Throwable givenException = mock(Throwable.class);
        final String givenMessageException = "exception-message";
        when(givenException.getMessage()).thenReturn(givenMessageException);

        when(this.mockedContextAttributeManager.findTrackerImei(any(ChannelHandlerContext.class)))
                .thenReturn(empty());

        this.handler.exceptionCaught(givenContext, givenException);

        verify(this.mockedContextAttributeManager, times(1))
                .findTrackerImei(this.contextArgumentCaptor.capture());
        verify(this.mockedLogger, times(1))
                .error(anyString(), this.stringArgumentCaptor.capture(), this.stringArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
        assertEquals(
                List.of(IMEI_NOT_DEFINED_TRACKER, givenMessageException),
                this.stringArgumentCaptor.getAllValues()
        );
    }
}
