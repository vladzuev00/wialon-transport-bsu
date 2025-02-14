package by.bsu.wialontransport.protocol.core.serverrunner;

import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.exceptionhandler.ProtocolExceptionHandler;
import by.bsu.wialontransport.protocol.core.model.ProtocolServer;
import by.bsu.wialontransport.protocol.core.property.ProtocolServerProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@RequiredArgsConstructor
public final class ProtocolServerRunner {
    private final List<ProtocolServer> servers;
    private final ContextAttributeManager contextAttributeManager;

    public void run() {
        servers.stream()
                .map(server -> new Thread(() -> run(server)))
                .forEach(Thread::start);
    }

    private void run(final ProtocolServer server) {
        final ProtocolServerProperty property = server.getProperty();
        final EventLoopGroup parentGroup = new NioEventLoopGroup(property.getThreadCountProcessingConnection());
        final EventLoopGroup childGroup = new NioEventLoopGroup(property.getThreadCountProcessingData());
        try {
            new ServerBootstrap()
                    .group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(createChildHandler(server))
                    .localAddress(new InetSocketAddress(property.getHost(), property.getPort()))
                    .bind(property.getPort())
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } catch (final InterruptedException exception) {
            currentThread().interrupt();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    private ChannelHandler createChildHandler(final ProtocolServer server) {
        return new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(final SocketChannel channel) {
                channel.pipeline()
                        .addLast(
                                server.getDecoder(),
                                createReadTimeoutHandler(server.getProperty()),
                                server.getEncoder(),
                                server.getHandler(),
                                createExceptionHandler()
                        );
            }
        };
    }

    private ReadTimeoutHandler createReadTimeoutHandler(ProtocolServerProperty property) {
        return new ReadTimeoutHandler(property.getConnectionLifeTimeoutSeconds(), SECONDS);
    }

    private ProtocolExceptionHandler createExceptionHandler() {
        return new ProtocolExceptionHandler(contextAttributeManager);
    }
}
