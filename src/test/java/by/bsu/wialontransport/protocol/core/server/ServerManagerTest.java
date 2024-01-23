package by.bsu.wialontransport.protocol.core.server;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class ServerManagerTest {

    @Mock
    private ProtocolServer firstMockedProtocolServer;

    @Mock
    private ProtocolServer secondMockedProtocolServer;

    @Mock
    private ProtocolServer thirdMockedProtocolServer;

    private ServerManager manager;

    @Before
    public void initializeManager() {
        manager = new ServerManager(
                List.of(
                        firstMockedProtocolServer,
                        secondMockedProtocolServer,
                        thirdMockedProtocolServer
                )
        );
    }

    @Test
    public void serversShouldBeRun()
            throws Exception {
        manager.runServers();
        SECONDS.sleep(1);

        verify(firstMockedProtocolServer, times(1)).run();
        verify(secondMockedProtocolServer, times(1)).run();
        verify(thirdMockedProtocolServer, times(1)).run();
    }

    @Test
    public void serversShouldBeStop() {
        manager.stopServers();

        verify(firstMockedProtocolServer, times(1)).stop();
        verify(secondMockedProtocolServer, times(1)).stop();
        verify(thirdMockedProtocolServer, times(1)).stop();
    }
}
