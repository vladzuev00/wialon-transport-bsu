//package by.bsu.wialontransport.protocol.newwing.handler.packages;
//
//import by.bsu.wialontransport.protocol.core.model.packages.Package;
//import by.bsu.wialontransport.protocol.newwing.model.request.NewWingEventCountPackage;
//import by.bsu.wialontransport.protocol.newwing.model.response.NewWingSuccessResponsePackage;
//import io.netty.channel.ChannelHandlerContext;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.mock;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class NewWingEventCountPackageHandlerTest {
//    private final NewWingEventCountPackageHandler handler = new NewWingEventCountPackageHandler();
//
//    @Test
//    public void packageShouldBeHandledInternally() {
//        final NewWingEventCountPackage givenRequestPackage = mock(NewWingEventCountPackage.class);
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        final Package actual = handler.handleInternal(givenRequestPackage, givenContext);
//        final Package expected = new NewWingSuccessResponsePackage();
//        assertEquals(expected, actual);
//    }
//}
