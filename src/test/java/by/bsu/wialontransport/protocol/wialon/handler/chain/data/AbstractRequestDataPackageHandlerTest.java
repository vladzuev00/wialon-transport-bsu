package by.bsu.wialontransport.protocol.wialon.handler.chain.data;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.core.service.receivingdata.ReceivingDataService;
import by.bsu.wialontransport.protocol.wialon.handler.chain.PackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractWialonRequestDataPackage;

import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class AbstractRequestDataPackageHandlerTest {

    @Mock
    private PackageHandler mockedNextHandler;

    @Mock
    private ReceivingDataService<TestRequestDataPackage, ?> mockedReceivingPackageService;

    private TestRequestDataPackageHandler handler;

    @Captor
    private ArgumentCaptor<TestRequestDataPackage> requestDataPackageArgumentCaptor;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Before
    public void initializeHandler() {
        this.handler = new TestRequestDataPackageHandler(this.mockedNextHandler, this.mockedReceivingPackageService);
    }

    @Test
    public void packageShouldBeHandledIndependently() {
        final TestRequestDataPackage givenRequestPackage = new TestRequestDataPackage(emptyList());
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.handler.handleIndependently(givenRequestPackage, givenContext);

        verify(this.mockedReceivingPackageService, times(1))
                .receive(this.requestDataPackageArgumentCaptor.capture(), this.contextArgumentCaptor.capture());
        assertSame(givenRequestPackage, this.requestDataPackageArgumentCaptor.getValue());
        assertSame(givenContext, this.contextArgumentCaptor.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void packageShouldNotBeHandledIndependentlyBecauseOfNotSuitableType() {
        final WialonPackage givenRequestPackage = new WialonPackage() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.handler.handleIndependently(givenRequestPackage, givenContext);
    }

    private static final class TestRequestDataPackage extends AbstractWialonRequestDataPackage {
        public TestRequestDataPackage(final List<Data> data) {
            super(data);
        }
    }

    private static final class TestRequestDataPackageHandler
            extends AbstractRequestDataPackageHandler<TestRequestDataPackage> {

        public TestRequestDataPackageHandler(final PackageHandler nextHandler,
                                             final ReceivingDataService<TestRequestDataPackage, ?> receivingPackageService) {
            super(TestRequestDataPackage.class, nextHandler, receivingPackageService);
        }
    }
}
