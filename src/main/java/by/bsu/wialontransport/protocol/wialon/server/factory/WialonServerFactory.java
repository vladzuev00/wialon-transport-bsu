package by.bsu.wialontransport.protocol.wialon.server.factory;

import by.bsu.wialontransport.configuration.property.WialonServerConfiguration;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.wialon.tempdecoder.WialonDecoder;
import by.bsu.wialontransport.protocol.wialon.tempdecoder.chain.StarterPackageDecoder;
import by.bsu.wialontransport.protocol.wialon.encoder.WialonEncoder;
import by.bsu.wialontransport.protocol.wialon.encoder.chain.StarterPackageEncoder;
import by.bsu.wialontransport.protocol.wialon.handler.WialonExceptionHandler;
import by.bsu.wialontransport.protocol.wialon.handler.WialonHandler;
import by.bsu.wialontransport.protocol.wialon.handler.chain.StarterPackageHandler;
import by.bsu.wialontransport.protocol.wialon.server.WialonServer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@RequiredArgsConstructor
public final class WialonServerFactory {
    private final WialonServerConfiguration serverConfiguration;
    private final StarterPackageDecoder starterPackageDecoder;
    private final StarterPackageEncoder starterPackageEncoder;
    private final StarterPackageHandler starterPackageHandler;
    private final ContextAttributeManager contextAttributeManager;
    private final ConnectionManager connectionManager;

    public WialonServer create() {
        return new WialonServer(
                this.createConnectionProcessLoopGroup(),
                this.createDataProcessLoopGroup(),
                this.serverConfiguration.getPort(),
                this.createDecoderFactory(),
                this.createReadTimeoutHandlerFactory(),
                this.createEncoderFactory(),
                this.createHandlerFactory(),
                this.createExceptionHandlerFactory()
        );
    }

    private EventLoopGroup createConnectionProcessLoopGroup() {
        final int amountThreadsToProcessConnection = this.serverConfiguration.getAmountThreadsToProcessData();
        return new NioEventLoopGroup(amountThreadsToProcessConnection);
    }

    private EventLoopGroup createDataProcessLoopGroup() {
        final int amountThreadsToProcessData = this.serverConfiguration.getAmountThreadsToProcessData();
        return new NioEventLoopGroup(amountThreadsToProcessData);
    }

    private Supplier<WialonDecoder> createDecoderFactory() {
        return () -> new WialonDecoder(this.starterPackageDecoder);
    }

    private Supplier<ReadTimeoutHandler> createReadTimeoutHandlerFactory() {
        final int aliveConnectionTimeoutSeconds = this.serverConfiguration.getAliveConnectionTimeoutSeconds();
        return () -> new ReadTimeoutHandler(aliveConnectionTimeoutSeconds, SECONDS);
    }

    private Supplier<WialonEncoder> createEncoderFactory() {
        return () -> new WialonEncoder(this.starterPackageEncoder);
    }

    private Supplier<WialonHandler> createHandlerFactory() {
        return () -> new WialonHandler(
                this.starterPackageHandler,
                this.contextAttributeManager,
                this.connectionManager
        );
    }

    private Supplier<WialonExceptionHandler> createExceptionHandlerFactory() {
        return () -> new WialonExceptionHandler(this.contextAttributeManager);
    }
}
