package by.bsu.wialontransport.protocol.core.handler;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.exception.AnsweredException;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler.NoSuitablePackageHandlerException;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.protocolpackage.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import lombok.Value;
import nl.altindag.log.LogCaptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static nl.altindag.log.LogCaptor.forClass;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ProtocolHandlerTest {
    private final LogCaptor logCaptor = forClass(ProtocolHandler.class);

    @Mock
    private PackageHandler<TestPackage> firstMockedPackageHandler;

    @Mock
    private PackageHandler<TestPackage> secondMockedPackageHandler;

    @Mock
    private PackageHandler<TestPackage> thirdMockedPackageHandler;

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    @Mock
    private ConnectionManager mockedConnectionManager;

    private ProtocolHandler<?> protocolHandler;

    @Before
    public void initializeProtocolHandler() {
        this.protocolHandler = new TestProtocolHandler(
                List.of(
                        this.firstMockedPackageHandler,
                        this.secondMockedPackageHandler,
                        this.thirdMockedPackageHandler
                ),
                this.mockedContextAttributeManager,
                this.mockedConnectionManager
        );
    }

    @Test
    public void channelShouldBeRead() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Package givenRequestPackage = new TestPackage(255L);

        when(this.firstMockedPackageHandler.isAbleToHandle(same(givenRequestPackage))).thenReturn(false);
        when(this.secondMockedPackageHandler.isAbleToHandle(same(givenRequestPackage))).thenReturn(true);

        this.protocolHandler.channelRead(givenContext, givenRequestPackage);

        final List<String> actualLogs = this.logCaptor.getLogs();
        final List<String> expectedLogs = List.of(
                "Start handling request package: 'ProtocolHandlerTest.TestPackage(id=255)'"
        );
        assertEquals(expectedLogs, actualLogs);

        verifyNoInteractions(this.thirdMockedPackageHandler);
        verify(this.secondMockedPackageHandler, times(1)).handle(
                same(givenRequestPackage),
                same(givenContext)
        );
    }

    @Test(expected = ClassCastException.class)
    public void channelShouldNotBeReadBecauseOfRequestObjectIsNotPackage() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Object givenRequestObject = new Object();

        this.protocolHandler.channelRead(givenContext, givenRequestObject);
    }

    @Test(expected = NoSuitablePackageHandlerException.class)
    public void channelShouldNotBeReadBecauseOfNoSuitablePackageHandler() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Package givenRequestPackage = new TestPackage(255L);

        when(this.firstMockedPackageHandler.isAbleToHandle(same(givenRequestPackage))).thenReturn(false);
        when(this.secondMockedPackageHandler.isAbleToHandle(same(givenRequestPackage))).thenReturn(false);
        when(this.thirdMockedPackageHandler.isAbleToHandle(same(givenRequestPackage))).thenReturn(false);

        this.protocolHandler.channelRead(givenContext, givenRequestPackage);
    }

    @Test
    public void answeredExceptionWrappedByDecoderExceptionShouldBeCaught() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Package givenAnswer = mock(Package.class);
        final AnsweredException givenAnsweredException = new AnsweredException(givenAnswer);
        final DecoderException givenDecoderException = new DecoderException(givenAnsweredException);

        this.protocolHandler.exceptionCaught(givenContext, givenDecoderException);

        verify(givenContext, times(1)).writeAndFlush(same(givenAnswer));
        verifyNoMoreInteractions(givenContext);
    }

    @Test
    public void answeredExceptionShouldBeCaught() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Package givenAnswer = mock(Package.class);
        final AnsweredException givenAnsweredException = new AnsweredException(givenAnswer);

        this.protocolHandler.exceptionCaught(givenContext, givenAnsweredException);

        verify(givenContext, times(1)).writeAndFlush(same(givenAnswer));
        verifyNoMoreInteractions(givenContext);
    }

    @Test
    public void exceptionWrappedByDecoderExceptionShouldBeCaught() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Exception givenException = new Exception();
        final DecoderException givenDecoderException = new DecoderException(givenException);

        this.protocolHandler.exceptionCaught(givenContext, givenDecoderException);

        verify(givenContext, times(1)).fireExceptionCaught(same(givenException));
        verifyNoMoreInteractions(givenContext);
    }

    @Test
    public void exceptionShouldBeCaught() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Exception givenException = new Exception();

        this.protocolHandler.exceptionCaught(givenContext, givenException);

        verify(givenContext, times(1)).fireExceptionCaught(same(givenException));
        verifyNoMoreInteractions(givenContext);
    }

    @Test
    public void activatingChannelShouldBeHandled() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.protocolHandler.channelActive(givenContext);

        final List<String> actualLogs = this.logCaptor.getLogs();
        final List<String> expectedLogs = List.of("New tracker is connected");
        assertEquals(expectedLogs, actualLogs);

        verifyNoInteractions(givenContext);
    }

    @Test
    public void inactivatingChannelShouldBeHandledInCaseExistingTrackerImeiInContextAndAuthorizedTracker() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final String givenTrackerImei = "11112222333344445555";
        when(this.mockedContextAttributeManager.findTrackerImei(same(givenContext))).thenReturn(
                Optional.of(givenTrackerImei)
        );

        final Long givenTrackerId = 255L;
        final Tracker givenTracker = createTracker(givenTrackerId);
        when(this.mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.of(givenTracker));

        this.protocolHandler.channelInactive(givenContext);

        final List<String> actualLogs = this.logCaptor.getLogs();
        final List<String> expectedLogs = List.of("Tracker with imei '11112222333344445555' was disconnected");
        assertEquals(expectedLogs, actualLogs);

        verify(this.mockedConnectionManager, times(1)).remove(same(givenTrackerId));
        verifyNoMoreInteractions(this.mockedConnectionManager);
        verifyNoInteractions(givenContext);
    }

    @Test
    public void inactivatingChannelShouldBeHandledInCaseNotExistingTrackerImeiInContextAndNotAuthorizedTracker() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        when(this.mockedContextAttributeManager.findTrackerImei(same(givenContext))).thenReturn(empty());
        when(this.mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(empty());

        this.protocolHandler.channelInactive(givenContext);

        final List<String> actualLogs = this.logCaptor.getLogs();
        final List<String> expectedLogs = List.of("Tracker with imei 'not defined imei' was disconnected");
        assertEquals(expectedLogs, actualLogs);

        verifyNoInteractions(this.mockedConnectionManager);
        verifyNoInteractions(givenContext);
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    @Value
    private static class TestPackage implements Package {
        long id;
    }

    private static final class TestProtocolHandler extends ProtocolHandler<PackageHandler<TestPackage>> {

        public TestProtocolHandler(final List<PackageHandler<TestPackage>> packageHandlers,
                                   final ContextAttributeManager contextAttributeManager,
                                   final ConnectionManager connectionManager) {
            super(packageHandlers, contextAttributeManager, connectionManager);
        }

    }
}
