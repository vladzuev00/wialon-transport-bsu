package by.bsu.wialontransport.protocol.it.tempdecode;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.protocol.core.decoder.BinaryProtocolDecoder;
import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;

import java.util.List;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.Assert.assertEquals;

public abstract class BinaryProtocolDecodeIT extends AbstractSpringBootTest {

    protected final void test(final byte[] givenBytes, final Package expected) {
        final BinaryProtocolDecoder givenProtocolDecoder = new BinaryProtocolDecoder(getPackageDecoders());
        final EmbeddedChannel givenChannel = new EmbeddedChannel(givenProtocolDecoder);
        final ByteBuf givenBuffer = wrappedBuffer(givenBytes);
        givenChannel.writeInbound(givenBuffer);
        final Object actual = givenChannel.readInbound();
        assertEquals(expected, actual);
    }

    protected abstract List<? extends PackageDecoder<ByteBuf>> getPackageDecoders();
}
