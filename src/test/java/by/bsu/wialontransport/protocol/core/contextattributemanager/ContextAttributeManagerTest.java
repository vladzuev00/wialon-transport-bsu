package by.bsu.wialontransport.protocol.core.contextattributemanager;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ContextAttributeManagerTest {
    private static final String EXPECTED_NAME_ATTRIBUTE_KEY_TRACKER_IMEI = "tracker_imei";
    private static final String EXPECTED_NAME_ATTRIBUTE_KEY_TRACKER = "tracker";
    private static final String EXPECTED_NAME_ATTRIBUTE_KEY_LAST_DATA = "last_data";

    private final ContextAttributeManager contextAttributeManager;

    @Captor
    private ArgumentCaptor<AttributeKey<String>> attributeKeyImeiArgumentCaptor;

    @Captor
    private ArgumentCaptor<AttributeKey<Tracker>> attributeKeyTrackerArgumentCaptor;

    @Captor
    private ArgumentCaptor<AttributeKey<Data>> attributeKeyDataArgumentCaptor;

    public ContextAttributeManagerTest() {
        contextAttributeManager = new ContextAttributeManager();
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void trackerImeiShouldBePutInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final String givenImei = "11111222223333344444";

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        contextAttributeManager.putTrackerImei(givenContext, givenImei);

        verify(givenAttribute, times(1)).set(same(givenImei));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void trackerImeiShouldBeFoundInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final String givenImei = "11111222223333344444";
        when(givenAttribute.get()).thenReturn(givenImei);

        final Optional<String> optionalActual = contextAttributeManager.findTrackerImei(givenContext);
        assertTrue(optionalActual.isPresent());
        final String actual = optionalActual.get();
        assertSame(givenImei, actual);

        verify(givenChannel, times(1)).attr(attributeKeyImeiArgumentCaptor.capture());

        final String actualNameAttributeKey = attributeKeyImeiArgumentCaptor.getValue().name();
        assertEquals(EXPECTED_NAME_ATTRIBUTE_KEY_TRACKER_IMEI, actualNameAttributeKey);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void trackerImeiShouldNotBeFoundInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        when(givenAttribute.get()).thenReturn(null);

        final Optional<String> optionalActual = contextAttributeManager.findTrackerImei(givenContext);
        assertTrue(optionalActual.isEmpty());

        verify(givenChannel, times(1)).attr(attributeKeyImeiArgumentCaptor.capture());

        final String actualNameAttributeKey = attributeKeyImeiArgumentCaptor.getValue().name();
        assertEquals(EXPECTED_NAME_ATTRIBUTE_KEY_TRACKER_IMEI, actualNameAttributeKey);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void trackerShouldBePutInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Tracker givenTracker = createTracker(255L);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        contextAttributeManager.putTracker(givenContext, givenTracker);

        verify(givenChannel, times(1)).attr(attributeKeyTrackerArgumentCaptor.capture());
        verify(givenAttribute, times(1)).set(same(givenTracker));

        final String actualNameAttributeKey = attributeKeyTrackerArgumentCaptor.getValue().name();
        assertEquals(EXPECTED_NAME_ATTRIBUTE_KEY_TRACKER, actualNameAttributeKey);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void trackerShouldBeFoundInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final Tracker givenTracker = createTracker(255L);
        when(givenAttribute.get()).thenReturn(givenTracker);

        final Optional<Tracker> optionalActual = contextAttributeManager.findTracker(givenContext);
        assertTrue(optionalActual.isPresent());
        final Tracker actual = optionalActual.get();
        assertSame(givenTracker, actual);

        verify(givenChannel, times(1)).attr(attributeKeyTrackerArgumentCaptor.capture());

        final String actualNameAttributeKey = attributeKeyTrackerArgumentCaptor.getValue().name();
        assertEquals(EXPECTED_NAME_ATTRIBUTE_KEY_TRACKER, actualNameAttributeKey);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void trackerShouldNotBeFoundInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        when(givenAttribute.get()).thenReturn(null);

        final Optional<Tracker> optionalActual = contextAttributeManager.findTracker(givenContext);
        assertTrue(optionalActual.isEmpty());

        verify(givenChannel, times(1)).attr(attributeKeyTrackerArgumentCaptor.capture());

        final String actualNameAttributeKey = attributeKeyTrackerArgumentCaptor.getValue().name();
        assertEquals(EXPECTED_NAME_ATTRIBUTE_KEY_TRACKER, actualNameAttributeKey);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void lastDataShouldBePutInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Data givenData = createData(255L);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        contextAttributeManager.putLastData(givenContext, givenData);

        verify(givenChannel, times(1)).attr(attributeKeyDataArgumentCaptor.capture());
        verify(givenAttribute, times(1)).set(same(givenData));

        final String actualNameAttributeKey = attributeKeyDataArgumentCaptor.getValue().name();
        assertEquals(EXPECTED_NAME_ATTRIBUTE_KEY_LAST_DATA, actualNameAttributeKey);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void lastDataShouldBeFoundFromContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final Data givenData = createData(255L);
        when(givenAttribute.get()).thenReturn(givenData);

        final Optional<Data> optionalActual = contextAttributeManager.findLastData(givenContext);
        assertTrue(optionalActual.isPresent());
        final Data actual = optionalActual.get();
        assertSame(givenData, actual);

        verify(givenChannel, times(1)).attr(attributeKeyDataArgumentCaptor.capture());

        final String actualNameAttributeKey = attributeKeyDataArgumentCaptor.getValue().name();
        assertEquals(EXPECTED_NAME_ATTRIBUTE_KEY_LAST_DATA, actualNameAttributeKey);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void lastDataShouldNotBeFoundFromContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        when(givenAttribute.get()).thenReturn(null);

        final Optional<Data> optionalActual = contextAttributeManager.findLastData(givenContext);
        assertTrue(optionalActual.isEmpty());

        verify(givenChannel, times(1)).attr(attributeKeyDataArgumentCaptor.capture());

        final String actualNameAttributeKey = attributeKeyDataArgumentCaptor.getValue().name();
        assertEquals(EXPECTED_NAME_ATTRIBUTE_KEY_LAST_DATA, actualNameAttributeKey);
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
