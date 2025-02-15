package by.vladzuev.locationreceiver.config;

import by.vladzuev.locationreceiver.protocol.core.decoder.BinaryProtocolDecoder;
import by.vladzuev.locationreceiver.protocol.core.decoder.TextProtocolDecoder;
import by.vladzuev.locationreceiver.protocol.core.encoder.ProtocolEncoder;
import by.vladzuev.locationreceiver.protocol.jt808.decoder.JT808PackageDecoder;
import by.vladzuev.locationreceiver.protocol.newwing.decoder.NewWingEventCountPackageDecoder;
import by.vladzuev.locationreceiver.protocol.newwing.decoder.NewWingLoginPackageDecoder;
import by.vladzuev.locationreceiver.protocol.newwing.decoder.location.NewWingLocationPackageDecoder;
import by.vladzuev.locationreceiver.protocol.newwing.encoder.NewWingPackageEncoder;
import by.vladzuev.locationreceiver.protocol.wialon.decoder.WialonPackageDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProtocolConfig {

    @Bean
    public BinaryProtocolDecoder jt808ProtocolDecoder(final List<JT808PackageDecoder> packageDecoders) {
        return new BinaryProtocolDecoder(packageDecoders);
    }

    @Bean
    public BinaryProtocolDecoder newWingProtocolDecoder(final NewWingLoginPackageDecoder loginPackageDecoder,
                                                        final NewWingEventCountPackageDecoder eventCountPackageDecoder,
                                                        final NewWingLocationPackageDecoder locationPackageDecoder) {
        return new BinaryProtocolDecoder(List.of(loginPackageDecoder, eventCountPackageDecoder, locationPackageDecoder));
    }

    @Bean
    public ProtocolEncoder newWingProtocolEncoder(final List<NewWingPackageEncoder<?>> encoders) {
        return new ProtocolEncoder(encoders);
    }

    @Bean
    public TextProtocolDecoder wialonProtocolDecoder(final List<WialonPackageDecoder> packageDecoders) {
        return new TextProtocolDecoder(packageDecoders);
    }
}
