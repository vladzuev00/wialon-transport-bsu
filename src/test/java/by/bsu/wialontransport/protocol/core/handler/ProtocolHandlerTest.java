package by.bsu.wialontransport.protocol.core.handler;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.contextmanager.ChannelHandlerContextManager;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.Value;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static nl.altindag.log.LogCaptor.forClass;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ProtocolHandlerTest {
    private final LogCaptor logCaptor = forClass(ProtocolHandler.class);

    @Mock
    private PackageHandler<Object> firstMockedPackageHandler;

    @Mock
    private PackageHandler<Object> secondMockedPackageHandler;

    @Mock
    private PackageHandler<Object> thirdMockedPackageHandler;

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    @Mock
    private ChannelHandlerContextManager mockedContextManager;

    private ProtocolHandler protocolHandler;

    @BeforeEach
    public void initializeProtocolHandler() {
        protocolHandler = new ProtocolHandler(
                List.of(firstMockedPackageHandler, secondMockedPackageHandler, thirdMockedPackageHandler),
                mockedContextAttributeManager,
                mockedContextManager
        );
    }

    @Test
    public void channelShouldBeRead() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Object givenRequest = new TestPackage("test-message");

        when(firstMockedPackageHandler.isAbleHandle(same(givenRequest))).thenReturn(false);
        when(secondMockedPackageHandler.isAbleHandle(same(givenRequest))).thenReturn(true);

        protocolHandler.channelRead(givenContext, givenRequest);

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of("Start handling request: 'ProtocolHandlerTest.TestPackage(message=test-message)'");
        assertEquals(expectedLogs, actualLogs);

        verify(secondMockedPackageHandler, times(1)).handle(same(givenRequest), same(givenContext));
        verify(firstMockedPackageHandler, times(0)).handle(any(Object.class), any(ChannelHandlerContext.class));
        verifyNoInteractions(thirdMockedPackageHandler);
    }

    @Test
    public void channelShouldNotBeRead() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Object givenRequest = new TestPackage("test-message");

        when(firstMockedPackageHandler.isAbleHandle(same(givenRequest))).thenReturn(false);
        when(secondMockedPackageHandler.isAbleHandle(same(givenRequest))).thenReturn(false);
        when(thirdMockedPackageHandler.isAbleHandle(same(givenRequest))).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> protocolHandler.channelRead(givenContext, givenRequest));

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of("Start handling request: 'ProtocolHandlerTest.TestPackage(message=test-message)'");
        assertEquals(expectedLogs, actualLogs);

        verify(firstMockedPackageHandler, times(0)).handle(any(Object.class), any(ChannelHandlerContext.class));
        verify(secondMockedPackageHandler, times(0)).handle(any(Object.class), any(ChannelHandlerContext.class));
        verify(thirdMockedPackageHandler, times(0)).handle(any(Object.class), any(ChannelHandlerContext.class));
    }

    @Test
    public void channelActivatingShouldBeHandling() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        protocolHandler.channelActive(givenContext);

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of("New tracker is connected");
        assertEquals(expectedLogs, actualLogs);
    }

    @Test
    public void channelInactivatingShouldBeHandlingWithRemovingContext() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Long givenTrackerId = 255L;
        final Tracker givenTracker = Tracker.builder().id(givenTrackerId).build();
        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(Optional.of(givenTracker));

        protocolHandler.channelInactive(givenContext);

        verify(mockedContextManager, times(1)).remove(same(givenTrackerId));
    }

    @Test
    public void channelInactivatingShouldBeHandlingWithoutRemovingContext() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        when(mockedContextAttributeManager.findTracker(same(givenContext))).thenReturn(empty());

        protocolHandler.channelInactive(givenContext);

        verifyNoInteractions(mockedContextManager);
    }

    @Value
    private static class TestPackage {
        String message;
    }
}
