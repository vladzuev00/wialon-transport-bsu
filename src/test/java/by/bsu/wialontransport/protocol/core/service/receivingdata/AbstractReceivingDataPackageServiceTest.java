package by.bsu.wialontransport.protocol.core.service.receivingdata;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.kafka.producer.AbstractKafkaDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.service.receivingdata.filter.DataFilter;
import by.bsu.wialontransport.protocol.core.service.receivingdata.fixer.DataFixer;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractRequestDataPackage;
import io.netty.channel.ChannelHandlerContext;
import lombok.Value;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class AbstractReceivingDataPackageServiceTest {

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    @Mock
    private DataFilter mockedDataFilter;

    @Mock
    private AbstractKafkaDataProducer mockedKafkaDataProducer;

    @Mock
    private DataFixer mockedDataFixer;

    @Captor
    private ArgumentCaptor<TestResponseDataPackage> responsePackageArgumentCaptor;

    private AbstractReceivingDataPackageService<TestRequestDataPackage, TestResponseDataPackage> receivingService;

    @Before
    public void initializeReceivingService() {
        this.receivingService = new TestReceivingDataPackageService(
                this.mockedContextAttributeManager,
                this.mockedDataFilter,
                this.mockedKafkaDataProducer,
                this.mockedDataFixer
        );
    }

    @Test
    public void dataShouldBeReceivedAsValidInCaseNotExistingPreviousData() {
        final Data givenData = createData(255L);
        final TestRequestDataPackage givenPackage = new TestRequestDataPackage(singletonList(givenData));

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        when(this.mockedContextAttributeManager.findLastData(givenContext)).thenReturn(empty());
        when(this.mockedDataFilter.isValid(givenData)).thenReturn(true);

        final Tracker givenTracker = createTracker(256L);
        when(this.mockedContextAttributeManager.findTracker(givenContext)).thenReturn(Optional.of(givenTracker));

        this.receivingService.receive(givenPackage, givenContext);

        verify(this.mockedContextAttributeManager, times(1)).findLastData(givenContext);
        verify(this.mockedDataFilter, times(1)).isValid(givenData);

        final Data expectedDataWithTracker = createDataWithTracker(255L, 256L);
        verify(this.mockedContextAttributeManager, times(1))
                .putLastData(givenContext, expectedDataWithTracker);

        verify(this.mockedKafkaDataProducer, times(1)).send(expectedDataWithTracker);

        verify(givenContext, times(1)).writeAndFlush(this.responsePackageArgumentCaptor.capture());
        final TestResponseDataPackage actualResponsePackage = this.responsePackageArgumentCaptor.getValue();
        final TestResponseDataPackage expectedResponsePackage = new TestResponseDataPackage(true);
        assertEquals(expectedResponsePackage, actualResponsePackage);
    }

    @Test
    public void dataShouldBeSkippedBecauseOfItIsNotValidAndPreviousDataDoesNotExist() {
        final Data givenData = createData(255L);
        final TestRequestDataPackage givenPackage = new TestRequestDataPackage(singletonList(givenData));

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        when(this.mockedContextAttributeManager.findLastData(givenContext)).thenReturn(empty());
        when(this.mockedDataFilter.isValid(givenData)).thenReturn(false);

        this.receivingService.receive(givenPackage, givenContext);

        verify(this.mockedContextAttributeManager, times(1)).findLastData(givenContext);
        verify(this.mockedDataFilter, times(1)).isValid(givenData);
        verify(this.mockedContextAttributeManager, times(0))
                .putLastData(same(givenContext), any(Data.class));
        verify(this.mockedKafkaDataProducer, times(0)).send(any(Data.class));

        verify(givenContext, times(1)).writeAndFlush(this.responsePackageArgumentCaptor.capture());
        final TestResponseDataPackage actualResponsePackage = this.responsePackageArgumentCaptor.getValue();
        final TestResponseDataPackage expectedResponsePackage = new TestResponseDataPackage(true);
        assertEquals(expectedResponsePackage, actualResponsePackage);
    }

    @Test
    public void dataShouldBeReceivedAsValidInCaseExistingPreviousData() {
        final Data givenData = createData(255L);
        final TestRequestDataPackage givenPackage = new TestRequestDataPackage(singletonList(givenData));

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Data givenPreviousData = createData(254L);
        when(this.mockedContextAttributeManager.findLastData(givenContext)).thenReturn(Optional.of(givenPreviousData));

        when(this.mockedDataFilter.isValid(givenData, givenPreviousData)).thenReturn(true);

        final Tracker givenTracker = createTracker(256L);
        when(this.mockedContextAttributeManager.findTracker(givenContext)).thenReturn(Optional.of(givenTracker));

        this.receivingService.receive(givenPackage, givenContext);

        verify(this.mockedContextAttributeManager, times(1)).findLastData(givenContext);
        verify(this.mockedDataFilter, times(1)).isValid(givenData, givenPreviousData);
        verify(this.mockedDataFixer, times(0)).fix(any(Data.class), any(Data.class));

        final Data expectedDataWithTracker = createDataWithTracker(255L, 256L);
        verify(this.mockedContextAttributeManager, times(1))
                .putLastData(givenContext, expectedDataWithTracker);

        verify(this.mockedKafkaDataProducer, times(1)).send(expectedDataWithTracker);

        verify(givenContext, times(1)).writeAndFlush(this.responsePackageArgumentCaptor.capture());
        final TestResponseDataPackage actualResponsePackage = this.responsePackageArgumentCaptor.getValue();
        final TestResponseDataPackage expectedResponsePackage = new TestResponseDataPackage(true);
        assertEquals(expectedResponsePackage, actualResponsePackage);
    }

    @Test
    public void dataShouldBeReceivedAsValidWithFixingInCaseExistingPreviousData() {
        final Data givenData = createData(255L);
        final TestRequestDataPackage givenPackage = new TestRequestDataPackage(singletonList(givenData));

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Data givenPreviousData = createData(254L);
        when(this.mockedContextAttributeManager.findLastData(givenContext)).thenReturn(Optional.of(givenPreviousData));

        when(this.mockedDataFilter.isValid(givenData, givenPreviousData)).thenReturn(false);
        when(this.mockedDataFilter.isNeedToBeFixed(givenData, givenPreviousData)).thenReturn(true);

        final Data givenFixedData = createData(257L);
        when(this.mockedDataFixer.fix(givenData, givenPreviousData)).thenReturn(givenFixedData);

        final Tracker givenTracker = createTracker(256L);
        when(this.mockedContextAttributeManager.findTracker(givenContext)).thenReturn(Optional.of(givenTracker));

        this.receivingService.receive(givenPackage, givenContext);

        verify(this.mockedContextAttributeManager, times(1)).findLastData(givenContext);
        verify(this.mockedDataFilter, times(1)).isValid(givenData, givenPreviousData);
        verify(this.mockedDataFilter, times(1)).isNeedToBeFixed(givenData, givenPreviousData);
        verify(this.mockedDataFixer, times(1)).fix(givenData, givenPreviousData);

        final Data expectedDataWithTracker = createDataWithTracker(257L, 256L);
        verify(this.mockedContextAttributeManager, times(1))
                .putLastData(givenContext, expectedDataWithTracker);

        verify(this.mockedKafkaDataProducer, times(1)).send(expectedDataWithTracker);

        verify(givenContext, times(1)).writeAndFlush(this.responsePackageArgumentCaptor.capture());
        final TestResponseDataPackage actualResponsePackage = this.responsePackageArgumentCaptor.getValue();
        final TestResponseDataPackage expectedResponsePackage = new TestResponseDataPackage(true);
        assertEquals(expectedResponsePackage, actualResponsePackage);
    }

    @Test
    public void dataShouldBeSkippedBecauseOfItIsNotValidAndDoesNotNeedToBeFixedInCaseExistingPreviousData() {
        final Data givenData = createData(255L);
        final TestRequestDataPackage givenPackage = new TestRequestDataPackage(singletonList(givenData));

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Data givenPreviousData = createData(254L);
        when(this.mockedContextAttributeManager.findLastData(givenContext)).thenReturn(Optional.of(givenPreviousData));

        when(this.mockedDataFilter.isValid(givenData, givenPreviousData)).thenReturn(false);
        when(this.mockedDataFilter.isNeedToBeFixed(givenData, givenPreviousData)).thenReturn(false);

        this.receivingService.receive(givenPackage, givenContext);

        verify(this.mockedContextAttributeManager, times(1)).findLastData(givenContext);
        verify(this.mockedDataFilter, times(1)).isValid(givenData, givenPreviousData);
        verify(this.mockedDataFilter, times(1)).isNeedToBeFixed(givenData, givenPreviousData);
        verify(this.mockedDataFixer, times(0)).fix(any(Data.class), any(Data.class));
        verify(this.mockedContextAttributeManager, times(0))
                .putLastData(any(ChannelHandlerContext.class), any(Data.class));
        verify(this.mockedKafkaDataProducer, times(0)).send(any(Data.class));

        verify(givenContext, times(1)).writeAndFlush(this.responsePackageArgumentCaptor.capture());
        final TestResponseDataPackage actualResponsePackage = this.responsePackageArgumentCaptor.getValue();
        final TestResponseDataPackage expectedResponsePackage = new TestResponseDataPackage(true);
        assertEquals(expectedResponsePackage, actualResponsePackage);
    }

    //TODO: tests with several data
    @Test
    public void a() {
        throw new RuntimeException();
    }

    private static Data createData(final Long id) {
        return Data.builder()
                .id(id)
                .build();
    }

    private static Data createDataWithTracker(final Long id, final Long trackerId) {
        return Data.builder()
                .id(id)
                .tracker(createTracker(trackerId))
                .build();
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    private static final class TestRequestDataPackage extends AbstractRequestDataPackage {

        public TestRequestDataPackage(final List<Data> data) {
            super(data);
        }
    }

    @Value
    private static class TestResponseDataPackage implements Package {
        boolean successReceived;
    }

    private static final class TestReceivingDataPackageService
            extends AbstractReceivingDataPackageService<TestRequestDataPackage, TestResponseDataPackage> {

        public TestReceivingDataPackageService(final ContextAttributeManager contextAttributeManager,
                                               final DataFilter dataFilter,
                                               final AbstractKafkaDataProducer kafkaInboundDataProducer,
                                               final DataFixer dataFixer) {
            super(contextAttributeManager, dataFilter, kafkaInboundDataProducer, dataFixer);
        }

        @Override
        protected TestResponseDataPackage createResponse(final List<Data> receivedData) {
            final boolean successReceived = !receivedData.isEmpty();
            return new TestResponseDataPackage(successReceived);
        }
    }
}
