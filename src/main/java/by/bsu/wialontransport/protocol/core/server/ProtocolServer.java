package by.bsu.wialontransport.protocol.core.server;

import by.bsu.wialontransport.configuration.property.ProtocolServerConfiguration;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder;
import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.core.encoder.ProtocolEncoder;
import by.bsu.wialontransport.protocol.core.encoder.packages.PackageEncoder;
import by.bsu.wialontransport.protocol.core.exceptionhandler.ProtocolExceptionHandler;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;

public abstract class ProtocolServer {
    private final ProtocolServerConfiguration configuration;
    private final ProtocolHandlerCreatingContext handlerCreatingContext;

    public ProtocolServer(final ProtocolServerConfiguration configuration,
                          final ContextAttributeManager contextAttributeManager,
                          final ConnectionManager connectionManager,
                          final List<PackageDecoder<?, ?, ?>> packageDecoders,
                          final List<PackageEncoder<?>> packageEncoders,
                          final List<PackageHandler<?>> packageHandlers) {
        this.configuration = configuration;
        handlerCreatingContext = ProtocolHandlerCreatingContext.builder()
                .contextAttributeManager(contextAttributeManager)
                .connectionManager(connectionManager)
                .packageDecoders(packageDecoders)
                .packageEncoders(packageEncoders)
                .packageHandlers(packageHandlers)
                .build();
    }

    public final void run() {
        try {
            createServerBootstrap()
                    .bind()
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } catch (final InterruptedException cause) {
            currentThread().interrupt();
        }
    }

    public final void stop() {
        shutdown(configuration.getLoopGroupProcessingConnection());
        shutdown(configuration.getLoopGroupProcessingData());
    }

    protected abstract ProtocolDecoder<?, ?> createDecoder();

    protected abstract ProtocolEncoder createEncoder();

    protected abstract ProtocolHandler createHandler(final ProtocolHandlerCreatingContext context);

    private ServerBootstrap createServerBootstrap() {
        return new ServerBootstrap()
                .group(configuration.getLoopGroupProcessingConnection(), configuration.getLoopGroupProcessingData())
                .channel(NioServerSocketChannel.class)
                .localAddress(configuration.getInetSocketAddress())
                .childHandler(createChannelInitializer());
    }

    private ChannelInitializer<SocketChannel> createChannelInitializer() {
        return new ChannelInitializer<>() {

            @Override
            public void initChannel(final SocketChannel channel) {
                channel.pipeline()
                        .addLast(
                                createDecoder(),
                                createReadTimeoutHandler(),
                                createEncoder(),
                                createHandler(handlerCreatingContext),
                                createExceptionHandler()
                        );
            }
        };
    }

    private ReadTimeoutHandler createReadTimeoutHandler() {
        return new ReadTimeoutHandler(configuration.getConnectionLifeTimeoutSeconds(), SECONDS);
    }

    private ProtocolExceptionHandler createExceptionHandler() {
        return new ProtocolExceptionHandler(handlerCreatingContext.contextAttributeManager);
    }

    private static void shutdown(final EventLoopGroup eventLoopGroup) {
        try {
            eventLoopGroup.shutdownGracefully().sync();
        } catch (final InterruptedException exception) {
            currentThread().interrupt();
        }
    }

    @RequiredArgsConstructor
    @Getter
    @Builder
    protected static final class ProtocolHandlerCreatingContext {
        private final ContextAttributeManager contextAttributeManager;
        private final ConnectionManager connectionManager;
        private final List<PackageDecoder<?, ?, ?>> packageDecoders;
        private final List<PackageEncoder<?>> packageEncoders;
        private final List<PackageHandler<?>> packageHandlers;
    }
}
