package by.bsu.wialontransport.protocol.wialon.handler.chain;

import by.bsu.wialontransport.protocol.core.service.login.TEMPAuthorizationTrackerService;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonRequestLoginPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonRequestPingPackage;

import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class RequestLoginPackageHandlerTest {

    @Mock
    private RequestPingPackageHandler mockedNextHandler;

    @Mock
    private TEMPAuthorizationTrackerService mockedAuthorizationTrackerService;

    private RequestLoginPackageHandler handler;

    @Captor
    private ArgumentCaptor<WialonRequestLoginPackage> requestLoginPackageArgumentCaptor;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Before
    public void initializeHandler() {
        this.handler = new RequestLoginPackageHandler(this.mockedNextHandler, this.mockedAuthorizationTrackerService);
    }

    @Test
    public void packageShouldBeHandledIndependently() {
        final WialonPackage givenPackage = new WialonRequestLoginPackage("11111222223333344444", "password");
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.handler.handleIndependently(givenPackage, givenContext);

        verify(this.mockedAuthorizationTrackerService, times(1))
                .authorize(this.requestLoginPackageArgumentCaptor.capture(), this.contextArgumentCaptor.capture());

        assertSame(givenPackage, this.requestLoginPackageArgumentCaptor.getValue());
        assertSame(givenContext, this.contextArgumentCaptor.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void packageShouldNotBeHandledIndependentlyBecauseOfNotSuitableType() {
        final WialonPackage givenPackage = new WialonRequestPingPackage();
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.handler.handleIndependently(givenPackage, givenContext);
    }
}
