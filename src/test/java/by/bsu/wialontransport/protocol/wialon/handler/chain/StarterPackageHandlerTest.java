package by.bsu.wialontransport.protocol.wialon.handler.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public final class StarterPackageHandlerTest {

    @Mock
    private RequestLoginPackageHandler mockedNextHandler;

    private StarterPackageHandler handler;

    @Before
    public void initializeHandler() {
        this.handler = new StarterPackageHandler(this.mockedNextHandler);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void packageShouldNotBeHandledIndependently() {
        final Package givenPackage = new Package() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.handler.handleIndependently(givenPackage, givenContext);
    }
}
