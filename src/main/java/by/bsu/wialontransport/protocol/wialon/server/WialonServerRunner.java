package by.bsu.wialontransport.protocol.wialon.server;

import by.bsu.wialontransport.protocol.wialon.server.factory.WialonServerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public final class WialonServerRunner {
    private final WialonServerFactory serverFactory;

    @PostConstruct
    public void run() {
        final WialonServer server = this.serverFactory.create();
        run(server);
    }

    private static void run(final WialonServer server) {
        final Thread threadRunningServer = new Thread(server::run);
        threadRunningServer.start();
    }
}
