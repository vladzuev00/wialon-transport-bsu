package by.bsu.wialontransport.protocol.core.contextattributemanager;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager.*;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ContextAttributeManagerTest {
    private final ContextAttributeManager contextAttributeManager = new ContextAttributeManager();

    @Test
    @SuppressWarnings("unchecked")
    public void trackerImeiShouldBePutInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final String givenImei = "11111222223333344444";

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<String> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(same(ATTRIBUTE_KEY_TRACKER_IMEI))).thenReturn(givenAttribute);

        contextAttributeManager.putTrackerImei(givenContext, givenImei);

        verify(givenAttribute, times(1)).set(same(givenImei));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void trackerImeiShouldBeFoundInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<String> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(same(ATTRIBUTE_KEY_TRACKER_IMEI))).thenReturn(givenAttribute);

        final String givenImei = "11111222223333344444";
        when(givenAttribute.get()).thenReturn(givenImei);

        final Optional<String> optionalActual = contextAttributeManager.findTrackerImei(givenContext);
        assertTrue(optionalActual.isPresent());
        final String actual = optionalActual.get();
        assertSame(givenImei, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void trackerImeiShouldNotBeFoundInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<String> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(same(ATTRIBUTE_KEY_TRACKER_IMEI))).thenReturn(givenAttribute);

        when(givenAttribute.get()).thenReturn(null);

        final Optional<String> optionalActual = contextAttributeManager.findTrackerImei(givenContext);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void trackerShouldBePutInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Tracker givenTracker = createTracker(255L);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<Tracker> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(same(ATTRIBUTE_KEY_TRACKER))).thenReturn(givenAttribute);

        contextAttributeManager.putTracker(givenContext, givenTracker);

        verify(givenAttribute, times(1)).set(same(givenTracker));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void trackerShouldBeFoundInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<Tracker> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(same(ATTRIBUTE_KEY_TRACKER))).thenReturn(givenAttribute);

        final Tracker givenTracker = createTracker(255L);
        when(givenAttribute.get()).thenReturn(givenTracker);

        final Optional<Tracker> optionalActual = contextAttributeManager.findTracker(givenContext);
        assertTrue(optionalActual.isPresent());
        final Tracker actual = optionalActual.get();
        assertSame(givenTracker, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void trackerShouldNotBeFoundInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<Tracker> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(same(ATTRIBUTE_KEY_TRACKER))).thenReturn(givenAttribute);

        when(givenAttribute.get()).thenReturn(null);

        final Optional<Tracker> optionalActual = contextAttributeManager.findTracker(givenContext);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void lastDataShouldBePutInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Data givenData = createData(255L);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<Data> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(same(ATTRIBUTE_KEY_LAST_DATA))).thenReturn(givenAttribute);

        contextAttributeManager.putLastData(givenContext, givenData);

        verify(givenAttribute, times(1)).set(same(givenData));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void lastDataShouldBeFoundFromContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<Data> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(same(ATTRIBUTE_KEY_LAST_DATA))).thenReturn(givenAttribute);

        final Data givenData = createData(255L);
        when(givenAttribute.get()).thenReturn(givenData);

        final Optional<Data> optionalActual = contextAttributeManager.findLastData(givenContext);
        assertTrue(optionalActual.isPresent());
        final Data actual = optionalActual.get();
        assertSame(givenData, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void lastDataShouldNotBeFoundFromContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<Data> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(same(ATTRIBUTE_KEY_LAST_DATA))).thenReturn(givenAttribute);

        when(givenAttribute.get()).thenReturn(null);

        final Optional<Data> optionalActual = contextAttributeManager.findLastData(givenContext);
        assertTrue(optionalActual.isEmpty());
    }

    @SuppressWarnings("SameParameterValue")
    private Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private Data createData(final Long id) {
        return Data.builder()
                .id(id)
                .build();
    }
}
