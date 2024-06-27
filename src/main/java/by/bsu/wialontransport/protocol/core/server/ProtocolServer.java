package by.bsu.wialontransport.protocol.core.server;

import io.netty.bootstrap.ServerBootstrap;
import lombok.RequiredArgsConstructor;

import static java.lang.Thread.currentThread;

@RequiredArgsConstructor
public final class ProtocolServer {
    private final ServerBootstrap bootstrap;

    public void run() {
        try {
            bootstrap.bind()
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } catch (final InterruptedException cause) {
            currentThread().interrupt();
        }
    }
}
