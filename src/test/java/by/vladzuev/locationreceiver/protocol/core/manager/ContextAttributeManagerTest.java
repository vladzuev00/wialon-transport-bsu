package by.vladzuev.locationreceiver.protocol.core.manager;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ContextAttributeManagerTest {
    private final ContextAttributeManager manager = new ContextAttributeManager();

    @Test
    public void imeiShouldBePut() {
        testPut("11111222223333344444", KEY_IMEI, ContextAttributeManager::putImei);
    }

    @Test
    public void trackerShouldBePut() {
        testPut(Tracker.builder().build(), KEY_TRACKER, ContextAttributeManager::putTracker);
    }

    @Test
    public void locationShouldBePut() {
        testPut(Location.builder().build(), KEY_LAST_LOCATION, ContextAttributeManager::putLastLocation);
    }

    @Test
    public void imeiShouldBeFound() {
        testSuccessFind("11111222223333344444", KEY_IMEI, ContextAttributeManager::findImei);
    }

    @Test
    public void imeiShouldNotBeFound() {
        testFailedFind(KEY_IMEI, ContextAttributeManager::findImei);
    }

    @Test
    public void trackerShouldBeFound() {
        testSuccessFind(Tracker.builder().build(), KEY_TRACKER, ContextAttributeManager::findTracker);
    }

    @Test
    public void trackerShouldNotBeFound() {
        testFailedFind(KEY_TRACKER, ContextAttributeManager::findTracker);
    }

    @Test
    public void lastLocationShouldBeFound() {
        testSuccessFind(Location.builder().build(), KEY_LAST_LOCATION, ContextAttributeManager::findLastLocation);
    }

    @Test
    public void lastLocationShouldNotBeFound() {
        testFailedFind(KEY_LAST_LOCATION, ContextAttributeManager::findLastLocation);
    }

    private <T> void testPut(final T givenObject,
                             final AttributeKey<T> expectedKey,
                             final AttributePutOperation<T> operation) {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Attribute<T> givenAttribute = mockAttribute(givenContext, expectedKey);
        operation.execute(manager, givenContext, givenObject);
        verify(givenAttribute, times(1)).set(same(givenObject));
    }

    private <T> Attribute<T> mockAttribute(final ChannelHandlerContext context, final AttributeKey<T> key) {
        final Channel channel = mock(Channel.class);
        when(context.channel()).thenReturn(channel);
        @SuppressWarnings("unchecked") final Attribute<T> attribute = mock(Attribute.class);
        when(channel.attr(same(key))).thenReturn(attribute);
        return attribute;
    }

    private <T> void testSuccessFind(final T expectedObject,
                                     final AttributeKey<T> expectedKey,
                                     final AttributeFindOperation<T> operation) {
        final Optional<T> optionalActual = testFind(expectedObject, expectedKey, operation);
        assertTrue(optionalActual.isPresent());
        final T actual = optionalActual.get();
        assertSame(expectedObject, actual);
    }

    private <T> void testFailedFind(final AttributeKey<T> expectedKey, final AttributeFindOperation<T> operation) {
        final Optional<T> optionalActual = testFind(null, expectedKey, operation);
        assertTrue(optionalActual.isEmpty());
    }

    private <T> Optional<T> testFind(final T expectedObject,
                                     final AttributeKey<T> expectedKey,
                                     final AttributeFindOperation<T> operation) {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Attribute<T> givenAttribute = mockAttribute(givenContext, expectedKey);
        when(givenAttribute.get()).thenReturn(expectedObject);
        return operation.execute(manager, givenContext);
    }

    @FunctionalInterface
    private interface AttributePutOperation<T> {
        void execute(ContextAttributeManager manager, ChannelHandlerContext context, T object);
    }

    @FunctionalInterface
    private interface AttributeFindOperation<T> {
        Optional<T> execute(ContextAttributeManager manager, ChannelHandlerContext context);
    }
}
