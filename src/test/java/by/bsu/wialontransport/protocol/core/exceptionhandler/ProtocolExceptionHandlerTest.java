//package by.bsu.wialontransport.protocol.core.exceptionhandler;
//
//import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
//import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
//import by.bsu.wialontransport.protocol.core.model.Package;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.DecoderException;
//import nl.altindag.log.LogCaptor;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.List;
//import java.util.Optional;
//
//import static java.util.Optional.empty;
//import static nl.altindag.log.LogCaptor.forClass;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ProtocolExceptionHandlerTest {
//    private final LogCaptor logCaptor = forClass(ProtocolExceptionHandler.class);
//
//    @Mock
//    private ContextAttributeManager mockedContextAttributeManager;
//
//    private ProtocolExceptionHandler handler;
//
//    @Before
//    public void initializeHandler() {
//        handler = new ProtocolExceptionHandler(mockedContextAttributeManager);
//    }
//
//    @Test
//    public void answerableExceptionShouldBeHandled() {
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        final Package givenAnswer = new Package() {
//        };
//        final AnswerableException givenException = new AnswerableException(givenAnswer);
//
//        handler.exceptionCaught(givenContext, givenException);
//
//        verify(givenContext, times(1)).writeAndFlush(same(givenAnswer));
//        verifyNoMoreInteractions(givenContext);
//    }
//
//    @Test
//    public void answerableExceptionWrappedInDecoderExceptionShouldBeHandled() {
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        final Package givenAnswer = new Package() {
//        };
//        final AnswerableException givenCause = new AnswerableException(givenAnswer);
//        final DecoderException givenException = new DecoderException(givenCause);
//
//        handler.exceptionCaught(givenContext, givenException);
//
//        verify(givenContext, times(1)).writeAndFlush(same(givenAnswer));
//        verifyNoMoreInteractions(givenContext);
//    }
//
//    @Test
//    public void notAnswerableExceptionShouldBeHandledWithDefinedTrackerImei() {
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//        final Exception givenException = mock(Exception.class);
//
//        final String givenTrackerImei = "11112222333344445555";
//        when(mockedContextAttributeManager.findTrackerImei(same(givenContext)))
//                .thenReturn(Optional.of(givenTrackerImei));
//
//        final String givenExceptionMessage = "exception-message";
//        when(givenException.getMessage()).thenReturn(givenExceptionMessage);
//
//        handler.exceptionCaught(givenContext, givenException);
//
//        final List<String> actualLogs = logCaptor.getLogs();
//        final List<String> expectedLogs = List.of(
//                "During working with tracker '11112222333344445555' was arisen exception: exception-message"
//        );
//        assertEquals(expectedLogs, actualLogs);
//
//        verify(givenException, times(1)).printStackTrace();
//        verify(givenContext, times(1)).close();
//    }
//
//    @Test
//    public void notAnswerableExceptionShouldBeHandledWithNotDefinedTrackerImei() {
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//        final Exception givenException = mock(Exception.class);
//
//        when(mockedContextAttributeManager.findTrackerImei(same(givenContext))).thenReturn(empty());
//
//        final String givenExceptionMessage = "exception-message";
//        when(givenException.getMessage()).thenReturn(givenExceptionMessage);
//
//        handler.exceptionCaught(givenContext, givenException);
//
//        final List<String> actualLogs = logCaptor.getLogs();
//        final List<String> expectedLogs = List.of(
//                "During working with tracker 'not defined' was arisen exception: exception-message"
//        );
//        assertEquals(expectedLogs, actualLogs);
//
//        verify(givenException, times(1)).printStackTrace();
//        verify(givenContext, times(1)).close();
//    }
//}
