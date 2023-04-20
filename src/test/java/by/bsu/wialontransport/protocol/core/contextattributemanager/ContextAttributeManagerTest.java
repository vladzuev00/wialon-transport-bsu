package by.bsu.wialontransport.protocol.core.contextattributemanager;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ContextAttributeManagerTest {
    private static final String NAME_ATTRIBUTE_KEY_TRACKER_IMEI = "tracker_imei";
    private static final String NAME_ATTRIBUTE_KEY_TRACKER = "tracker";
    private static final String NAME_ATTRIBUTE_KEY_LAST_DATA = "last_data";

    private final ContextAttributeManager contextAttributeManager;

    @Captor
    private ArgumentCaptor<AttributeKey<String>> attributeKeyImeiArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<AttributeKey<Tracker>> attributeKeyTrackerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Tracker> trackerArgumentCaptor;

    @Captor
    private ArgumentCaptor<AttributeKey<Data>> attributeKeyDataArgumentCaptor;

    @Captor
    private ArgumentCaptor<Data> dataArgumentCaptor;

    public ContextAttributeManagerTest() {
        this.contextAttributeManager = new ContextAttributeManager();
    }

    @Test
    @SuppressWarnings("all")
    public void trackerImeiShouldBePutInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final String givenImei = "11111222223333344444";
        this.contextAttributeManager.putTrackerImei(givenContext, givenImei);

        verify(givenContext, times(1)).channel();
        verify(givenChannel, times(1)).attr(this.attributeKeyImeiArgumentCaptor.capture());
        verify(givenAttribute, times(1)).set(this.stringArgumentCaptor.capture());

        assertEquals(NAME_ATTRIBUTE_KEY_TRACKER_IMEI, this.attributeKeyImeiArgumentCaptor.getValue().name());
        assertEquals(givenImei, this.stringArgumentCaptor.getValue());
    }

    @Test
    @SuppressWarnings("all")
    public void trackerImeiShouldBeFoundInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final String expected = "11111222223333344444";
        when(givenAttribute.get()).thenReturn(expected);

        final String actual = this.contextAttributeManager.findTrackerImei(givenContext).orElseThrow();
        assertEquals(expected, actual);

        verify(givenContext, times(1)).channel();
        verify(givenChannel, times(1)).attr(this.attributeKeyImeiArgumentCaptor.capture());
        verify(givenAttribute, times(1)).get();

        assertEquals(NAME_ATTRIBUTE_KEY_TRACKER_IMEI, this.attributeKeyImeiArgumentCaptor.getValue().name());
    }

    @Test
    @SuppressWarnings("all")
    public void trackerImeiShouldNotBeFoundInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        when(givenAttribute.get()).thenReturn(null);

        final Optional<String> optionalActual = this.contextAttributeManager.findTrackerImei(givenContext);
        assertTrue(optionalActual.isEmpty());

        verify(givenContext, times(1)).channel();
        verify(givenChannel, times(1)).attr(this.attributeKeyImeiArgumentCaptor.capture());
        verify(givenAttribute, times(1)).get();

        assertEquals(NAME_ATTRIBUTE_KEY_TRACKER_IMEI, this.attributeKeyImeiArgumentCaptor.getValue().name());
    }

    @Test
    @SuppressWarnings("all")
    public void trackerShouldBePutInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final Tracker givenTracker = Tracker.builder()
                .id(255L)
                .imei("1111122222333334444455555")
                .phoneNumber("447336934")
                .password("password")
                .build();
        this.contextAttributeManager.putTracker(givenContext, givenTracker);

        verify(givenContext, times(1)).channel();
        verify(givenChannel, times(1)).attr(this.attributeKeyTrackerArgumentCaptor.capture());
        verify(givenAttribute, times(1)).set(this.trackerArgumentCaptor.capture());

        assertEquals(NAME_ATTRIBUTE_KEY_TRACKER, this.attributeKeyTrackerArgumentCaptor.getValue().name());
        assertEquals(givenTracker, this.trackerArgumentCaptor.getValue());
    }

    @Test
    @SuppressWarnings("all")
    public void trackerShouldBeFoundInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final Tracker expected = Tracker.builder()
                .id(255L)
                .imei("11111222223333344444")
                .phoneNumber("447336934")
                .password("password")
                .build();
        when(givenAttribute.get()).thenReturn(expected);

        final Tracker actual = this.contextAttributeManager.findTracker(givenContext).orElseThrow();
        assertEquals(expected, actual);

        verify(givenContext, times(1)).channel();
        verify(givenChannel, times(1)).attr(this.attributeKeyImeiArgumentCaptor.capture());
        verify(givenAttribute, times(1)).get();

        assertEquals(NAME_ATTRIBUTE_KEY_TRACKER, this.attributeKeyImeiArgumentCaptor.getValue().name());
    }

    @Test
    @SuppressWarnings("all")
    public void trackerShouldNotBeFoundInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        when(givenAttribute.get()).thenReturn(null);

        final Optional<Tracker> optionalActual = this.contextAttributeManager.findTracker(givenContext);
        assertTrue(optionalActual.isEmpty());

        verify(givenContext, times(1)).channel();
        verify(givenChannel, times(1)).attr(this.attributeKeyImeiArgumentCaptor.capture());
        verify(givenAttribute, times(1)).get();

        assertEquals(NAME_ATTRIBUTE_KEY_TRACKER, this.attributeKeyImeiArgumentCaptor.getValue().name());
    }

    @Test
    @SuppressWarnings("all")
    public void lastDataShouldBePutInContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final Data givenData = Data.builder()
                .date(LocalDate.of(2022, 11, 15))
                .time(LocalTime.of(15, 44, 22))
                .latitude(Latitude.builder()
                        .degrees(30)
                        .minutes(31)
                        .minuteShare(32)
                        .type(NORTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(33)
                        .minutes(34)
                        .minuteShare(35)
                        .type(EAST)
                        .build())
                .speed(36)
                .course(37)
                .altitude(38)
                .amountOfSatellites(39)
                .build();
        this.contextAttributeManager.putLastData(givenContext, givenData);

        verify(givenContext, times(1)).channel();
        verify(givenChannel, times(1)).attr(this.attributeKeyDataArgumentCaptor.capture());
        verify(givenAttribute, times(1)).set(this.dataArgumentCaptor.capture());

        assertEquals(NAME_ATTRIBUTE_KEY_LAST_DATA, this.attributeKeyDataArgumentCaptor.getValue().name());
        assertEquals(givenData, this.dataArgumentCaptor.getValue());
    }

    @Test
    @SuppressWarnings("all")
    public void lastDataShouldBeFoundFromContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final Data expected = Data.builder()
                .date(LocalDate.of(20212, 11, 15))
                .time(LocalTime.of(15, 44, 22))
                .latitude(Latitude.builder()
                        .degrees(30)
                        .minutes(31)
                        .minuteShare(32)
                        .type(NORTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(33)
                        .minutes(34)
                        .minuteShare(35)
                        .type(EAST)
                        .build())
                .speed(36)
                .course(37)
                .altitude(38)
                .amountOfSatellites(39)
                .build();
        when(givenAttribute.get()).thenReturn(expected);

        final Data actual = this.contextAttributeManager.findLastData(givenContext).orElseThrow();
        assertEquals(expected, actual);

        verify(givenContext, times(1)).channel();
        verify(givenChannel, times(1)).attr(this.attributeKeyDataArgumentCaptor.capture());
        verify(givenAttribute, times(1)).get();

        assertEquals(NAME_ATTRIBUTE_KEY_LAST_DATA, this.attributeKeyDataArgumentCaptor.getValue().name());
    }

    @Test
    @SuppressWarnings("all")
    public void lastDataShouldNotBeFoundFromContextAsAttribute() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        when(givenAttribute.get()).thenReturn(null);

        final Optional<Data> optionalActual = this.contextAttributeManager.findLastData(givenContext);
        assertTrue(optionalActual.isEmpty());

        verify(givenContext, times(1)).channel();
        verify(givenChannel, times(1)).attr(this.attributeKeyDataArgumentCaptor.capture());
        verify(givenAttribute, times(1)).get();

        assertEquals(NAME_ATTRIBUTE_KEY_LAST_DATA, this.attributeKeyDataArgumentCaptor.getValue().name());
    }
}
