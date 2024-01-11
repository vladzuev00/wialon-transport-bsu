package by.bsu.wialontransport.protocol.newwing.handler.packages;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingEventCountPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.response.NewWingSuccessResponsePackage;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class NewWingEventCountPackageHandlerTest {
    private final NewWingEventCountPackageHandler packageHandler = new NewWingEventCountPackageHandler();

    @Captor
    private ArgumentCaptor<NewWingSuccessResponsePackage> responsePackageArgumentCaptor;

    @Test
    public void concretePackageShouldBeHandled() {
        final NewWingEventCountPackage givenRequestPackage = mock(NewWingEventCountPackage.class);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        packageHandler.handleConcretePackage(givenRequestPackage, givenContext);

        verify(givenContext, times(1)).writeAndFlush(responsePackageArgumentCaptor.capture());

        final NewWingSuccessResponsePackage actualResponsePackage = responsePackageArgumentCaptor.getValue();
        final NewWingSuccessResponsePackage expectedResponsePackage = new NewWingSuccessResponsePackage();
        assertEquals(expectedResponsePackage, actualResponsePackage);
    }
}
