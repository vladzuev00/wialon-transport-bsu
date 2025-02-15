package by.vladzuev.locationreceiver.protocol.core.serverrunner;

import by.vladzuev.locationreceiver.protocol.core.contextattributemanager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.model.ProtocolServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public final class ProtocolServerRunnerTest {

    @Mock
    private ProtocolServer firstMockedServer;

    @Mock
    private ProtocolServer secondMockedServer;

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    private ProtocolServerRunner runner;

    @BeforeEach
    public void initializeRunner() {
        runner = new ProtocolServerRunner(List.of(firstMockedServer, secondMockedServer), mockedContextAttributeManager);
    }

    @Test
    public void serversShouldBeRun() {
        runner.run();
    }
}
