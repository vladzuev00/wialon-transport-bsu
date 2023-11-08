package by.bsu.wialontransport.protocol.core.handler.packages;

import by.bsu.wialontransport.protocol.core.handler.packages.login.LoginPackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.LoginPackage;
import by.bsu.wialontransport.protocol.core.service.login.TrackerLoginService;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class LoginPackageHandlerTest {

    @Mock
    private TrackerLoginService<LoginPackage> mockedLoginService;

    private LoginPackageHandler<LoginPackage> packageHandler;

    @Before
    public void initializePackageHandler() {
        this.packageHandler = new LoginPackageHandler<>(LoginPackage.class, this.mockedLoginService) {
        };
    }

    @Test
    public void concretePackageShouldBeHandled() {
        final LoginPackage givenPackage = mock(LoginPackage.class);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.packageHandler.handleConcretePackage(givenPackage, givenContext);

        verify(this.mockedLoginService, times(1)).login(same(givenPackage), same(givenContext));
    }
}
