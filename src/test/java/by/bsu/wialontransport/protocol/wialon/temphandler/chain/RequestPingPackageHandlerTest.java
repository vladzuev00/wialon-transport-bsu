package by.bsu.wialontransport.protocol.wialon.temphandler.chain;

import by.bsu.wialontransport.protocol.wialon.temphandler.chain.data.RequestDataPackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonRequestPingPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonResponsePingPackage;

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
    private ArgumentCaptor<WialonResponsePingPackage> responsePingPackageArgumentCaptor;

    public RequestPingPackageHandlerTest() {
        this.handler = new RequestPingPackageHandler(this.mockedNextHandler);
    }

    @Test
    public void packageShouldBeHandledIndependently() {
        final WialonPackage givenRequestPackage = new WialonRequestPingPackage();
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.handler.handle(givenRequestPackage, givenContext);

        verify(givenContext, times(1))
                .writeAndFlush(this.responsePingPackageArgumentCaptor.capture());
        assertNotNull(this.responsePingPackageArgumentCaptor.getValue());
    }
}