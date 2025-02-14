package by.bsu.wialontransport.protocol.core.serverrunner;

import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.exceptionhandler.ProtocolExceptionHandler;
import by.bsu.wialontransport.protocol.core.model.ProtocolServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
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

    private void run(ProtocolServer server) {
        EventLoopGroup parentGroup = new NioEventLoopGroup(server.getProperty().getThreadCountProcessingConnection());
        EventLoopGroup childGroup = new NioEventLoopGroup(server.getProperty().getThreadCountProcessingData());
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel ch) {
                                    ch.pipeline().addLast(
                                            server.getDecoder(),
                                            createReadTimeoutHandler(server),
                                            server.getEncoder(),
                                            server.getHandler(),
                                            createExceptionHandler()
                                    );
                                }
                            }
                    ).localAddress(new InetSocketAddress(server.getProperty().getHost(), server.getProperty().getPort()));

            ChannelFuture f = bootstrap.bind(server.getProperty().getPort()).sync();
            f.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    private ReadTimeoutHandler createReadTimeoutHandler(ProtocolServer server) {
        return new ReadTimeoutHandler(server.getProperty().getConnectionLifeTimeoutSeconds(), SECONDS);
    }

    private ProtocolExceptionHandler createExceptionHandler() {
        return new ProtocolExceptionHandler(contextAttributeManager);
    }
}
