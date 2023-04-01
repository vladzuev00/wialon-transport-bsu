package by.bsu.wialontransport.protocol.wialon.handler.chain;

import by.bsu.wialontransport.protocol.wialon.handler.chain.data.RequestDataPackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.RequestPingPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.ResponsePingPackage;

import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public final class RequestPingPackageHandlerTest {
    private final RequestPingPackageHandler handler;

    @Mock
    private RequestDataPackageHandler mockedNextHandler;

    @Captor
    private ArgumentCaptor<ResponsePingPackage> responsePingPackageArgumentCaptor;

    public RequestPingPackageHandlerTest() {
        this.handler = new RequestPingPackageHandler(this.mockedNextHandler);
    }

    @Test
    public void packageShouldBeHandledIndependently() {
        final Package givenRequestPackage = new RequestPingPackage();
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.handler.handle(givenRequestPackage, givenContext);

        verify(givenContext, times(1))
                .writeAndFlush(this.responsePingPackageArgumentCaptor.capture());
        assertNotNull(this.responsePingPackageArgumentCaptor.getValue());
    }
}
