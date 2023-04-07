package by.bsu.wialontransport.protocol.wialon.handler;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
import by.bsu.wialontransport.protocol.wialon.handler.chain.StarterPackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
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
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class WialonHandlerTest {
    private static final String FIELD_NAME_LOGGER = "log";
    private static final String FIELD_NAME_THE_UNSAFE = "theUnsafe";

    private static final String TEMPLATE_MESSAGE_INACTIVE_CHANNEL = "Tracker with imei '%s' is disconnected.";
    private static final String NOT_DEFINED_TRACKER_IMEI = "not defined imei";

    @Mock
    private StarterPackageHandler mockedStarterPackageHandler;

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    @Mock
    private ConnectionManager mockedConnectionManager;

    @Mock
    private Logger mockedLogger;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    private WialonHandler handler;

    @Before
    public void initializeHandler() {
        this.handler = new WialonHandler(
                this.mockedStarterPackageHandler,
                this.mockedContextAttributeManager,
                this.mockedConnectionManager
        );
    }

    @Before
    public void injectMockedLogger()
            throws Exception {
        final Field unsafeField = Unsafe.class.getDeclaredField(FIELD_NAME_THE_UNSAFE);
        unsafeField.setAccessible(true);
        try {
            final Unsafe unsafe = (Unsafe) unsafeField.get(null);
            final Field ourField = WialonHandler.class.getDeclaredField(FIELD_NAME_LOGGER);
            final Object staticFieldBase = unsafe.staticFieldBase(ourField);
            final long staticFieldOffset = unsafe.staticFieldOffset(ourField);
            unsafe.putObject(staticFieldBase, staticFieldOffset, this.mockedLogger);
        } finally {
            unsafeField.setAccessible(true);
        }
    }

    @Test
    public void channelShouldBeRead() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Package givenPackage = mock(Package.class);

        this.handler.channelRead(givenContext, givenPackage);

        verify(this.mockedStarterPackageHandler, times(1))
                .handle(givenPackage, givenContext);
        verify(this.mockedLogger, times(1)).info(anyString());
    }

    @Test(expected = ClassCastException.class)
    public void channelShouldNotBeReadBecauseOfRequestObjectIsNotPackage() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Object givenRequestObject = new Object();

        this.handler.channelRead(givenContext, givenRequestObject);
    }

    @Test
    public void decoderAnswerableExceptionShouldBeCaught() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Package givenAnswer = new Package() {
        };
        final DecoderException givenDecoderException = createDecoderAnswerableException(givenAnswer);

        this.handler.exceptionCaught(givenContext, givenDecoderException);

        verifyAnswerWasSent(givenContext, givenAnswer);
    }

    @Test
    public void decoderNotAnswerableExceptionShouldBeCaught() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Throwable givenCauseOfDecoderException = mock(Throwable.class);
        final DecoderException givenDecoderException = new DecoderException(givenCauseOfDecoderException);

        this.handler.exceptionCaught(givenContext, givenDecoderException);

        verifyExceptionWasDeliveredToNextHandler(givenContext, givenCauseOfDecoderException);
    }

    @Test
    public void answerableExceptionShouldBeCaught() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Package givenAnswer = mock(Package.class);
        final AnswerableException givenException = new AnswerableException(givenAnswer);

        this.handler.exceptionCaught(givenContext, givenException);

        verifyAnswerWasSent(givenContext, givenAnswer);
    }

    @Test
    public void exceptionShouldBeCaught() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Throwable givenException = mock(Throwable.class);

        this.handler.exceptionCaught(givenContext, givenException);

        verifyExceptionWasDeliveredToNextHandler(givenContext, givenException);
    }

    @Test
    public void eventOfActivatingChannelShouldBeReacted() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.handler.channelActive(givenContext);

        verify(this.mockedLogger, times(1)).info(anyString());
    }

    @Test
    public void eventOfInactivatingChannelShouldBeReactedWithNotDefinedTrackerImei() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        when(this.mockedContextAttributeManager.findTrackerImei(givenContext))
                .thenReturn(empty());
        when(this.mockedContextAttributeManager.findTracker(givenContext))
                .thenReturn(empty());

        this.handler.channelInactive(givenContext);

        verify(this.mockedLogger, times(1))
                .info(this.stringArgumentCaptor.capture());
        verify(this.mockedConnectionManager, times(0)).remove(anyLong());

        final String expectedLoggedMessage = format(TEMPLATE_MESSAGE_INACTIVE_CHANNEL, NOT_DEFINED_TRACKER_IMEI);
        assertEquals(expectedLoggedMessage, this.stringArgumentCaptor.getValue());
    }

    @Test
    public void eventOfInactivatingChannelShouldBeReactedWithDefinedTrackerImeiButTrackerWasNotAuthorized() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final String givenTrackerImei = "11112222333344445555";

        when(this.mockedContextAttributeManager.findTrackerImei(givenContext))
                .thenReturn(Optional.of(givenTrackerImei));
        when(this.mockedContextAttributeManager.findTracker(givenContext))
                .thenReturn(empty());

        this.handler.channelInactive(givenContext);

        verify(this.mockedLogger, times(1))
                .info(this.stringArgumentCaptor.capture());
        verify(this.mockedConnectionManager, times(0)).remove(anyLong());

        final String expectedLoggedMessage = format(TEMPLATE_MESSAGE_INACTIVE_CHANNEL, givenTrackerImei);
        assertEquals(expectedLoggedMessage, this.stringArgumentCaptor.getValue());
    }

    @Test
    public void eventOfInactivatingChannelShouldBeReactedWithDefinedTrackerImeiAndTrackerWasAuthorized() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final String givenTrackerImei = "11112222333344445555";

        final Long givenTrackerId = 255L;
        final Tracker givenTracker = createTracker(givenTrackerId);

        when(this.mockedContextAttributeManager.findTrackerImei(givenContext))
                .thenReturn(Optional.of(givenTrackerImei));
        when(this.mockedContextAttributeManager.findTracker(givenContext))
                .thenReturn(Optional.of(givenTracker));

        this.handler.channelInactive(givenContext);

        verify(this.mockedLogger, times(1))
                .info(this.stringArgumentCaptor.capture());
        verify(this.mockedConnectionManager, times(1))
                .remove(givenTrackerId);

        final String expectedLoggedMessage = format(TEMPLATE_MESSAGE_INACTIVE_CHANNEL, givenTrackerImei);
        assertEquals(expectedLoggedMessage, this.stringArgumentCaptor.getValue());
    }

    private static DecoderException createDecoderAnswerableException(final Package answer) {
        final AnswerableException answerableException = new AnswerableException(answer);
        return new DecoderException(answerableException);
    }

    private static void verifyAnswerWasSent(final ChannelHandlerContext context, final Package answer) {
        verify(context, times(1)).writeAndFlush(answer);
        verify(context, times(0)).fireExceptionCaught(any(Throwable.class));
    }

    private static void verifyExceptionWasDeliveredToNextHandler(final ChannelHandlerContext context,
                                                                 final Throwable exception) {
        verify(context, times(0)).writeAndFlush(any());
        verify(context, times(1)).fireExceptionCaught(exception);
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }
}
