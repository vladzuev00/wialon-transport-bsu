package by.vladzuev.locationreceiver.protocol.core.exceptionhandler;

import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import io.netty.channel.ChannelHandlerContext;
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
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ProtocolExceptionHandlerTest {
    private final LogCaptor logCaptor = forClass(ProtocolExceptionHandler.class);

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    private ProtocolExceptionHandler handler;

    @BeforeEach
    public void initializeHandler() {
        handler = new ProtocolExceptionHandler(mockedContextAttributeManager);
    }

    @Test
    public void exceptionShouldBeHandledWithDefinedTrackerImei() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Throwable givenException = new RuntimeException();

        final String givenTrackerImei = "11112222333344445555";
        when(mockedContextAttributeManager.findTrackerImei(same(givenContext)))
                .thenReturn(Optional.of(givenTrackerImei));

        handler.exceptionCaught(givenContext, givenException);

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of("Communication with tracker '11112222333344445555' arose exception");
        assertEquals(expectedLogs, actualLogs);

        verify(givenContext, times(1)).close();
    }

    @Test
    public void exceptionShouldBeHandledWithNotDefinedTrackerImei() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Throwable givenException = new RuntimeException();

        when(mockedContextAttributeManager.findTrackerImei(same(givenContext))).thenReturn(empty());

        handler.exceptionCaught(givenContext, givenException);

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of("Communication with unknown tracker arose exception");
        assertEquals(expectedLogs, actualLogs);

        verify(givenContext, times(1)).close();
    }
}
