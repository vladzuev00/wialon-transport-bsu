package by.bsu.wialontransport.protocol.wialon.server;

import by.bsu.wialontransport.protocol.wialon.server.factory.WialonServerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class WialonServerRunnerTest {

    private static final long SLEEPING_TIMEOUT_IN_MILLISECONDS_TO_WAIT_SERVER_TO_BE_RUN = 100;

    @Mock
    private WialonServerFactory mockedServerFactory;

    private WialonServerRunner serverRunner;

    @Before
    public void initializeServerRunner() {
        this.serverRunner = new WialonServerRunner(this.mockedServerFactory);
    }

    @Test
    public void serverShouldBeRun()
            throws Exception {
        final WialonServer givenServer = mock(WialonServer.class);
        when(this.mockedServerFactory.create()).thenReturn(givenServer);

        this.serverRunner.run();

        MILLISECONDS.sleep(SLEEPING_TIMEOUT_IN_MILLISECONDS_TO_WAIT_SERVER_TO_BE_RUN);

        verify(givenServer, times(1)).run();
    }
}
